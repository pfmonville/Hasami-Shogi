package view;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class View {
	private Pane panel;
	
	//constructeurs
	public View(int x, int y){
		//initialisation du panel
		this.panel = new Pane();
		this.panel.setPrefWidth(x);
		this.panel.setPrefHeight(y);
	}
	
	//getters et setters
	public Pane getPanel(){
		return this.panel;
	}
	
	//méthodes
	
	/**
	 * 
	 * @param boutons liste des boutons que l'on souhaite mettre en page
	 * @param nbColonnes nombre de colonnes souhaitées
	 * @param hauteurBoutons hauteur des boutons souhaités
	 * @param margeHauteur nombre de pixels séparant les boutons du sommet du parent
	 * @param margeCote nombre de pixels séparant les boutons des marges à  gauche et à  droite - permet de fixer la largeur des boutons
	 * 
	 * Méthode permettant de créer une mise en page rapide pour une liste de boutons
	 */
	public void menuRapide(ArrayList<Button> boutons, int nbColonnes, int hauteurBoutons, int margeHauteur, int margeCote){
		int indexBoutons = 0;
		int espaceEntreBoutons = 5;
		
		//parcours du tableau
		while(indexBoutons < boutons.size()){
			
			//hauteur des boutons 
			boutons.get(indexBoutons).setMinHeight(hauteurBoutons);
			
			//largeur des boutons fixée
			int largeurBoutons = (int)(getPanel().getPrefWidth()) - margeCote * 2;
			boutons.get(indexBoutons).setMinWidth((largeurBoutons / nbColonnes) - 2);
						
			//déplacement des boutons à  l'endroit voulu
			boutons.get(indexBoutons).setTranslateX(margeCote + ((largeurBoutons / nbColonnes) - 2) * (indexBoutons % nbColonnes));
			boutons.get(indexBoutons).setTranslateY(margeHauteur + hauteurBoutons * (indexBoutons / nbColonnes) + ((int)(indexBoutons / nbColonnes) * espaceEntreBoutons));
			indexBoutons++;
			
		}
	}
	
	/**
	 * 
	 * @param texte le texte que l'on souhaite mettre en page
	 * @param largeurTexte la largeur du texte souhaitée
	 * @param hauteur les coordonnées Y souhaitées
	 * 
	 * Permet de mettre en page et de centrer automatiquement un texte
	 */
	public void miseEnPageTexteCentre(Text texte, int largeurTexte, int hauteur){
		texte.setWrappingWidth(largeurTexte);
		texte.setTranslateY(hauteur);
	}
}