package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Case extends Elements{
	private Pion pion;
	private Rectangle rectangle;
	private boolean highlighted = false;
	private int coordonneeX;
	private int coordonneeY;

	public Case(Pion pion){
		super(0, 0);
		this.pion = pion;
	}
	
	
	public void init(int largeur, int x, int y){
		this.rectangle = new Rectangle(0, 0, largeur, largeur);
		this.rectangle.setFill(Color.TRANSPARENT);
		this.rectangle.setStroke(Color.BLACK);
		this.coordonneeX = x;
		this.coordonneeY = y;
	}


	//getters et setters
	public Pion getPion() {
		return pion;
	}

	public void setPion(Pion pion) {
		this.pion = pion;
	}
	
	/**
	 * 
	 * @param couleur la couleur avec laquelle on souhaite colorier la case
	 * Permet de recolorier la case.
	 */
	private void setCouleur(Color couleur){
		getRectangle().setFill(couleur);
	}
	
	public void putInhighlight(){
		setCouleur(Color.rgb(178,22,0,0.5));
		this.highlighted = true;
	}

	public void resetHighlight(){
		setCouleur(Color.TRANSPARENT);
		this.highlighted = false;
	}
	
	public void setRectangle(Rectangle rectangle){
		this.rectangle = rectangle;
	}
	
	public Rectangle getRectangle(){
		return this.rectangle;
	}
	
	public boolean isHighlighted(){
		return highlighted;
	}
	
	public int getCoordonneeX(){
		return coordonneeX;
	}
	
	public void setCoordonneeX(int x){
		this.coordonneeX = x;
	}
	
	public int getCoordonneeY(){
		return coordonneeY;
	}
	
	public void setCoordonneeY(int y){
		this.coordonneeY = y;
	}
	
}
