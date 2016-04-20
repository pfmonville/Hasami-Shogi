package model;

import java.util.ArrayList;

public class Regles {
	private boolean capturerDiagonale;
	private boolean capturerCoins;
	private int nbPiecesAvantDefaite;
	private boolean ecartAvantDefaite;
	private int nbEcartAvantDefaite;
	private int nbDeCaseParLigne;
	private int numeroJoueurNoir;
	private int joueurBlanc;
	
	private ArrayList<String> sounds = new ArrayList<>();

	
	public Regles(){
		this.capturerDiagonale = false;
		this.capturerCoins = false;
		this.nbPiecesAvantDefaite = 2;
		this.ecartAvantDefaite = true;
		this.nbEcartAvantDefaite = 3;
		this.nbDeCaseParLigne = 9;
		this.numeroJoueurNoir = 0;
		this.joueurBlanc = 1;
		sounds.add("sound/wood-on-wood-1.aiff");
		sounds.add("sound/wood-on-wood-2.aiff");
	}
	
	public Regles(boolean capturerDiagonale, boolean capturerCoins, int nbPiecesAvantDefaite, boolean ecartAvantDefaite, int nbEcartAvantDefaite){
		this.capturerCoins = capturerCoins;
		this.capturerDiagonale = capturerDiagonale;
		this.nbPiecesAvantDefaite = nbPiecesAvantDefaite;
		this.ecartAvantDefaite = ecartAvantDefaite;
		this.nbEcartAvantDefaite = nbEcartAvantDefaite;
	}

	public boolean isCapturerDiagonale() {
		return capturerDiagonale;
	}

	public void setCapturerDiagonale(boolean capturerDiagonale) {
		this.capturerDiagonale = capturerDiagonale;
	}

	public boolean isCapturerCoins() {
		return capturerCoins;
	}

	public void setCapturerCoins(boolean capturerCoins) {
		this.capturerCoins = capturerCoins;
	}

	public int getNbPiecesAvantDefaite() {
		return nbPiecesAvantDefaite;
	}

	public void setNbPiecesAvantDefaite(int nbPiecesAvantDefaite) {
		this.nbPiecesAvantDefaite = nbPiecesAvantDefaite;
	}

	public boolean isEcartAvantDefaite() {
		return ecartAvantDefaite;
	}

	public void setEcartAvantDefaite(boolean ecartAvantDefaite) {
		this.ecartAvantDefaite = ecartAvantDefaite;
	}

	public int getNbEcartAvantDefaite() {
		return nbEcartAvantDefaite;
	}

	public void setNbEcartAvantDefaite(int nbEcartAvantDefaite) {
		this.nbEcartAvantDefaite = nbEcartAvantDefaite;
	}

	public int getNbDeCaseParLigne() {
		return nbDeCaseParLigne;
	}

	public void setNbDeCaseParLigne(int nbDeCaseParLigne) {
		this.nbDeCaseParLigne = nbDeCaseParLigne;
	}

	public int getNumeroJoueurNoir() {
		return numeroJoueurNoir;
	}

	public int getNumeroJoueurBlanc() {
		return joueurBlanc;
	}
	
	public int getMaxDeplacement(){
		return nbDeCaseParLigne * (nbDeCaseParLigne - 1);
	}
	
	public String getASound(){
		return sounds.get((int) Math.round(Math.random()));
	}
	
	
}
