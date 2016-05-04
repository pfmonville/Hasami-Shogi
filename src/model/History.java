package model;

import java.util.ArrayList;
import java.util.Stack;

import mainPackage.App;

public class History {
	
	private Stack<State>statesByTurn;
	private Stack<State2>statesByTurn2;
	
	/**
	 * permet d'initialiser l'historique et de stocker l'état de la première position
	 * @param plateau le plateau de jeu (Case[][])
	 * @param pionsJoueur1 la liste des pions du joueur 1 (ArrayList)
	 * @param pionsJoueur2 la liste des pions du joueur 2 (ArrayList)
	 */
	public History(Case[][] plateau, ArrayList<Pion> pionsJoueur1, ArrayList<Pion> pionsJoueur2){
		this.statesByTurn = new Stack<>();
		this.statesByTurn.push(new State(pionsJoueur1, pionsJoueur2, App.regles.getNumeroJoueurNoir()));
		
		//this.statesByTurn2 = new Stack<>();
	}
	
	
//	//******************************************************
//	// FONCTIONS POUR RECUPERER LES ETATS A UN TOUR DONNE
//	
//	public Case[][] getPlateauAtTurn(int turn){
//		return statesByTurn.get(turn).getPlateau();
//	}
//	
//	public ArrayList<Pion> getPionsNoirsAtTurn(int turn){
//		return statesByTurn.get(turn).getPionsJoueur1();
//	}
//	
//	public ArrayList<Pion> getPionsBlancsAtTurn(int turn){
//		return statesByTurn.get(turn).getPionsJoueur2();
//	}
//	
//	public int getJoueurNumberAtTurn(int turn){
//		return statesByTurn.get(turn).getActualJoueurNumber();
//	}
//	
//	//*********************************************************
	
	
	
	//*******************************************************
	// FONCTIONS POUR RECUPERER LES ETATS DU TOUR PRECEDENT
	
	public ArrayList<Pion> getPionsNoirsAtLastTurn(){
		return statesByTurn.peek().getPionsJoueur1();
	}
	
	public ArrayList<Pion> getPionsBlancsAtLastTurn(){
		return statesByTurn.peek().getPionsJoueur2();
	}
	
	public int getJoueurNumberAtLastTurn(){
		return statesByTurn.peek().getActualJoueurNumber();
	}
	
	//**********************************************************
	
	
	
	/**
	 * ajoute l'état à la l'historique et augmente le nombre de tours de 1
	 * @param plateau
	 * @param pionsJoueur1
	 * @param pionsJoueur2
	 * @param actualJoueur
	 */
	public void addTurn(ArrayList<Pion> pionsJoueur1, ArrayList<Pion> pionsJoueur2, int actualJoueur){
		statesByTurn.push(new State(pionsJoueur1, pionsJoueur2, actualJoueur));
	}
	
	public void addTurn2(Pion pion, Case caseOrigine, ArrayList<Pion> pionsSupprimes){
		statesByTurn2.push(new State2(pion, caseOrigine, pionsSupprimes));
	}
//	/**
//	 * remet l'historique dans l'état où il était au moment tu tour turn
//	 * @param turn le numéro du tour où l'on veut revenir
//	 */
//	public void discardFromTurn(int turn){
//		for(int i = this.actualTurn - 1; i >= turn; i--){
//			statesByTurn.remove(i);
//		}
//		this.actualTurn = turn;
//	}
	
	
	/**
	 * diminue le nombre de tours de 1 et supprime l'état précédemment sauvegardé
	 */
	public void discardLastMove(Case[][] plateau, ArrayList<Pion> pionsJoueurNoir, ArrayList<Pion> pionsJoueurBlancs){
		statesByTurn.pop();
		
		//on réinitialise les cases du plateau
		for(Case[] caseColonne: plateau){
			for(Case casePlateau: caseColonne){
				casePlateau.setPion(null);
			}
		}
		pionsJoueurNoir.clear();
		pionsJoueurNoir.addAll(statesByTurn.peek().getPionsJoueur1());
		pionsJoueurBlancs.clear();
		pionsJoueurBlancs.addAll(statesByTurn.peek().getPionsJoueur2());
		
		//on leur remet les anciens pions
		for(Pion pion: statesByTurn.peek().getPionsJoueur1()){
			plateau[pion.getCasePlateau().getCoordonneeX()][pion.getCasePlateau().getCoordonneeY()].setPion(pion);
		}
		for(Pion pion: statesByTurn.peek().getPionsJoueur2()){
			plateau[pion.getCasePlateau().getCoordonneeX()][pion.getCasePlateau().getCoordonneeY()].setPion(pion);
		}
		
	}
	
	public void discardLastMove2(Case[][] plateau){
		statesByTurn2.pop();
		
	}
	
	
	
	
	
	private static class State{
		ArrayList<Pion> pionsNoirs;
		ArrayList<Pion> pionsBlancs;
		int actualJoueur;
		
		@SuppressWarnings("unchecked")
		private State(ArrayList<Pion> pionsJoueur1, ArrayList<Pion> pionsJoueur2, int actualJoueur) {
			this.pionsNoirs = (ArrayList<Pion>) pionsJoueur1.clone();
			this.pionsBlancs = (ArrayList<Pion>) pionsJoueur2.clone();
			//on initialise les clones entre eux pour ne pas avoir de références aux anciens objets
			//this.initClones(plateau, pionsJoueur1, pionsJoueur2);
			this.actualJoueur = actualJoueur;
		}
		
		private int getActualJoueurNumber(){
			return this.actualJoueur;
		}
	

		public ArrayList<Pion> getPionsJoueur1() {
			return pionsNoirs;
		}

		public ArrayList<Pion> getPionsJoueur2() {
			return pionsBlancs;
		}
		
		/**
		 * permet d'initialiser les clones de plateau et des listes de pions entre eux i.e. aucune référence aux objets parents
		 * @param pionsJoueur1
		 * @param pionsJoueur2
		 */
		private void initClones(Case[][] plateau, ArrayList<Pion> pionsJoueur1, ArrayList<Pion> pionsJoueur2){
			
			//on initialise le plateau à vide
			for(int i=0;i<App.regles.getNbDeCaseParLigne(); i++){
				for(int j=0;j<App.regles.getNbDeCaseParLigne();j++){
					plateau[i][j].setPion(null);
				}
			}
			
			//on ajoute les éléments de la liste 1
			for(Pion pion:pionsJoueur1){
				plateau[pion.getCasePlateau().getCoordonneeX()][pion.getCasePlateau().getCoordonneeY()].setPion(pion);
				pion.setCasePlateau(plateau[pion.getCasePlateau().getCoordonneeX()][pion.getCasePlateau().getCoordonneeY()]);
			}
			
			//on ajoute les éléments de la liste 2
			for(Pion pion:pionsJoueur2){
				plateau[pion.getCasePlateau().getCoordonneeX()][pion.getCasePlateau().getCoordonneeY()].setPion(pion);
				pion.setCasePlateau(plateau[pion.getCasePlateau().getCoordonneeX()][pion.getCasePlateau().getCoordonneeY()]);
			}
		}
		
		
	}
	
	private static class State2{
		Pion pion;
		Case caseOrigine;
		ArrayList<Pion> pionsSupprimes;
		
		public State2(Pion pion, Case caseOrigine, ArrayList<Pion> pionsSupprimes) {
			super();
			this.pion = pion;
			this.caseOrigine = caseOrigine;
			this.pionsSupprimes = pionsSupprimes;
		}

		public Pion getPion() {
			return pion;
		}

		public Case getCaseOrigine() {
			return caseOrigine;
		}

		public ArrayList<Pion> getPionsSupprimes() {
			return pionsSupprimes;
		}
		
		
	}
}
