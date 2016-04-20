package controller.iaUtils;

import java.util.ArrayList;

import controller.GameController;
import controller.IAController;
import controller.PlateauController;
import model.Case;
import model.Joueur;
import model.Pion;

public class NegaMax {

	private static ArrayList<Pion> pionsIA = new ArrayList<>();
	private static ArrayList<Pion> pionsAdversaire = new ArrayList<>();
	private static int maxProfondeur = -1;
	private static ArrayList<IAController.ScoreCoupsInitiaux> coupsJouables = new ArrayList<>();
	private static Joueur iaAppellante;
	private static Case caseAlreadyPlayed;
	private static Pion pionAlreadyPlayed;
	
	private static EvaluatePosition.Setup setup;

	public static int getAutreJoueur(int numeroJoueur){
		return 1 - numeroJoueur;
	}

	public static double negaMax(double alpha, double beta, int profondeur, int numeroJoueurActuel){

		if(profondeur == maxProfondeur){
			if(numeroJoueurActuel == iaAppellante.getNumeroJoueur()){
				return EvaluatePosition.evaluate(pionsIA, pionsAdversaire, PlateauController.getCases(), setup);
			}else{
				return -EvaluatePosition.evaluate(pionsIA, pionsAdversaire, PlateauController.getCases(), setup);
			}
		} else{
			double bestValue = -EvaluatePosition.maxValuePossible();



			//**************************************************************
			//OBTENTION DE TOUS LES DEPLACEMENTS

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

			//**************************************************************


			if(profondeur == 0){
				coupsJouables.clear();
			}


			//si le joueur ne peut pas bouger
			if(!canMove){
				bestValue = -negaMax(-beta, -alpha, profondeur + 1, getAutreJoueur(numeroJoueurActuel));
			}
			else{
				for(IAController.Deplacements deplacement:listeTousDeplacement){
					Case caseOrigine = deplacement.getPion().getCasePlateau();
					double scoreActuel = 0;
					for(Case casePlateau: deplacement.getCases()){
//						if(profondeur == 0 && caseAlreadyPlayed == casePlateau && pionAlreadyPlayed == deplacement.getPion()){
//							continue;
//						}
						//**************************************************************
						//JOUER LE COUP

						ArrayList<Pion> pionsASupprimer = null;
						//on joue le coup
						deplacerPion(deplacement.getPion(), casePlateau);
						//vérification des pions supprimés
						pionsASupprimer = PlateauController.verifierCapture(deplacement.getPion(), PlateauController.getCases());
						//suppression des pions
						supprimerPions(pionsASupprimer, pionsIA, pionsAdversaire, PlateauController.getCases());

						//**************************************************************


						//si cela entraine une victoire le score actuel devient MAXVALUE et on ne continue pas à explorer l'arbre
						if(IAController.isGameOver(pionsIA, pionsAdversaire)){
							scoreActuel = Double.MAX_VALUE;
						}else{
							scoreActuel = -negaMax(-beta, -alpha, profondeur + 1, getAutreJoueur(numeroJoueurActuel));	
						}



						//**************************************************************
						//ANNULER LE COUP

						remettrePions(pionsASupprimer);
						deplacerPion(deplacement.getPion(), caseOrigine);

						//**************************************************************


						//**************************************************************
						//RECUPERATION DES COUPS A PROFONDEUR 0

						//si on à profondeur 0, on sauve le coup potentiel dans une liste des coups à jouer par l'ia
						if(profondeur == 0){
							coupsJouables.add(new IAController.ScoreCoupsInitiaux(deplacement.getPion(), casePlateau, scoreActuel));
						}

						//**************************************************************



						if(scoreActuel > bestValue){
							bestValue = scoreActuel;
							if(bestValue > alpha){
								alpha = bestValue;
								if(alpha >= beta){
									return bestValue;
								}
							}
						}	
						
						
					}//Fin du for pour tous les deplacements
				}//Fin du for pour tous les pions	
			}//Fin du else can move	
			return bestValue;
		}//Fin du else profondeur max pas atteinte
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
		}else if (liste2.contains(pion)){
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
		if(GameController.isAllIA() && coupsJouables.size() == 1 && coupsJouables.get(0).getCase() == caseAlreadyPlayed && coupsJouables.get(0).getPion() == pionAlreadyPlayed){
			return coupsJouables.get(0);
		}
		else{
			for(IAController.ScoreCoupsInitiaux sci: coupsJouables){
				//autre version pour avoir des parties différentes (avis final: très mauvais sur la qualité de l'ia)
				if(setup.isRandomisedAfter()){
					sci.randomizeScore();
				}
				if(GameController.isAllIA()){
					if(sci.getScore() > best.getScore() && sci.getCase() != caseAlreadyPlayed && sci.getPion() != pionAlreadyPlayed){
						best = sci;
					}
				}else if(sci.getScore() > best.getScore()){
					best = sci;
				}
			}
		}
		return best;
	}

	public static IAController.ScoreCoupsInitiaux launchNegaMax(ArrayList<Pion> pionsIA, ArrayList<Pion> pionsAdversaire, int maxProfondeur, Case caseAlreadyPlayed, Pion pionAlreadyPlayed,Joueur iaAppellante, EvaluatePosition.Setup setup){
		NegaMax.pionsIA = pionsIA;
		NegaMax.pionsAdversaire = pionsAdversaire;
		NegaMax.maxProfondeur = maxProfondeur;
		NegaMax.iaAppellante = iaAppellante;
		NegaMax.caseAlreadyPlayed = caseAlreadyPlayed;
		NegaMax.pionAlreadyPlayed = pionAlreadyPlayed;
		NegaMax.setup = setup;
		negaMax(-EvaluatePosition.maxValuePossible(), EvaluatePosition.maxValuePossible(), 0, NegaMax.iaAppellante.getNumeroJoueur());
		return getBestMove();
	}
}
