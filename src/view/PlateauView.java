package view;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import mainPackage.App;
import model.Case;
import model.Pion;

public class PlateauView extends View{

	@FXML
	private Pane panel;
	private GridPane gridPanel;
	private Text pionsPerdusJ1, pionsPerdusJ2, aQuiLeTour;
	private int tailleGrille;
	private int tailleCase;
	private Image imageJoueur;
	private ImageView imageAQuiLeTour, waitingCursor;
	
	private Case cases[][];
	
	@SuppressWarnings("unused")
	private ArrayList<Pion> pionsBlancs;
	@SuppressWarnings("unused")
	private ArrayList<Pion> pionsNoirs;
	private boolean tourDeNoir;
	private boolean mouseListenerIsActive;
	
	
	public PlateauView(int x, int y){
		super(x, y);
	}
	

	
	/**
	 * lancement de PlateauView par un controller et initialisation de la vue
	 * @param cases la liste des cases du plateau
	 * @param pionsBlancs: liste des pions blancs
	 * @param pionsNoirs: liste des pions noirs
	 */
	public void begin(Case cases[][], ArrayList<Pion> pionsBlancs, ArrayList<Pion> pionsNoirs){
		this.cases = cases;
		this.pionsBlancs = pionsBlancs;
		this.pionsNoirs = pionsNoirs;
		
		tourDeNoir = true;
		mouseListenerIsActive = true;
		
		//***************************************************************//
		//DEFINITION DE LA GRILLE
		
		ImageView wood = new ImageView(new Image("image/wood.png"));
		
		//initialisation des valeurs
		gridPanel = new GridPane();
		tailleGrille = App.regles.getNbDeCaseParLigne();
		tailleCase = 49;
		
		//definition du fond
		wood.setFitHeight(tailleGrille * (tailleCase + 1));
		wood.setFitWidth(tailleGrille * (tailleCase + 1));
		wood.setTranslateX((super.getPanel().getPrefWidth() - (tailleGrille*tailleCase))/2);
		wood.setTranslateY((super.getPanel().getPrefHeight() - (tailleGrille*tailleCase))/2);
		
		//ajout du fond
		super.getPanel().getChildren().add(wood);
		
		//dessin des cases
		for(int i=0;i<tailleGrille;i++){
			for(int j=0;j<tailleGrille;j++){
				gridPanel.add(this.cases[i][j].getRectangle(), i, j);
			}
		}
		
		//gridPanel centré
		gridPanel.setTranslateX((super.getPanel().getPrefWidth() - (tailleGrille*tailleCase))/2);
		gridPanel.setTranslateY((super.getPanel().getPrefHeight() - (tailleGrille*tailleCase))/2);
		
		//***************************************************************//
		
		
		
		
		
		
		//***************************************************************//
		//DEFINITION DES PIONS
		
		//affichage des pions
		for(int i=0;i<tailleGrille;i++){
			gridPanel.add(pionsBlancs.get(i).getPion(), i, 0);
		}
		
		for(int i=0;i<tailleGrille;i++){
			gridPanel.add(pionsNoirs.get(i).getPion(), i, tailleGrille - 1);
		}
		
		//***************************************************************//
		
		
		
		
		
		
		
		
		//***************************************************************//
		//INDIQUER STATISTIQUES DE LA PARTIE EN COURS
		
		int tailleImageJoueur = 24;
		int decalageX = 35;
		int decalageY = 18;
		
		//Joueur 1
		ImageView joueur1 = new ImageView(new Image(App.theme.getImageJoueurNoir()));
		
		joueur1.setFitHeight(tailleImageJoueur);
		joueur1.setFitWidth(tailleImageJoueur);
		joueur1.setTranslateX(75);
		joueur1.setTranslateY(540);
		
		pionsPerdusJ1 = new Text(tailleGrille + "/" + tailleGrille);
		pionsPerdusJ1.setTranslateX(joueur1.getTranslateX() + decalageX);
		pionsPerdusJ1.setTranslateY(joueur1.getTranslateY() + decalageY);
		pionsPerdusJ1.setId("stat");
		
		//Joueur 2
		ImageView joueur2 = new ImageView(new Image(App.theme.getImageJoueurBlanc()));
		
		joueur2.setFitHeight(tailleImageJoueur);
		joueur2.setFitWidth(tailleImageJoueur);
		joueur2.setTranslateX(470);
		joueur2.setTranslateY(540);
		
		pionsPerdusJ2 = new Text(tailleGrille + "/" + tailleGrille);
		pionsPerdusJ2.setTranslateX(joueur2.getTranslateX() + decalageX);
		pionsPerdusJ2.setTranslateY(joueur2.getTranslateY() + decalageY);
		pionsPerdusJ2.setId("stat");
		
		//***************************************************************//
		
		
		
		
		
		
		
		//***************************************************************//
		//INDIQUER QUI DOIT JOUER
		
		//image du joueur qui doit jouer
		imageJoueur = new Image(App.theme.getImageJoueurNoir());
		this.imageAQuiLeTour = new ImageView(imageJoueur);
		this.imageAQuiLeTour.setTranslateX(415);
		this.imageAQuiLeTour.setTranslateY(25);
		
		//texte
		aQuiLeTour = new Text("C'est au tour de : ");
		aQuiLeTour.setId("titrePlateau");
		aQuiLeTour.setId("titrePlateau");
		super.miseEnPageTexteCentre(aQuiLeTour, (int)super.getPanel().getPrefWidth(), 50);
		super.getPanel().getChildren().add(imageAQuiLeTour);
		
		
		
		//***************************************************************//
		
		
		
		
		
		
		
		
		
		
		//***************************************************************//
		//GESTION DES EVENEMENTS
		
		gridPanel.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		    	if (mouseListenerIsActive){
		    		int column = (int)(mouseEvent.getX() / 50);
			    	int line = (int)(mouseEvent.getY() / 50);
			    	App.gameController.getPlateauController().isClicking(cases[column][line]);
		    	}
		    }
		});
		//***************************************************************//
		
		
		
		
		
		//***************************************************************//
		//CREATION DU BOUTON POUR RETOURNER AU MENU
		Button retourAuMenu = new Button("Retour au menu");
		retourAuMenu.setOnAction((event)->{
			try {
				App.reset();
				App.changementFenetre(super.getPanel(), App.sv.getPanel());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		super.menuRapide(new ArrayList<Button>(Arrays.asList(retourAuMenu)), 1, 40, 550, 200);
		
		
		//***************************************************************//
		
		
		
		
		
		
		//***************************************************************//
		//GIF POUR L'ATTENTE
		
		waitingCursor = new ImageView();
		Image cursor = new Image("image/waiting_cursor.gif");
		waitingCursor.setImage(cursor);
		waitingCursor.setLayoutX(150);
		waitingCursor.setLayoutY(23);
        
        
        
      //***************************************************************//
		
		
		
		
		
		//***************************************************************//
		//AJOUT DES ELEMENTS A LA VIEW
		
		super.getPanel().getChildren().add(gridPanel);		
		super.getPanel().getChildren().add(aQuiLeTour);
		super.getPanel().getChildren().add(pionsPerdusJ1);
		super.getPanel().getChildren().add(joueur1);
		super.getPanel().getChildren().add(joueur2);
		super.getPanel().getChildren().add(pionsPerdusJ2);
		super.getPanel().getChildren().add(retourAuMenu);
		
		//***************************************************************//
	}
	
	@Override
	public Pane getPanel(){
		return super.getPanel();
	}
	
	
	public void switchImageJoueur(int nextJoueur){
		if(nextJoueur == App.regles.getNumeroJoueurNoir()){
			changeImage(new Image(App.theme.getImageJoueurNoir()));
		}else{
			changeImage(new Image(App.theme.getImageJoueurBlanc()));
		}
	}
	
	/**
	 * Permet de changer l'image du joueur en cours
	 * @param image url de l'image à changer
	 */
	private void changeImage(Image image){
		imageJoueur = image;
		ImageView nouvelleImage = new ImageView(image);
		setImageAQuiLeTour(nouvelleImage); 
	}

	/**
	 * Permet de mettre à jour l'attribut ImageAQuiLeTour
	 * @param image nouvelle ImageView
	 */
	private void setImageAQuiLeTour(ImageView image){
		getPanel().getChildren().remove(imageAQuiLeTour);
		this.imageAQuiLeTour = image;
		this.imageAQuiLeTour.setTranslateX(415);
		this.imageAQuiLeTour.setTranslateY(25);
		getPanel().getChildren().add(imageAQuiLeTour);
	}
	
	/**
	 * Permet d'afficher quel joueur doit jouer
	 */
	public void changementJoueur(){
		if(tourDeNoir){
			changeImage(new Image(App.theme.getImageJoueurBlanc()));
		}
		else{
			changeImage(new Image(App.theme.getImageJoueurNoir()));
		}
		tourDeNoir = !tourDeNoir;
	}
	
	public void suppressionPion(Pion pion){
		gridPanel.getChildren().remove(pion.getPion());
	}
	
	public void deplacerPion(Pion pion, int i, int j){
		suppressionPion(pion);
		gridPanel.add(pion.getPion(), i, j);
	}
	
	public void makePionSelected(boolean isSelected, Pion pion){
			pion.setCircle(isSelected);
		
	}
	
	public void stopMouseListener(){
		this.mouseListenerIsActive = false;
	}
	
	public void startMouseListener(){
		this.mouseListenerIsActive = true;
	}
	
	public void setTexteJ1(int nbPions){
		this.pionsPerdusJ1.setText(nbPions + "/" + tailleGrille);
	}
	
	public void setTexteJ2(int nbPions){
		this.pionsPerdusJ2.setText(nbPions + "/" + tailleGrille);
	}
	
	public void setWinnerTextInTopBanner(int joueur){
		//régler le texte
		this.aQuiLeTour.setText("a gagn� !");
		
		//régler l'icone du pion
		Image imageGagnant;
		if(joueur == App.regles.getNumeroJoueurNoir()){
			imageGagnant = new Image(App.theme.getImageJoueurNoir());
		}else{
			imageGagnant = new Image(App.theme.getImageJoueurBlanc());
		}
		changeImage(imageGagnant);
		this.imageAQuiLeTour.setTranslateX(200);
	}
	
	public void printWaitingCursor(){
		if(!getPanel().getChildren().contains(getCursor())){
			super.getPanel().getChildren().add(waitingCursor);
		}
	}
	
	public void removeWaitingCursor(){
		super.getPanel().getChildren().remove(waitingCursor);
	}
	
	public void putCursorInWait(){
		App.scene.setCursor(Cursor.WAIT);
	}
	
	public void resetCursor(){
		App.scene.setCursor(App.oldCursor);
	}
	
	public ImageView getCursor(){
		return waitingCursor;
	}
}
