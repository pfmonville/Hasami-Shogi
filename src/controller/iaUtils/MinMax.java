package controller.iaUtils;

import java.util.ArrayList;

import controller.PlateauController;
import mainPackage.App;
import model.Case;
import model.Pion;

public class MinMax implements Cloneable{

	//liste mémorisant tous les coups possibles
	private static ArrayList<Object[]> coupsPossibles;

	/**
	 * Cette fonction permet de trouver le déplacement le plus judicieux à effectuer en estimant que l'adversaire joue au mieux.
	 * Application de l'algorithme min-max
	 * @param profondeur profondeur souhaitée pour la recherche
	 * @param pionsIA pions de l'IA qui fait la recherche
	 * @param pionsAdversaire pions de l'adversaire
	 * @return retourne le déplacement le plus optimisé selon la profondeur renseignée
	 * @throws CloneNotSupportedException 
	 */
	public static Object[] minMaxDecision(int profondeur, ArrayList<Pion> pionsIA, ArrayList<Pion> pionsAdversaire, 
		EvaluatePosition.Setup setup) throws CloneNotSupportedException{

		//initialisation de la liste des coups possibles
		coupsPossibles = new ArrayList<Object[]>();
		
		//cette fonction va modifier le tableau déplacement à retourner automatiquement
		maxValue(0, profondeur, PlateauController.getCases(), pionsIA, pionsAdversaire, setup);
		
		//parcours de la liste de coups possibles
		double score = -EvaluatePosition.maxValuePossible();
		Object[] best = null;
		for(Object[] coup:coupsPossibles){
			//on retient le max de la liste
			if((double)coup[2] > score){
				best = coup;
				score = (double)coup[2];
			}
		}
		
		//on réinitialise le plateau
		MinMax.init(PlateauController.getCases(), pionsIA, pionsAdversaire);
		//on retourne le meilleur choix de déplacement
		return best;
	}







	
	@SuppressWarnings("unchecked")
	private static double maxValue(int profondeur, int profondeurMax, Case[][] plateau, ArrayList<Pion> pionsJoueurCourant, 
		ArrayList<Pion> pionsAdversaire, EvaluatePosition.Setup setup) throws CloneNotSupportedException{

		//on initialise la valeur de score au résultat le plus petit possible
		double score = -EvaluatePosition.maxValuePossible();

		//si profondeur voulue non atteinte
		if(profondeur !=profondeurMax){
			
			//pour chaque pion jouable
			for(Pion pion:pionsJoueurCourant){			

				//estimation de tous les coups possibles d'un pion
				ArrayList<Case> deplacementsPossibles = PlateauController.getPossibleMoves(pion);

				//pour chaque coup possible
				for(Case casePlateau:deplacementsPossibles){
					
					
					//copie de tous les éléments à modifier
					Case[][] plateauBis = plateau.clone();
					ArrayList<Pion> pionsJoueurCourantBis = (ArrayList<Pion>) pionsJoueurCourant.clone();
					ArrayList<Pion> pionsAdversaireBis = (ArrayList<Pion>) pionsAdversaire.clone();
					Pion pionBis = pion.clone();
					
					//initialisation du plateau cloné
					MinMax.init(plateauBis, pionsJoueurCourantBis, pionsAdversaireBis);
					
					//déplacement du pion
					MinMax.deplacerPion(pionBis, plateauBis, casePlateau);
					
					//vérification des pions supprimés
					ArrayList<Pion> pionsASupprimer = PlateauController.verifierCapture(pionBis, plateauBis);
					MinMax.supprimerPions(pionsASupprimer, pionsJoueurCourantBis, pionsAdversaireBis, plateauBis);
					
					//on garde en mémoire dans une liste le score de chaque coup
					if(profondeur == 0){
						Object[] coup = new Object[3];
						coup[0] = pion;
						coup[1] = casePlateau;
						coup[2] = MinMax.minValue(profondeur + 1, profondeurMax, plateauBis, pionsAdversaireBis, 
								pionsJoueurCourantBis, setup);
						score = Math.max(score, (double)coup[2]);
						coupsPossibles.add(coup);
					}

					else{
						//calcul du score
						score = Math.max(score, MinMax.minValue(profondeur + 1, profondeurMax, plateauBis, 
								pionsAdversaireBis, pionsJoueurCourantBis, setup));
					}
				}//fin pour tous les déplacements d'un pion
			}//fin pour tous les pions
		}//fin if profondeur différent profondeur max

		//profondeur voulue atteinte
		else{
			score = EvaluatePosition.evaluate(pionsJoueurCourant, pionsAdversaire, plateau, setup);
		}

		//on retourne le résultat
		return score;
	}








	@SuppressWarnings("unchecked")
	private static double minValue(int profondeur, int profondeurMax, Case[][] plateau, ArrayList<Pion> pionsJoueurCourant, 
		ArrayList<Pion> pionsAdversaire, EvaluatePosition.Setup setup) throws CloneNotSupportedException{
		
		//on initialise la valeur de score au résultat le plus petit possible
		double score = EvaluatePosition.maxValuePossible();

		//si profondeur voulue non atteinte
		if(profondeur !=profondeurMax){
			
			//pour chaque pion jouable
			for(Pion pion:pionsJoueurCourant){
				//estimation de tous les coups possibles d'un pion
				ArrayList<Case> deplacementsPossibles = PlateauController.getPossibleMoves(pion);

				//pour chaque coup possible
				for(Case casePlateau:deplacementsPossibles){
					
					//copie de tous les éléments à modifier
					Case[][] plateauBis = plateau.clone();
					ArrayList<Pion> pionsJoueurCourantBis = (ArrayList<Pion>) pionsJoueurCourant.clone();
					ArrayList<Pion> pionsAdversaireBis = (ArrayList<Pion>) pionsAdversaire.clone();
					Pion pionBis = (Pion) pion.clone();
					
					//initialisation du plateau cloné
					MinMax.init(plateauBis, pionsJoueurCourantBis, pionsAdversaireBis);
					
					//déplacement du pion
					MinMax.deplacerPion(pionBis, plateauBis, casePlateau);
					
					//vérification des pions supprimés
					ArrayList<Pion> pionsASupprimer = PlateauController.verifierCapture(pionBis, plateauBis);
					MinMax.supprimerPions(pionsASupprimer, pionsJoueurCourantBis, pionsAdversaireBis, plateauBis);

					//calcul du score
					score = Math.min(score, MinMax.maxValue(profondeur + 1, profondeurMax, plateauBis, pionsAdversaireBis, 
							pionsJoueurCourantBis, setup));
				}
			}
		}

		//profondeur voulue atteinte
		else{
			score = EvaluatePosition.evaluate(pionsAdversaire, pionsJoueurCourant, plateau, setup);
		}

		//on retourne le résultat
		return score;
	}




	private static void supprimerPions(ArrayList<Pion> pions, ArrayList<Pion> liste1, ArrayList<Pion> liste2, Case[][] plateau){
		for(Pion pion:pions){
			supprimerPion(pion, liste1, liste2, plateau);
		}
	}

	private static void supprimerPion(Pion pion, ArrayList<Pion> liste1, ArrayList<Pion> liste2, Case[][] plateau){
		if(liste1.contains(pion)){
			liste1.remove(pion);
		}else{
			liste2.remove(pion);
		}
		plateau[pion.getCasePlateau().getCoordonneeX()][pion.getCasePlateau().getCoordonneeY()].setPion(null);
	}
	
	private static void deplacerPion(Pion pion, Case[][] plateau, Case casePlateau){
		plateau[pion.getCasePlateau().getCoordonneeX()][pion.getCasePlateau().getCoordonneeY()].setPion(null);
		pion.setCasePlateau(casePlateau);
		plateau[casePlateau.getCoordonneeX()][casePlateau.getCoordonneeY()].setPion(pion);
	}

	
	private static void init(Case[][] plateau, ArrayList<Pion> liste1, ArrayList<Pion> liste2){
		
		//on initialise le plateau à vide
		for(int i=0;i<App.regles.getNbDeCaseParLigne(); i++){
			for(int j=0;j<App.regles.getNbDeCaseParLigne();j++){
				plateau[i][j].setPion(null);
			}
		}
		
		//on ajoute les éléments de la liste 1
		for(Pion pion:liste1){
			plateau[pion.getCasePlateau().getCoordonneeX()][pion.getCasePlateau().getCoordonneeY()].setPion(pion);
		}
		
		//on ajoute les éléments de la liste 2
		for(Pion pion:liste2){
			plateau[pion.getCasePlateau().getCoordonneeX()][pion.getCasePlateau().getCoordonneeY()].setPion(pion);
		}
	}

}