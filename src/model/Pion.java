package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pion extends Elements implements Comparable<Pion>, Cloneable{
	private CouleurPion couleur;
	private Case casePlateau;
	private int joueur;
	private ImageView pion;
	private Circle cercle;
	private Pane panel;
	private int index;

	public Pion(int joueur){
		super(0,0);
		this.joueur = joueur;
	}
	
	public Pion(CouleurPion couleur, Case casePlateau, int joueur, int index) {
		//****************************************************************************
		//INITIALISATION DES ATTRIBUTS DE LA CLASSE
		super(0, 0);
		this.couleur = couleur;
		this.casePlateau = casePlateau;
		this.joueur = joueur;
		this.panel = new Pane();
		this.index = index;
		
		//****************************************************************************
		
		
		
		
		
		//****************************************************************************
		//DEFINITION DU CERCLE PERMETTANT D'INDIQUER QUEL EST LE PION SELECTIONNE
		this.cercle = new Circle();
		this.cercle.setRadius(3.0f);
		this.cercle.setFill(Color.TRANSPARENT);
		this.cercle.setStroke(Color.TRANSPARENT);
		this.cercle.setTranslateX((casePlateau.getRectangle().getWidth())/2);
		this.cercle.setTranslateY((casePlateau.getRectangle().getWidth())/2);
				
		//****************************************************************************
		
		
		
		
		
		//****************************************************************************
		//PLACEMENT DU PION DANS LA CASE CORRESPONDANTE
		casePlateau.setPion(this);
		
		//****************************************************************************
		
		
		
		
		
		//****************************************************************************
		//INITIALISATION DE L'IMAGE
		String url;
		if(couleur == CouleurPion.BLANC){
			url = "image/gogui-white-32x32.png";
		}
		else{
			url = "image/gogui-black-32x32.png";
		}
		
		//DEFINITION DE L'IMAGE
		Image image = new Image(url);
		pion = new ImageView();
		pion.setImage(image);
		int largeur = 32;
		pion.setTranslateX((casePlateau.getRectangle().getWidth() - largeur)/2);
		pion.setTranslateY((casePlateau.getRectangle().getWidth() - largeur)/2);
		
		//****************************************************************************
		
		//****************************************************************************
		//AJOUT DES ELEMENTS AU PANEL DE LA CASE
		this.panel.getChildren().add(this.pion);
		this.panel.getChildren().add(this.cercle);
		
	}
	
	
	
	
	//****************************************************************************
	//GETTERS ET SETTERS
	
	public Circle getCercle() {
		return cercle;
	}

	public void setCercle(Circle cercle) {
		this.cercle = cercle;
	}

	public void setPion(ImageView pion) {
		this.pion = pion;
	}

	public CouleurPion getCouleur() {
		return couleur;
	}
	
	public void setCouleur(CouleurPion couleur) {
		this.couleur = couleur;
	}
	
	public Case getCasePlateau() {
		return casePlateau;
	}
	
	public void setCasePlateau(Case casePlateau) {
		this.casePlateau = casePlateau;
	}
	
	public int getNumeroJoueur() {
		return joueur;
	}
	
	public void setJoueur(int joueur) {
		this.joueur = joueur;
	}
	

	public Pane getPion(){
		return this.panel;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	//****************************************************************************
	
	
	
	
	
	//****************************************************************************
	//METHODES
	
	

	/**
	 * si affiche est à  true, alors affiche un cercle rouge 
	 * sinon affiche rien
	 * @param affiche boolean indiquant si le cercle doit être affiché ou non
	 */
	public void setCircle(boolean affiche){
		if(affiche)
			cercle.setFill(Color.BROWN);
		else
			cercle.setFill(Color.TRANSPARENT);
	}

	@Override
	public int compareTo(Pion arg0) {
		if(this.index > arg0.index){
			return 10;
		}else if(this.index < arg0.index){
			return -10;
		}else{
			return 0;
		}
	}
	
	@Override
	public Pion clone() throws CloneNotSupportedException{
		return (Pion)super.clone();
	}
	
	//****************************************************************************
	
}
