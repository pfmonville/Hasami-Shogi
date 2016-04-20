package model;

public class Theme {

	private String imageJoueurNoir;
	private String imageJoueurBlanc;
	private int tailleCase;
	
	public Theme(){
		this.imageJoueurNoir = "image/gogui-black-32x32.png";
		this.imageJoueurBlanc = "image/gogui-white-32x32.png";
		this.tailleCase = 49;
	}

	public String getImageJoueurNoir() {
		return imageJoueurNoir;
	}

	public String getImageJoueurBlanc() {
		return imageJoueurBlanc;
	}

	public int getTailleCase() {
		return tailleCase;
	}
}
