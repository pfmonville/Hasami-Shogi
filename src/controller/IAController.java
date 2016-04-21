package controller;

import java.util.ArrayList;
import java.util.List;

import controller.iaUtils.AlphaBeta;
import controller.iaUtils.EvaluatePosition;
import controller.iaUtils.MinMax;
import controller.iaUtils.NegaMax;
import javafx.application.Platform;
import mainPackage.App;
import model.Case;
import model.Joueur;
import model.Pion;

public class IAController implements PlayerController, Cloneable, Runnable{
	private Joueur IA;
	private ArrayList<Pion> pionsIA;
	private ArrayList<Pion> pionsAdversaire;
	private Pion pionToMove = null;
	private Case caseWhereToMove = null;
	private Case caseAlreadyPlayed1 = null;
	private Pion pionAlreadyPlayed1 = null;
	private Case caseAlreadyPlayed2 = null;
	private Pion pionAlreadyPlayed2 = null;
	
	public IAController(Joueur joueur){
		this.IA = joueur;
	}
	
	
	/**
	 * fonction principale de IAController qui calcul le meilleur coup possible et effectue le d�placement
	 */
	@Override
	public void playAMove(){
		
		//si l'ia peut bouger actuellement
		if(isAbleToMove(pionsIA)){		
			
			//initialisation de toutes les variables
			Object[] result;
			ScoreCoupsInitiaux sci = null;
			int niveau = IA.getNiveau();
			
			switch (niveau){
			
			//minimax profondeur 2 avec fonction d'�valuation non compl�te
			case 1:
				try {
					result = MinMax.minMaxDecision(checkBoost(2), pionsIA, pionsAdversaire, new EvaluatePosition.Setup(true, 1, false, 0, false, 0, false));
					pionToMove = (Pion)result[0];
					caseWhereToMove = (Case)result[1];
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				break;
				
			//minimax profondeur 3 avec fonction d'�valuation compl�te
			case 2:
				try {
					result = MinMax.minMaxDecision(checkBoost(3), pionsIA, pionsAdversaire, new EvaluatePosition.Setup(true, 40, true, 1, true, 4.5, false));
					pionToMove = (Pion)result[0];
					caseWhereToMove = (Case)result[1];
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				break;
				
			//alphabeta profondeur 3 avec fonction d'�valuation non compl�te
			case 3:
				sci = AlphaBeta.launchAlphaBeta(pionsIA, pionsAdversaire, checkBoost(3), getCasePreviouslyPlayed(), getPionPreviouslyPlayed(), this.IA, new EvaluatePosition.Setup(true, 1, false, 0, false, 0, false));
				pionToMove = sci.getPion();
				caseWhereToMove = sci.getCase();
				break;
				
			//minimax profondeur 4 avec fonction d'�valuation compl�te
			case 4:
				sci = AlphaBeta.launchAlphaBeta(pionsIA, pionsAdversaire, checkBoost(4), getCasePreviouslyPlayed(), getPionPreviouslyPlayed(), this.IA, new EvaluatePosition.Setup(true, 40, true, 1, true, 4.5, false));
				pionToMove = sci.getPion();
				caseWhereToMove = sci.getCase();
				break;
				
			//negaMax profondeur 4 avec fonction d'�valuation compl�te
			case 5:
				sci = NegaMax.launchNegaMax(pionsIA, pionsAdversaire, checkBoost(4), getCasePreviouslyPlayed(),getPionPreviouslyPlayed(), this.IA, new EvaluatePosition.Setup(true, 40, true, 1, true, 4.5, false));
				pionToMove = sci.getPion();
				caseWhereToMove = sci.getCase();
				break;
				
			//negamax profondeur 5 avec fonction d'�valuation compl�te
			case 6:
				sci = NegaMax.launchNegaMax(pionsIA, pionsAdversaire, checkBoost(5), getCasePreviouslyPlayed(),getPionPreviouslyPlayed(), this.IA, new EvaluatePosition.Setup(true, 40, true, 1, true, 4.5, false));
				pionToMove = sci.getPion();
				caseWhereToMove = sci.getCase();
				break;
			}
			
			setPreviouslyPlayed(caseWhereToMove, pionToMove);
			App.gameController.getPlateauController().deplacerPion(pionToMove, caseWhereToMove, true);
			ArrayList<Pion> pionsASupprimer = PlateauController.verifierCapture(pionToMove, PlateauController.getCases());
			App.gameController.getPlateauController().supprimerPion(pionsASupprimer);
			
		}
		App.gameController.finTour();	
	}
	
	/**
	 * 
	 * @param base la profondeur de base (int)
	 * @return la profondeur sous forme d'entier augment�e de 1 si il y assez peu de pions sur le terrain permettant un calcul plus rapide
	 */
	public int checkBoost(int base){
		if(pionsIA.size() + pionsAdversaire.size() <= 8){
			base++;
		}
		return base;
	}
	
	/**
	 * Permet de d�placer un pion sans alt�rer la partie graphique
	 * @param pion le pion � d�placer
	 * @param caseFinale la case d'arriv�e
	 */
	public static void deplacerPion(Pion pion, Case caseFinale){
		//d'abord on supprime la reference du pion dans l'ancienne case
		pion.getCasePlateau().setPion(null);
		//on met � jour le pion
		pion.setCasePlateau(caseFinale);
		//on met � jour la nouvelle case
		caseFinale.setPion(pion);
	}
	
	/**
	 * supprime la r�f�rence du pion � supprimer de la liste de pions fournie 
	 * @param pion le pion �supprimer 
	 * @param pions la liste contenant le pion
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Pion> supprimerPion(Pion pion, ArrayList<Pion> pions){
		pion.getCasePlateau().setPion(null);
		ArrayList<Pion> nouvelleListe = (ArrayList<Pion>) pions.clone();
		nouvelleListe.remove(pion);
		return nouvelleListe;
	}
	
	/**
	 * supprimer un groupe de pions de la liste des pions du joueur
	 * @param pionsASupprimer les pions � supprimer
	 * @param pions la liste de pions du joueur
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Pion> supprimerPion(ArrayList<Pion> pionsASupprimer, ArrayList<Pion> pions){
		ArrayList<Pion> nouvelleListe = (ArrayList<Pion>) pions.clone();
		for(Pion pion: pionsASupprimer){
			nouvelleListe = IAController.supprimerPion(pion, pions);
		}
		return nouvelleListe;
	}
	
	
	/**
	 * permet de remettre les pions supprim�s pr�cedemment 
	 * @param pionsARemettre la liste de pions � remettre
	 * @param pions la liste des pions du joueur
	 */
	public static void remettrePion(List<Pion> pionsARemettre, List<Pion> pions){
		for(Pion pion: pionsARemettre){
			pion.getCasePlateau().setPion(pion);
		}
	}
	

	/**
	 * r�cup�re le lien vers les pions de la partie
	 * @param pionNoirs
	 * @param pionBlancs
	 */
	public void init(ArrayList<Pion> pionNoirs, ArrayList<Pion> pionBlancs){
		if(IA.isPremier()){
			this.pionsIA = pionNoirs;
			this.pionsAdversaire = pionBlancs;
		}else{
			this.pionsIA = pionBlancs;
			this.pionsAdversaire = pionNoirs;
		}
	}
	
	
	/**
	 * 
	 * @return la variable Joueur repr�sentant ce controlleur
	 */
	public Joueur getInformation(){
		return this.IA;
	}
	
	
	/**
	 * permet de savoir si la partie est termin�e
	 * @param pionsIA les pions de l'ia appelant la fonction
	 * @param pionsAdversaire les pions de joueur adverse
	 * @return vrai si la partie est termin�e faux sinon
	 */
	public static boolean isGameOver(ArrayList<Pion> pionsIA, ArrayList<Pion> pionsAdversaire){
		//si l'un des joueurs � trop peu de pions
		if(pionsIA.size() <= App.regles.getNbPiecesAvantDefaite()){
			return true;
		}
		if(pionsAdversaire.size() <= App.regles.getNbPiecesAvantDefaite()){
			return true;
		}
		
		//si la r�gle sur l'�cart trop grand est selectionn�e
		if(App.regles.isEcartAvantDefaite()){
			if(Math.abs(pionsIA.size() - pionsAdversaire.size()) > App.regles.getNbEcartAvantDefaite()){
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * � appeler apr�s isGameOver(); permet de savoir lequel des deux joueurs � gagner
	 * @param pionsIA la liste des pions du joueur � tester
	 * @param pionsAdversaire la liste des pions de son adversaire
	 * @return vrai si le premier joueur � gagn�; faux s'il n'a pas gagn� (apr�s l'appel de isGameOver() un r�sultat de faux indique une victoire de l'autre joueur)
	 */
	public static boolean hasFirstPlayerWon(ArrayList<Pion> pionsIA, ArrayList<Pion> pionsAdversaire){
		//si le joueur adverse � trop peu de pions
		if(pionsAdversaire.size() <= App.regles.getNbPiecesAvantDefaite()){
			return true;
		}
		
		//si la r�gle sur l'�cart trop grand est selectionn�e
		if(App.regles.isEcartAvantDefaite()){
			if(pionsIA.size() - pionsAdversaire.size() > App.regles.getNbEcartAvantDefaite()){
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 
	 * @param pionsJoueurCourant
	 * @param pionsAdversaire
	 * @param plateau
	 * @return
	 */
	public static Case[][] initPlateau(ArrayList<Pion> pionsJoueurCourant, ArrayList<Pion> pionsAdversaire, Case[][] plateau){
		for(Pion pion:pionsJoueurCourant){
			//on met � jour la nouvelle case
			plateau[pion.getCasePlateau().getCoordonneeX()][pion.getCasePlateau().getCoordonneeY()].setPion(pion);

		}
		
		for(Pion pion:pionsAdversaire){
			//on met � jour la nouvelle case
			plateau[pion.getCasePlateau().getCoordonneeX()][pion.getCasePlateau().getCoordonneeY()].setPion(pion);
		}
		
		return plateau;
	}
	
	
	/**
	 * permet de savoir si le joueur peut se d�placer dans sa situation actuelle
	 * @param pionsIA 
	 * @return vrai si le joueur peut bouger faux sinon
	 */
	public static boolean isAbleToMove(ArrayList<Pion> pionsIA){
		boolean canMove = false;
		for(Pion pion:pionsIA){
			//estimation de tous les coups possibles d'un pion
			ArrayList<Case> deplacementPossibles = PlateauController.getPossibleMoves(pion);
			if(!deplacementPossibles.isEmpty()){
				canMove = true;
			}
		}
		return canMove;
	}
	
	
	/**
	 * mt � jour la case et le pion qui viennent d'�tre jou�s afin d'�viter les r�p�titions
	 * @param caseJustPlayed
	 * @param pionJustePlayed
	 */
	public void setPreviouslyPlayed(Case caseJustPlayed, Pion pionJustePlayed){
		caseAlreadyPlayed1 = caseAlreadyPlayed2;
		caseAlreadyPlayed2 = caseJustPlayed;
		pionAlreadyPlayed1 = pionAlreadyPlayed2;
		pionAlreadyPlayed2 = pionJustePlayed;
	}
	
	/**
	 * 
	 * @return la case sur laquelle il ne faudrait pas aller si c'est le m�me pion qui se d�place
	 */
	public Case getCasePreviouslyPlayed(){
		return caseAlreadyPlayed1;
	}
	
	/**
	 * 
	 * @return le pion qui ne doit pas se d�placer s'il se remet sur la m�me case
	 */
	public Pion getPionPreviouslyPlayed(){
		return pionAlreadyPlayed1;
	}
	
	/**
	 * fonction principale permettant de lancer l'ia et de g�rer les threads de calcul et graphique
	 */
	@Override
	public void run(){
		Platform.runLater(()->App.pv.printWaitingCursor());
		//App.pv.putCursorInWait();
		this.playAMove();
		Platform.runLater(()->App.pv.removeWaitingCursor());
		//App.pv.resetCursor();
	}
	
	
	public static class ScoreCoupsInitiaux{
		private double score;
		private Pion pion;
		private Case casePlateau;
		
		public ScoreCoupsInitiaux(Pion pion, Case casePlateau, Double score) {
			this.pion = pion;
			this.casePlateau = casePlateau;
			this.score = score;
		}
		
		public Pion getPion(){
			return pion;
		}
		
		public Case getCase(){
			return casePlateau;
		}
		
		public double getScore(){
			return score;
		}
		
		public void randomizeScore(){
			this.score += (Math.random()*0.01) - 0.005;
		}
	}
	
	
	public static class Deplacements{
		public Pion pion;
		public ArrayList<Case> cases;
		
		public Deplacements(Pion pion, ArrayList<Case>cases){
			this.pion = pion;
			this.cases = cases;
		}
		
		public Pion getPion(){
			return pion;
		}
		
		public ArrayList<Case> getCases(){
			return cases;
		}
	}
	
}
