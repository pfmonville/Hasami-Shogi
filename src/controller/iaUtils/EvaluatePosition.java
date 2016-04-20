package controller.iaUtils;

import java.util.ArrayList;

import mainPackage.App;
import model.Case;
import model.Pion;

public class EvaluatePosition {

	private static double scorePartieFinie = Double.MAX_VALUE;

	
	/**
	 * permet d'�valuer la position actuelle du plateau
	 * @param pionsJoueur1
	 * @param pionsJoueur2
	 * @param cases un tableau 2 dimensions contenant les cases du plateau
	 * @param setup classe sp�cial permettant de r�gler la fonction d'�valuation(combien de coefs pris en compte, les valeurs des coefs, si on randomise apr�s)
	 * @return un double repr�sentant le score vu du joueur1
	 */
	public static double evaluate(ArrayList<Pion> pionsJoueur1, ArrayList<Pion> pionsJoueur2, Case[][] cases, Setup setup){

		double result = 0;
		if(setup.isCoef1){
			result += setup.getCoef1() * evaluateMaterial(pionsJoueur1, pionsJoueur2);
		}
		if(setup.isCoef2){
			result += setup.getCoef2() * evaluateMobility(pionsJoueur1, pionsJoueur2, cases);
		}
		if(setup.isCoef3){
			result += setup.getCoef3() * evaluateThreat2(pionsJoueur1, pionsJoueur2, cases);
		}
		if(! setup.isRandomisedAfter){
			result += (Math.random()*0.01)-0.005;
		}
		return result;
	}

	/**
	 * 
	 * @return valeur maximale de la fonction d'�valutation
	 */
	public static double maxValuePossible(){
		return Double.MAX_VALUE;
	}

	/**
	 * 
	 * @return l'�quivalent de + l'infini
	 */
	public static double getScorePartieFinie(){
		return scorePartieFinie;
	}


	/**
	 * calcul le score li� au mat�riel
	 * @param pionsJoueur1 la liste des pions du joueur1
	 * @param pionsJoueur2 la liste des pions du joueur2
	 * @param toMaximased bool�en mis � vrai si on veut r�cup�rer le score maximum que peut g�n�rer cette fonction
	 * @return un double repr�sentant le nombre de pions du joueur1 moins celui du joueur 2
	 */
	private static double evaluateMaterial(ArrayList<Pion> pionsJoueur1, ArrayList<Pion> pionsJoueur2){
		//TODO ajuster la d�fense de l'IA
		return pionsJoueur1.size() - pionsJoueur2.size();
	}


	/**
	 * calcul le score li� � la mobilit� du joueur
	 * @param toMaximised bool�en mis � vrai si on veut r�cup�rer le score maximum que peut g�n�rer cette fonction
	 * @return un double repr�sentant le nombre de cases atteignables par tous les pions du joueur1 moins celles du joueur2
	 */
	private static double evaluateMobility(ArrayList<Pion> pionsJoueur1, ArrayList<Pion> pionsJoueur2,Case[][] cases){
		int compteurDeplacementPossibleJoueur1 = 0;
		for(Pion pion:pionsJoueur1){
			compteurDeplacementPossibleJoueur1 += getNumberOfPossibleMoves(pion, cases);
		}

		int compteurDeplacementPossibleJoueur2 = 0;
		for(Pion pion:pionsJoueur2){
			compteurDeplacementPossibleJoueur2 += getNumberOfPossibleMoves(pion, cases);
		}

		return compteurDeplacementPossibleJoueur1 - compteurDeplacementPossibleJoueur2;
	}


	/**
	 * calcul le score li� � la menace du joueur
	 * @param pionsJoueur1 la liste des pions du joueur1
	 * @param pionsJoueur2 la liste des pions du joueur2
	 * @param toMaximised bool�en mis � vrai si on veut r�cup�rer le score maximum que peut g�n�rer cette fonction
	 * @return un double repr�sentant le nombre de pions que le joueur1 menace de prendre en un coup moins ceux de son adversaire
	 */
	private static double evaluateThreat2(ArrayList<Pion> pionsJoueur1, ArrayList<Pion> pionsJoueur2, Case[][] plateau){
		
		double menaceJoueur1 = 0;
		double menaceJoueur2 = 0;
		
		//menace pour le joueur 1
		for(Pion pion:pionsJoueur1){
			int x = pion.getCasePlateau().getCoordonneeX();
			int y = pion.getCasePlateau().getCoordonneeY();
			Case caseVideDroite = null;
			Case caseVideGauche = null;
			Case caseVideHaut = null;
			Case caseVideBas = null;

			double groupeGauche = 0;
			double groupeDroite = 0;
			double groupeHaut = 0;
			double groupeBas = 0;

			boolean estMenace = false;

			for(int i = pion.getCasePlateau().getCoordonneeX()+1; i < App.regles.getNbDeCaseParLigne(); i++){
				if(plateau[i][y].getPion() != null && plateau[i][y].getPion().getNumeroJoueur() != pion.getNumeroJoueur()){
					estMenace = true;
					groupeGauche++;
				}else if(plateau[i][y].getPion() == null && estMenace){
					caseVideDroite = plateau[i][y];
					break;
				}else{
					break;
				}
			}
			estMenace = false;
			for(int i = pion.getCasePlateau().getCoordonneeX()-1; i >= 0; i--){
				if(plateau[i][y].getPion() != null && plateau[i][y].getPion().getNumeroJoueur() != pion.getNumeroJoueur()){
					estMenace = true;
					groupeGauche++;
				}else if(plateau[i][y].getPion() == null && estMenace){
					caseVideGauche = plateau[i][y];
					break;
				}else{
					break;
				}		
			}
			estMenace = false;
			for(int j = pion.getCasePlateau().getCoordonneeY()-1; j >= 0; j--){
				if(plateau[x][j].getPion() != null && plateau[x][j].getPion().getNumeroJoueur() != pion.getNumeroJoueur()){
					estMenace = true;
					groupeHaut++;
				}else if(plateau[x][j].getPion() == null && estMenace){
					caseVideHaut = plateau[x][j];
					break;
				}else{
					break;
				}
			}
			estMenace = false;
			for(int j = pion.getCasePlateau().getCoordonneeX()+1; j < App.regles.getNbDeCaseParLigne(); j++){
				if(plateau[x][j].getPion() != null && plateau[x][j].getPion().getNumeroJoueur() != pion.getNumeroJoueur()){
					estMenace = true;
					groupeBas++;
				}else if(plateau[x][j].getPion() == null && estMenace){
					caseVideBas = plateau[x][j];
					break;
				}else{
					break;
				}
			}
			
			//test si le pion est vraiment en position d'�chec en v�rifiant que la case vide � c�t� peut �tre atteinte directement par un pion du joueur 1
			if(caseVideGauche != null && check(plateau, caseVideGauche, pion.getNumeroJoueur())){
				menaceJoueur1 += groupeGauche;
			}
			if(caseVideDroite != null && check(plateau, caseVideDroite, pion.getNumeroJoueur())){
				menaceJoueur1 += groupeDroite;
			}
			if(caseVideHaut != null && check(plateau, caseVideHaut, pion.getNumeroJoueur())){
				menaceJoueur1 += groupeHaut;
			}
			if(caseVideBas != null && check(plateau, caseVideBas, pion.getNumeroJoueur())){
				menaceJoueur1 += groupeBas;
			}
		}
		
		//menace pour le joueur 2
		for(Pion pion:pionsJoueur2){
			int x = pion.getCasePlateau().getCoordonneeX();
			int y = pion.getCasePlateau().getCoordonneeY();
			Case caseVideDroite = null;
			Case caseVideGauche = null;
			Case caseVideHaut = null;
			Case caseVideBas = null;

			double groupeGauche = 0;
			double groupeDroite = 0;
			double groupeHaut = 0;
			double groupeBas = 0;

			boolean estMenace = false;
			
			//vers la droite
			for(int i = pion.getCasePlateau().getCoordonneeX()+1; i < App.regles.getNbDeCaseParLigne(); i++){
				if(plateau[i][y].getPion() != null && plateau[i][y].getPion().getNumeroJoueur() != pion.getNumeroJoueur()){
					estMenace = true;
					groupeDroite++;
				}else if(plateau[i][y].getPion() == null && estMenace){
					caseVideDroite = plateau[i][y];
					break;
				}else{
					break;
				}
			}
			
			estMenace = false;
			//vers la gauche
			for(int i = pion.getCasePlateau().getCoordonneeX()-1; i >= 0; i--){
				if(plateau[i][y].getPion() != null && plateau[i][y].getPion().getNumeroJoueur() != pion.getNumeroJoueur()){
					estMenace = true;
					groupeGauche++;
				}else if(plateau[i][y].getPion() == null && estMenace){
					caseVideGauche = plateau[i][y];
					break;
				}else{
					break;
				}		
			}
			
			estMenace = false;
			//vers le haut
			for(int j = pion.getCasePlateau().getCoordonneeY()-1; j >= 0; j--){
				if(plateau[x][j].getPion() != null && plateau[x][j].getPion().getNumeroJoueur() != pion.getNumeroJoueur()){
					estMenace = true;
					groupeHaut++;
				}else if(plateau[x][j].getPion() == null && estMenace){
					caseVideHaut = plateau[x][j];
					break;
				}else{
					break;
				}
			}
			
			estMenace = false;
			//vers le bas
			for(int j = pion.getCasePlateau().getCoordonneeX()+1; j < App.regles.getNbDeCaseParLigne(); j++){
				if(plateau[x][j].getPion() != null && plateau[x][j].getPion().getNumeroJoueur() != pion.getNumeroJoueur()){
					estMenace = true;
					groupeBas++;
				}else if(plateau[x][j].getPion() == null && estMenace){
					caseVideBas = plateau[x][j];
					break;
				}else{
					break;
				}
			}

			//test si le pion est vraiment en position d'�chec en v�rifiant que la case vide � c�t� peut �tre atteinte directement par un pion du joueur 2
			if(caseVideGauche != null && check(plateau, caseVideGauche, pion.getNumeroJoueur())){
				menaceJoueur2 += groupeGauche;
			}
			if(caseVideDroite != null && check(plateau, caseVideDroite, pion.getNumeroJoueur())){
				menaceJoueur2 += groupeDroite;
			}
			if(caseVideHaut != null && check(plateau, caseVideHaut, pion.getNumeroJoueur())){
				menaceJoueur2 += groupeHaut;
			}
			if(caseVideBas != null && check(plateau, caseVideBas, pion.getNumeroJoueur())){
				menaceJoueur2 += groupeBas;
			}
		}
		
		// cas sp�cial des coins
		int indiceMax = App.regles.getNbDeCaseParLigne() - 1;
		//en haut � gauche
		if(plateau[0][0].getPion() != null){
			if(plateau[0][1].getPion() != null && plateau[1][0].getPion() == null && plateau[0][1].getPion().getNumeroJoueur() != plateau[0][0].getPion().getNumeroJoueur()){
				if (check(plateau, plateau[1][0], plateau[0][0].getPion().getNumeroJoueur())){
					// si le pion en danger appartient au joueur 1, la menace du joueur 2 augmente
					if(plateau[0][0].getPion().getNumeroJoueur() == pionsJoueur1.get(0).getNumeroJoueur()){
						menaceJoueur2++;
					}else{
						menaceJoueur1++;
					}
				}
			}
			else if(plateau[1][0].getPion() != null && plateau[0][1].getPion() == null && plateau[1][0].getPion().getNumeroJoueur() != plateau[0][0].getPion().getNumeroJoueur()){
				if (check(plateau, plateau[0][1], plateau[0][0].getPion().getNumeroJoueur())){
					// si le pion en danger appartient au joueur 1, la menace du joueur 2 augmente
					if(plateau[0][0].getPion().getNumeroJoueur() == pionsJoueur1.get(0).getNumeroJoueur()){
						menaceJoueur2++;
					}else{
						menaceJoueur1++;
					}
				}
			}
		}
		//en bas � gauche
		if(plateau[0][indiceMax].getPion() != null){
			if(plateau[0][indiceMax-1].getPion() != null && plateau[1][indiceMax].getPion() == null && plateau[0][indiceMax-1].getPion().getNumeroJoueur() != plateau[0][indiceMax].getPion().getNumeroJoueur()){
				if (check(plateau, plateau[1][indiceMax], plateau[0][indiceMax].getPion().getNumeroJoueur())){
					// si le pion en danger appartient au joueur 1, la menace du joueur 2 augmente
					if(plateau[0][indiceMax].getPion().getNumeroJoueur() == pionsJoueur1.get(0).getNumeroJoueur()){
						menaceJoueur2++;
					}else{
						menaceJoueur1++;
					}
				}
			}
			else if(plateau[1][indiceMax].getPion() != null && plateau[0][indiceMax-1].getPion() == null && plateau[1][indiceMax].getPion().getNumeroJoueur() != plateau[0][indiceMax].getPion().getNumeroJoueur()){
				if (check(plateau, plateau[0][indiceMax-1], plateau[0][indiceMax].getPion().getNumeroJoueur())){
					// si le pion en danger appartient au joueur 1, la menace du joueur 2 augmente
					if(plateau[0][indiceMax].getPion().getNumeroJoueur() == pionsJoueur1.get(0).getNumeroJoueur()){
						menaceJoueur2++;
					}else{
						menaceJoueur1++;
					}
				}
			}
		}
		//en haut � droite
		if(plateau[indiceMax][0].getPion() != null){
			if(plateau[indiceMax][1].getPion() != null && plateau[indiceMax- 1][0].getPion() == null && plateau[indiceMax][1].getPion().getNumeroJoueur() != plateau[indiceMax][0].getPion().getNumeroJoueur()){
				if (check(plateau, plateau[indiceMax-1][0], plateau[indiceMax][0].getPion().getNumeroJoueur())){
					// si le pion en danger appartient au joueur 1, la menace du joueur 2 augmente
					if(plateau[indiceMax][0].getPion().getNumeroJoueur() == pionsJoueur1.get(0).getNumeroJoueur()){
						menaceJoueur2++;
					}else{
						menaceJoueur1++;
					}
				}
			}
			else if(plateau[indiceMax - 1][0].getPion() != null && plateau[indiceMax][1].getPion() == null && plateau[indiceMax-1][0].getPion().getNumeroJoueur() != plateau[indiceMax][0].getPion().getNumeroJoueur()){
				if (check(plateau, plateau[indiceMax][1], plateau[indiceMax][0].getPion().getNumeroJoueur())){
					// si le pion en danger appartient au joueur 1, la menace du joueur 2 augmente
					if(plateau[indiceMax][0].getPion().getNumeroJoueur() == pionsJoueur1.get(0).getNumeroJoueur()){
						menaceJoueur2++;
					}else{
						menaceJoueur1++;
					}
				}
			}
		}
		//en bas � droite
		if(plateau[indiceMax][indiceMax].getPion() != null){
			if(plateau[indiceMax][indiceMax-1].getPion() != null && plateau[indiceMax- 1][indiceMax].getPion() == null && plateau[indiceMax][indiceMax-1].getPion().getNumeroJoueur() != plateau[indiceMax][indiceMax].getPion().getNumeroJoueur()){
				if (check(plateau, plateau[indiceMax-1][indiceMax], plateau[indiceMax][indiceMax].getPion().getNumeroJoueur())){
					// si le pion en danger appartient au joueur 1, la menace du joueur 2 augmente
					if(plateau[indiceMax][indiceMax].getPion().getNumeroJoueur() == pionsJoueur1.get(0).getNumeroJoueur()){
						menaceJoueur2++;
					}else{
						menaceJoueur1++;
					}
				}
			}
			else if(plateau[indiceMax - 1][indiceMax].getPion() != null && plateau[indiceMax][indiceMax-1].getPion() == null && plateau[indiceMax-1][indiceMax].getPion().getNumeroJoueur() != plateau[indiceMax][indiceMax].getPion().getNumeroJoueur()){
				if (check(plateau, plateau[indiceMax][indiceMax-1], plateau[indiceMax][indiceMax].getPion().getNumeroJoueur())){
					// si le pion en danger appartient au joueur 1, la menace du joueur 2 augmente
					if(plateau[indiceMax][indiceMax].getPion().getNumeroJoueur() == pionsJoueur1.get(0).getNumeroJoueur()){
						menaceJoueur2++;
					}else{
						menaceJoueur1++;
					}
				}
			}
		}
		
		return menaceJoueur1 - menaceJoueur2;
	}
	
	
	/**
	 * Permet de savoir si un pion appartenant au joueur numeroJoueur peut aller sur la case caseVide
	 * @param plateau le plateau de jeu
	 * @param caseVide la case � consid�rer
	 * @param numeroJoueur le num�ro du joueur � consid�rer
	 * @return vrai si le joueur peut d�placer un de ses pions sur la case faux sinon
	 */
	private static boolean check(Case[][] plateau, Case caseVide, int numeroJoueur){
		int x = caseVide.getCoordonneeX();
		int y = caseVide.getCoordonneeY();
		//vers la droite
		for(int i = x+1; i < App.regles.getNbDeCaseParLigne(); i++){
			if(plateau[i][y].getPion() != null){
				//si le pion appartient � l'adversaire alors la menace est r�elle
				if(plateau[i][y].getPion().getNumeroJoueur() != numeroJoueur){
					return true;
				}else{
					break;
				}
			}
		}
		//vers la gauche
		for(int i = x-1; i >= 0; i--){
			if(plateau[i][y].getPion() != null){
				//si le pion appartient � l'adversaire alors la menace est r�elle
				if(plateau[i][y].getPion().getNumeroJoueur() != numeroJoueur){
					return true;
				}else{
					break;
				}
			}
		}
		//vers le haut
		for(int j = y+1; j < App.regles.getNbEcartAvantDefaite(); j++){
			if(plateau[x][j].getPion() != null){
				//si le pion appartient � l'adversaire alors la menace est r�elle
				if(plateau[x][j].getPion().getNumeroJoueur() != numeroJoueur){
					return true;
				}else{
					break;
				}
			}
		}
		// vers le bas
		for(int j = y-1; j >= 0; j--){
			if(plateau[x][j].getPion() != null){
				//si le pion appartient � l'adversaire alors la menace est r�elle
				if(plateau[x][j].getPion().getNumeroJoueur() != numeroJoueur){
					return true;
				}else{
					break;
				}
			}
		}
		
		return false;
	}

	/**
	 * renvoie le nombre de d�placement possible pour le pion
	 * @param pion le pion qui veut se d�placer
	 * @param cases le plateau sur lequel il faut calculer les d�placements possibles
	 * @return un entier repr�sentant le nombre de d�placements possibles pour le pion sur le plateau
	 */
	public static int getNumberOfPossibleMoves(Pion pion, Case[][] cases){
		//r�cup�re les coodronn�es du pion
		int pionX = pion.getCasePlateau().getCoordonneeX();
		int pionY = pion.getCasePlateau().getCoordonneeY();
		int compteur = 0;


		//du pion vers la droite
		for(int i = pionX + 1; i < App.regles.getNbDeCaseParLigne(); i++){
			//si la case est libre
			if(cases[i][pionY].getPion() == null){
				compteur++;
			}else{
				//� partir de l� il n'y a plus de d�placements valides
				break;
			}
		}


		//du pion vers la gauche
		for(int i = pionX - 1; i >= 0; i--){
			if(cases[i][pionY].getPion() == null){
				compteur++;
			}else{
				//� partir de l� il n'y a plus de d�placements valides
				break;
			}
		}

		//du pion vers le haut
		for(int i = pionY - 1;i >= 0 ; i--){
			if(cases[pionX][i].getPion() == null){
				compteur++;
			}else{
				//� partir de l� il n'y a plus de d�placements valides
				break;
			}
		}

		//du pion vers le bas
		for(int i = pionY + 1;i < App.regles.getNbDeCaseParLigne(); i++){
			if(cases[pionX][i].getPion() == null){
				compteur++;
			}else{
				//� partir de l� il n'y a plus de d�placements valides
				break;
			}
		}

		return compteur;
	}
	
	
	public static class Setup{
		private double coef1;
		private double coef2;
		private double coef3;
		
		private boolean isCoef1;
		private boolean isCoef2;
		private boolean isCoef3;
		
		private boolean isRandomisedAfter = false;
		
		public Setup(boolean isCoef1, double coef1,boolean isCoef2, double coef2,boolean isCoef3, double coef3, boolean isRandomisedAfter){
			this.coef1 = coef1;
			this.coef2 = coef2;
			this.coef3 = coef3;
			
			this.isCoef1 = isCoef1;
			this.isCoef2 = isCoef2;
			this.isCoef3 = isCoef3;
			
			this.isRandomisedAfter = isRandomisedAfter;
		}

		public double getCoef1() {
			return coef1;
		}

		public double getCoef2() {
			return coef2;
		}

		public double getCoef3() {
			return coef3;
		}

		public boolean isCoef1() {
			return isCoef1;
		}

		public boolean isCoef2() {
			return isCoef2;
		}

		public boolean isCoef3() {
			return isCoef3;
		}
		
		public boolean isRandomisedAfter(){
			return isRandomisedAfter;
		}
		
		
	}
}




//possibilit�s

//lin�aire
//score = alpha1 * contrainte1 + alpha2 * contrainte2 

//logistique
//score  = 1 / (1 + exp^(alpha1 * contrainte1 + alpha2 * contrainte2))

//polynomiale
//score = alpha1 * contrainte1^puissance1 + alpha2 * contrainte2^puissance2
