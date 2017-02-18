package model;

import mainPackage.App;

public class Player {
	private boolean isPremier;
	private boolean isHumain;
	private int niveau;
	private int nbPions;
	private int numeroJoueur; 
	
	public Player(boolean isPremier, boolean isHumain, int niveau, int nbPions) {
		this.isPremier = isPremier;
		this.isHumain = isHumain;
		this.niveau = niveau;
		this.nbPions = nbPions;
		if(isPremier){
			numeroJoueur = App.regles.getNumeroJoueurNoir();
		}else{
			numeroJoueur = App.regles.getNumeroJoueurBlanc();
		}
	}

	//getters et setters
	public boolean isPremier() {
		return isPremier;
	}

	public void setPremier(boolean isPremier) {
		this.isPremier = isPremier;
	}

	public boolean isHumain() {
		return isHumain;
	}

	public void setHumain(boolean isHumain) {
		this.isHumain = isHumain;
	}

	public int getNiveau() {
		return niveau;
	}

	public void setNiveau(int niveau) {
		this.niveau = niveau;
	}

	public int getNbPions() {
		return nbPions;
	}

	public void setNbPions(int nbPions) {
		this.nbPions = nbPions;
	}
	
	public void loseOnePion(){
		this.nbPions--;
	}

	public int getNumeroJoueur() {
		return numeroJoueur;
	}
	
	public int getNumeroAdversaire(){
		return 1 - numeroJoueur;
	}

	public void setNumeroJoueur(int numeroJoueur) {
		this.numeroJoueur = numeroJoueur;
	}
	
}
