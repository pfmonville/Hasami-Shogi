package controller.iaUtils;

import java.util.ArrayList;
import java.util.Collections;

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
	
	
	public static int compteur = 0;
	public static boolean test = false;

	/**
	 * 
	 * @param numeroJoueur le numéro du joueur actuel
	 * @return le numéro de son adversaire
	 */
	public static int getAutreJoueur(int numeroJoueur){
		return 1 - numeroJoueur;
	}

	/**
	 * algorithme récursif permettant de parcourir l'arbre des coups possible en élaguant les moins bonnes branches avec une meilleur implémentation que alphaBeta
	* @param alpha
	 * @param beta
	 * @param profondeur la profondeur actuelle
	 * @param numeroJoueurActuel le numéro du joueur dont les coups possibles vont être considérés pendant cet appel
	 * @return le score du meilleur fils
	 */
	public static double negaMax(double alpha, double beta, int profondeur, int numeroJoueurActuel){

		compteur++;
		
		if(profondeur == maxProfondeur){
			if(numeroJoueurActuel == iaAppellante.getNumeroJoueur()){
				return EvaluatePosition.evaluate(pionsIA, pionsAdversaire, PlateauController.getCases(), setup);
			}else{
				return -EvaluatePosition.evaluate(pionsIA, pionsAdversaire, PlateauController.getCases(), setup);
			}
		} else{
			double bestValue = -EvaluatePosition.maxValuePossible();



//			//**************************************************************
//			//OBTENTION DE TOUS LES DEPLACEMENTS
//
//			ArrayList<IAController.Deplacement> listeTousDeplacement = new ArrayList<>();
//			boolean canMove = false;
//			//pour chaque pion jouable
//			if(numeroJoueurActuel == pionsIA.get(0).getNumeroJoueur()){
//				for(Pion pion:pionsIA){
//					//estimation de tous les coups possibles d'un pion
//					ArrayList<Case> deplacementPossibles = PlateauController.getPossibleMoves(pion);
//					if(!deplacementPossibles.isEmpty()){
//						canMove = true;
//					}
//					IAController.Deplacement deplacements = new IAController.Deplacement(pion, deplacementPossibles);
//					listeTousDeplacement.add(deplacements);
//				}
//			}else{
//				for(Pion pion:pionsAdversaire){
//					//estimation de tous les coups possibles d'un pion
//					ArrayList<Case> deplacementPossibles = PlateauController.getPossibleMoves(pion);
//					if(!deplacementPossibles.isEmpty()){
//						canMove = true;
//					}
//					IAController.Deplacement deplacements = new IAController.Deplacement(pion, deplacementPossibles);
//					listeTousDeplacement.add(deplacements);
//				}
//			}
//
//			//**************************************************************
//
//
//			if(profondeur == 0){
//				coupsJouables.clear();
//			}
//
//
//			//si le joueur ne peut pas bouger
//			if(!canMove){
//				bestValue = -negaMax(-beta, -alpha, profondeur + 1, getAutreJoueur(numeroJoueurActuel));
//			}
//			else{
//				for(IAController.Deplacement deplacement:listeTousDeplacement){
//					Case caseOrigine = deplacement.getPion().getCasePlateau();
//					double scoreActuel = 0;
//					for(Case casePlateau: deplacement.getCases()){
//						if(profondeur == 0 && caseAlreadyPlayed == casePlateau && pionAlreadyPlayed == deplacement.getPion()){
//							continue;
//						}
//						//**************************************************************
//						//JOUER LE COUP
//
//						ArrayList<Pion> pionsASupprimer = null;
//						//on joue le coup
//						deplacerPion(deplacement.getPion(), casePlateau);
//						//vérification des pions supprimés
//						pionsASupprimer = PlateauController.verifierCapture(deplacement.getPion(), PlateauController.getCases());
//						//suppression des pions
//						supprimerPions(pionsASupprimer, pionsIA, pionsAdversaire, PlateauController.getCases());
//
//						//**************************************************************
//
//
//						//si cela entraine une victoire le score actuel devient MAXVALUE et on ne continue pas à explorer l'arbre
//						if(IAController.isGameOver(pionsIA, pionsAdversaire)){
//							scoreActuel = Double.MAX_VALUE;
//						}else{
//							scoreActuel = -negaMax(-beta, -alpha, profondeur + 1, getAutreJoueur(numeroJoueurActuel));	
//						}
//
//
//
//						//**************************************************************
//						//ANNULER LE COUP
//
//						remettrePions(pionsASupprimer);
//						deplacerPion(deplacement.getPion(), caseOrigine);
//
//						//**************************************************************
//
//
//						//**************************************************************
//						//RECUPERATION DES COUPS A PROFONDEUR 0
//
//						//si on à profondeur 0, on sauve le coup potentiel dans une liste des coups à jouer par l'ia
//						if(profondeur == 0){
//							coupsJouables.add(new IAController.ScoreCoupsInitiaux(deplacement.getPion(), casePlateau, scoreActuel));
//						}
//
//						//**************************************************************
//
//
//
//						if(scoreActuel > bestValue){
//							bestValue = scoreActuel;
//							if(bestValue > alpha){
//								alpha = bestValue;
//								if(alpha >= beta){
//									return bestValue;
//								}
//							}
//						}	
//						
//						
//					}//Fin du for pour tous les deplacements
//				}//Fin du for pour tous les pions	
//			}//Fin du else can move	
//			return bestValue;
//		}//Fin du else profondeur max pas atteinte
			
			//**************************************************************
			//OBTENTION DE TOUS LES DEPLACEMENTS

			ArrayList<IAController.Deplacement> listeTousDeplacement = new ArrayList<>();
			boolean canMove = false;
			//pour chaque pion jouable
			if(numeroJoueurActuel == pionsIA.get(0).getNumeroJoueur()){
				for(Pion pion:pionsIA){
					//estimation de tous les coups possibles d'un pion
					ArrayList<Case> deplacementPossibles = PlateauController.getPossibleMoves(pion);
					if(!deplacementPossibles.isEmpty()){
						canMove = true;
					}
					for(Case casePlateau:deplacementPossibles){
						IAController.Deplacement deplacement = new IAController.Deplacement(pion, casePlateau);
						deplacement.setEstimatedScore(EvaluatePosition.getEstimatedScore(pionsIA, pionsAdversaire, pion, casePlateau, PlateauController.getCases()));
						listeTousDeplacement.add(deplacement);
					}
					
					
					//TODO: a supprimer 
					//IAController.Deplacements deplacements = new IAController.Deplacements(pion, deplacementPossibles);
					
					//listeTousDeplacement.add(deplacements);
				}
			}else{
				for(Pion pion:pionsAdversaire){
					//estimation de tous les coups possibles d'un pion
					ArrayList<Case> deplacementPossibles = PlateauController.getPossibleMoves(pion);
					if(!deplacementPossibles.isEmpty()){
						canMove = true;
					}
					for(Case casePlateau:deplacementPossibles){
						IAController.Deplacement deplacement = new IAController.Deplacement(pion, casePlateau);
						deplacement.setEstimatedScore(EvaluatePosition.getEstimatedScore(pionsIA, pionsAdversaire, pion, casePlateau, PlateauController.getCases()));
						listeTousDeplacement.add(deplacement);
					}
					
					//TODO: a supprimer
//					IAController.Deplacements deplacements = new IAController.Deplacements(pion, deplacementPossibles);
//					listeTousDeplacement.add(deplacements);
				}
			}

			//**************************************************************


			if(profondeur == 0){
				coupsJouables.clear();
			}


			//on trie la liste des déplacements afin qu'elle soit dans l'ordre des coups les plus intéressants en premier
			if(test){
				Collections.sort(listeTousDeplacement);
				Collections.reverse(listeTousDeplacement);
			}
			
			
			//si le joueur ne peut pas bouger
			if(!canMove){
				bestValue = -negaMax(-beta, -alpha, profondeur + 1, getAutreJoueur(numeroJoueurActuel));
			}
			else{
				for(IAController.Deplacement deplacement:listeTousDeplacement){
					Case caseOrigine = deplacement.getPion().getCasePlateau();
					double scoreActuel = 0;
					if(profondeur == 0 && caseAlreadyPlayed == deplacement.getCase() && pionAlreadyPlayed == deplacement.getPion()){
						continue;
					}
					//**************************************************************
					//JOUER LE COUP

					ArrayList<Pion> pionsASupprimer = null;
					//on joue le coup
					deplacerPion(deplacement.getPion(), deplacement.getCase());
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
						coupsJouables.add(new IAController.ScoreCoupsInitiaux(deplacement.getPion(), deplacement.getCase(), scoreActuel));
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
					
				}//Fin du for pour tous les pions	
			}//Fin du else can move	
			return bestValue;
		}//Fin du else profondeur max pas atteinte
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
	public static void supprimerPions(ArrayList<Pion> pions, ArrayList<Pion> liste1, ArrayList<Pion> liste2, Case[][] plateau){
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
		}else if (liste2.contains(pion)){
			liste2.remove(pion);
		}
	}

	
	/**
	 * permet de remettre les pions qui ont été supprimés,
	 * permet d'éviter de faire des clones
	 * @param pionsARemmettre la liste des pions qui ont été précedemment supprimés
	 */
	public static void remettrePions(ArrayList<Pion> pionsARemmettre){
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
			//autre version pour avoir des parties différentes (avis final: très mauvais sur la qualité de l'ia)
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
	 * @param caseAlreadyPlayed la case déjà jouée précédemment (pour éviter les répétitions)
	 * @param pionAlreadyPlayed le pion déjà joué précédemment (pour éviter les répétitions)
	 * @param iaAppellante la classe Joueur de l'IA qui appelle (permet de récupérer le numéro du joueur qui a appelé)
	 * @param setup classe spécial permettant de régler la fonction d'évaluation(combien de coefs pris en compte, les valeurs des coefs, si on randomise après)
	 * @return le meilleur des coups sous forme de ScoreCoupsInitiaux
	 */
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
