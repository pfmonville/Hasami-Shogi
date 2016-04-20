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
	
	public static double alphaBetaMiniMax(double alpha, double beta, int profondeur, 
			int numeroJoueurActuel, EvaluatePosition.Setup setup){
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
				maxValue = alphaBetaMiniMax(alpha, beta, profondeur + 1, iaAppellante.getNumeroAdversaire(), setup);
			}else{
				minValue = alphaBetaMiniMax(alpha, beta, profondeur + 1, iaAppellante.getNumeroJoueur(), setup);
			}
		}
		
		//sinon pour chaque déplacement possible
		else{
			for(IAController.Deplacements deplacement:listeTousDeplacement){
				Case caseOrigine = deplacement.getPion().getCasePlateau();
				double scoreActuel = 0;
				for(Case casePlateau: deplacement.getCases()){
					
					
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
							scoreActuel = alphaBetaMiniMax(alpha, beta, profondeur + 1, AlphaBeta.iaAppellante.getNumeroAdversaire(), setup);	
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
							scoreActuel = alphaBetaMiniMax(alpha, beta, profondeur + 1, AlphaBeta.iaAppellante.getNumeroJoueur(), setup);
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
	
	private static void deplacerPion(Pion pion, Case casePlateau){
		//d'abord on supprime la reference du pion dans l'ancienne case
		pion.getCasePlateau().setPion(null);
		//on met à jour le pion
		pion.setCasePlateau(casePlateau);
		//on met à jour la nouvelle case
		casePlateau.setPion(pion);
	}
	
	private static void supprimerPions(ArrayList<Pion> pions, ArrayList<Pion> liste1, ArrayList<Pion> liste2, Case[][] plateau){
		if(pions.isEmpty()){
			return;
		}
		for(Pion pion:pions){
			supprimerPion(pion, liste1, liste2, plateau);
		}
	}

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
	
	public static IAController.ScoreCoupsInitiaux getBestMove(){
		IAController.ScoreCoupsInitiaux best = coupsJouables.get(0);
		//si jouer le même coups que précédemment est la seule possibilité alors on le joue
		if(coupsJouables.size() == 1 && coupsJouables.get(0).getCase() == caseAlreadyPlayed && coupsJouables.get(0).getPion() == pionAlreadyPlayed){
			return coupsJouables.get(0);
		}
		for(IAController.ScoreCoupsInitiaux sci: coupsJouables){
			if(sci.getScore() > best.getScore() && sci.getCase() != caseAlreadyPlayed && sci.getPion() != pionAlreadyPlayed){
				best = sci;
			}
		}
		return best;
	}
	
	public static IAController.ScoreCoupsInitiaux launchAlphaBeta(ArrayList<Pion> pionsIA, ArrayList<Pion> pionsAdversaire, 
			int maxProfondeur, Case caseAlreadyPlayed, Pion pionAlreadyPlayed,Joueur iaAppellante, EvaluatePosition.Setup setup){
		AlphaBeta.pionsIA = pionsIA;
		AlphaBeta.pionsAdversaire = pionsAdversaire;
		AlphaBeta.maxProfondeur = maxProfondeur;
		AlphaBeta.iaAppellante = iaAppellante;
		AlphaBeta.caseAlreadyPlayed = caseAlreadyPlayed;
		AlphaBeta.pionAlreadyPlayed = pionAlreadyPlayed;
		alphaBetaMiniMax(-EvaluatePosition.maxValuePossible(), EvaluatePosition.maxValuePossible(), 0, AlphaBeta.iaAppellante.getNumeroJoueur(), setup);
		return getBestMove();
	}
}
