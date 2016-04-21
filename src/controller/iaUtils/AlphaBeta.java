package controller.iaUtils;

import java.util.ArrayList;

import controller.IAController;
import controller.PlateauController;
import model.Case;
import model.Joueur;
import model.Pion;

public class AlphaBeta {
	
	private static ArrayList<Pion> pionsIA = new ArrayList<>();
	private static ArrayList<Pion> pionsAdversaire = new ArrayList<>();
	private static int maxProfondeur = -1;
	private static ArrayList<IAController.ScoreCoupsInitiaux> coupsJouables = new ArrayList<>();
	private static Joueur iaAppellante;
	private static Case caseAlreadyPlayed;
	private static Pion pionAlreadyPlayed;
	
	private static EvaluatePosition.Setup setup;
	
	
	/**
	 * algorithme récursif permettant de parcourir l'arbre des coups possible en élaguant les moins bonnes branches
	 * @param alpha
	 * @param beta
	 * @param profondeur la profondeur actuelle
	 * @param numeroJoueurActuel le numéro du joueur dont les coups possibles vont être considérés pendant cet appel
	 * @return le score du meilleur fils
	 */
	public static double alphaBetaMiniMax(double alpha, double beta, int profondeur, 
			int numeroJoueurActuel){
		if(beta < alpha){
			if(numeroJoueurActuel == iaAppellante.getNumeroJoueur()){
				return Integer.MAX_VALUE;
			}else{
				return -Integer.MAX_VALUE;
			}
		}
		
		if(profondeur == maxProfondeur){
			return EvaluatePosition.evaluate(pionsIA, pionsAdversaire, PlateauController.getCases(), setup);
		}
		
		//on obtient la liste de tous les déplacements
		ArrayList<IAController.Deplacements> listeTousDeplacement = new ArrayList<>();
		
		
		boolean canMove = false;
		//pour chaque pion jouable
		if(numeroJoueurActuel == pionsIA.get(0).getNumeroJoueur()){
			for(Pion pion:pionsIA){
				//estimation de tous les coups possibles d'un pion
				ArrayList<Case> deplacementPossibles = PlateauController.getPossibleMoves(pion);
				if(!deplacementPossibles.isEmpty()){
					canMove = true;
				}
				IAController.Deplacements deplacements = new IAController.Deplacements(pion, deplacementPossibles);
				listeTousDeplacement.add(deplacements);
			}
		}else{
			for(Pion pion:pionsAdversaire){
				//estimation de tous les coups possibles d'un pion
				ArrayList<Case> deplacementPossibles = PlateauController.getPossibleMoves(pion);
				if(!deplacementPossibles.isEmpty()){
					canMove = true;
				}
				IAController.Deplacements deplacements = new IAController.Deplacements(pion, deplacementPossibles);
				listeTousDeplacement.add(deplacements);
			}
		}
		
		if(profondeur == 0){
			coupsJouables.clear();
		}
		
		double maxValue = -EvaluatePosition.maxValuePossible();
		double minValue = EvaluatePosition.maxValuePossible();
		
		
		//si le joueur ne peut pas bouger
		if(!canMove){
			//si c'est au joueur qui a appellé alphabeta
			if(numeroJoueurActuel == AlphaBeta.iaAppellante.getNumeroJoueur()){
				maxValue = alphaBetaMiniMax(alpha, beta, profondeur + 1, iaAppellante.getNumeroAdversaire());
			}else{
				minValue = alphaBetaMiniMax(alpha, beta, profondeur + 1, iaAppellante.getNumeroJoueur());
			}
		}
		
		//sinon pour chaque déplacement possible
		else{
			for(IAController.Deplacements deplacement:listeTousDeplacement){
				Case caseOrigine = deplacement.getPion().getCasePlateau();
				double scoreActuel = 0;
				for(Case casePlateau: deplacement.getCases()){
					if(profondeur == 0 && caseAlreadyPlayed == casePlateau && pionAlreadyPlayed == deplacement.getPion()){
						continue;
					}
					
					ArrayList<Pion> pionsASupprimer = null;
					
					
					//si c'est au joueur qui a appellé alphabeta
					if(numeroJoueurActuel == AlphaBeta.iaAppellante.getNumeroJoueur()){
						
						//on joue le coup
						deplacerPion(deplacement.getPion(), casePlateau);
						//vérification des pions supprimés
						pionsASupprimer = PlateauController.verifierCapture(deplacement.getPion(), PlateauController.getCases());
						//suppression des pions
						supprimerPions(pionsASupprimer, pionsIA, pionsAdversaire, PlateauController.getCases());
						
						//si cela entraine une victoire le score actuel devient MAXVALUE et on ne continue pas à explorer l'arbre
						if(IAController.isGameOver(pionsIA, pionsAdversaire)){
							scoreActuel = Double.MAX_VALUE;
						}else{
							scoreActuel = alphaBetaMiniMax(alpha, beta, profondeur + 1, AlphaBeta.iaAppellante.getNumeroAdversaire());	
						}
						
						maxValue = Math.max(maxValue, scoreActuel);
						
						//set alpha
						alpha = Math.max(scoreActuel, alpha);
						//si on à profondeur 0, on sauve le coup potentiel dans une liste des coups à jouer par l'ia
						if(profondeur == 0){
							coupsJouables.add(new IAController.ScoreCoupsInitiaux(deplacement.getPion(), casePlateau, scoreActuel));
						}
						
					}
					//si c'est à l'adversaire
					else{
						//on joue le coup
						deplacerPion(deplacement.getPion(), casePlateau);
						//vérification des pions supprimés
						pionsASupprimer = PlateauController.verifierCapture(deplacement.getPion(), PlateauController.getCases());
						//suppression des pions
						supprimerPions(pionsASupprimer, pionsIA, pionsAdversaire, PlateauController.getCases());
						
						//si cela entraine une victoire le score actuel devient MINVALUE et on ne continue pas à explorer l'arbre
						if(IAController.isGameOver(pionsIA, pionsAdversaire)){
							scoreActuel = EvaluatePosition.maxValuePossible();
						}else{
							scoreActuel = alphaBetaMiniMax(alpha, beta, profondeur + 1, AlphaBeta.iaAppellante.getNumeroJoueur());
						}
						
						minValue = Math.min(minValue, scoreActuel);
						
						//set beta
						beta = Math.min(scoreActuel, beta);
						
						
						
					}
					
					//annuler le coup
					remettrePions(pionsASupprimer);
					deplacerPion(deplacement.getPion(), caseOrigine);
					
					//si une coupure à eu lieu on n'évalue pas le reste de l'arbre
					if(scoreActuel == EvaluatePosition.maxValuePossible() || scoreActuel == -EvaluatePosition.maxValuePossible()){
//						break;
					}
				}
				//si une coupure à eu lieu on n'évalue pas le reste de l'arbre
				if(scoreActuel == EvaluatePosition.maxValuePossible() || scoreActuel == -EvaluatePosition.maxValuePossible()){
//					break;
				}
			}
		}
		
		return numeroJoueurActuel == iaAppellante.getNumeroJoueur() ? maxValue : minValue;
	}
	
	/**
	 * permet de déplacer le pion sans toucher à l'UI
	 * @param pion le pion à déplacer
	 * @param casePlateau le plateau
	 */
	private static void deplacerPion(Pion pion, Case casePlateau){
		//d'abord on supprime la reference du pion dans l'ancienne case
		pion.getCasePlateau().setPion(null);
		//on met à jour le pion
		pion.setCasePlateau(casePlateau);
		//on met à jour la nouvelle case
		casePlateau.setPion(pion);
	}
	
	/**
	 * permet de supprimer des pions sans toucher à l'UI, s'occupe tout seul de trouver à qui appartient les pions à supprimer
	 * @param pions la liste de pions à supprimer
	 * @param liste1 la liste des pions du joueur1
	 * @param liste2 la liste des pions du joueurs2
	 * @param plateau le plateau de jeu
	 */
	private static void supprimerPions(ArrayList<Pion> pions, ArrayList<Pion> liste1, ArrayList<Pion> liste2, Case[][] plateau){
		if(pions.isEmpty()){
			return;
		}
		for(Pion pion:pions){
			supprimerPion(pion, liste1, liste2, plateau);
		}
	}
	
	/**
	 * permet de supprimer un pion sans toucher à l'IU
	 * @param pion le pion à supprimer
	 * @param liste1 la liste des pions du joueur1
	 * @param liste2 la liste des pions du joueur2
	 * @param plateau le plateau de jeu
	 */
	private static void supprimerPion(Pion pion, ArrayList<Pion> liste1, ArrayList<Pion> liste2, Case[][] plateau){
		//on supprime la référence du pion pour la case
		pion.getCasePlateau().setPion(null);
		
		//on déconnecte le pion de la liste
		if(liste1.contains(pion)){
			liste1.remove(pion);
		}else{
			liste2.remove(pion);
		}
	}
	
	/**
	 * permet de remettre les pions qui ont été supprimés,
	 * permet d'éviter de faire des clones
	 * @param pionsARemmettre la liste des pions qui ont été précedemment supprimés
	 */
	private static void remettrePions(ArrayList<Pion> pionsARemmettre){
		if(pionsARemmettre.isEmpty()){
			return;
		}
		for(Pion pion:pionsARemmettre){
			//si le pion à remettre appartient à la liste pionsIA on l'y remet
			if(pion.getNumeroJoueur() == iaAppellante.getNumeroJoueur()){
				pionsIA.add(pion);
			}else{
				pionsAdversaire.add(pion);
			}
			pion.getCasePlateau().setPion(pion);
		}
	}
	
	/**
	 * 
	 * @return le meilleur des coups sous forme de ScoreCoupsInitiaux
	 */
	public static IAController.ScoreCoupsInitiaux getBestMove(){
		IAController.ScoreCoupsInitiaux best = coupsJouables.get(0);
		for(IAController.ScoreCoupsInitiaux sci: coupsJouables){
			if(setup.isRandomisedAfter()){
				sci.randomizeScore();
			}
			if(sci.getScore() > best.getScore()){
				best = sci;
			}
		}
		return best;
	}
	
	/**
	 * fonction principal permettant de lancer l'algorithme
	 * @param pionsIA la liste des pions de l'IA
	 * @param pionsAdversaire la liste des pions de son adversaire
	 * @param maxProfondeur la profondeur maximale à atteindre
	 * @param caseAlreadyPlayed la case déjé jouée précédemment (pour éviter les répétitions)
	 * @param pionAlreadyPlayed le pion déjé joué précédemment (pour éviter les répétitions)
	 * @param iaAppellante la classe Joueur de l'IA qui appelle (permet de récupèrer le numéro du joueur qui a appelé)
	 * @param setup classe spécial permettant de régler la fonction d'évaluation(combien de coefs pris en compte, les valeurs des coefs, si on randomise après)
	 * @return le meilleur des coups sous forme de ScoreCoupsInitiaux
	 */
	public static IAController.ScoreCoupsInitiaux launchAlphaBeta(ArrayList<Pion> pionsIA, ArrayList<Pion> pionsAdversaire, 
			int maxProfondeur, Case caseAlreadyPlayed, Pion pionAlreadyPlayed,Joueur iaAppellante, EvaluatePosition.Setup setup){
		AlphaBeta.pionsIA = pionsIA;
		AlphaBeta.pionsAdversaire = pionsAdversaire;
		AlphaBeta.maxProfondeur = maxProfondeur;
		AlphaBeta.iaAppellante = iaAppellante;
		AlphaBeta.caseAlreadyPlayed = caseAlreadyPlayed;
		AlphaBeta.pionAlreadyPlayed = pionAlreadyPlayed;
		AlphaBeta.setup = setup;
		alphaBetaMiniMax(-EvaluatePosition.maxValuePossible(), EvaluatePosition.maxValuePossible(), 0, AlphaBeta.iaAppellante.getNumeroJoueur());
		return getBestMove();
	}
}
