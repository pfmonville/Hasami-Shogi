package view;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
	private boolean mouseListenerIsActive, couperSon;
	
	private Button retourAuMenu;
	private Button retourAuMenuFinPartie;
	private Button rejouerPartie;


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
		this.couperSon = false;

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
		// DEFINITIONS DES TOOLTIPS
		final Tooltip tooltipStat = new Tooltip("pions restants / pions initiaux");
		final Tooltip tooltipFaireJouerIA = new Tooltip("Faire jouer l'IA pour vous");
		final Tooltip tooltipCouperSon = new Tooltip("Couper/Remettre le son");

	
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
		Tooltip.install(pionsPerdusJ1, tooltipStat);

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
		Tooltip.install(pionsPerdusJ2, tooltipStat);

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
					App.gameController.getPlateauController().isClicking(cases[column][line], false);
				}
			}
		});
		//***************************************************************//





		//***************************************************************//
		//CREATION DU BOUTON POUR RETOURNER AU MENU
		retourAuMenu = new Button("MENU");
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
		//CREATION DU BOUTON POUR RETOURNER AU MENU
		retourAuMenuFinPartie = new Button("MENU");
		retourAuMenuFinPartie.setOnAction((event)->{
			try {
				App.reset();
				App.changementFenetre(super.getPanel(), App.sv.getPanel());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		retourAuMenuFinPartie.setVisible(false);

		//CREATION DU BOUTON POUR RETOURNER AU MENU
		rejouerPartie = new Button("REJOUER");
		rejouerPartie.setOnAction((event)->{
			this.displayGameButtons();
			App.replay();
		});
		rejouerPartie.setVisible(false);

		super.menuRapide(new ArrayList<Button>(Arrays.asList(retourAuMenuFinPartie, rejouerPartie)), 2, 40, 550, 200);

		//***************************************************************//

		
		
		//***************************************************************//
		//CREATION DU BOUTON POUR FAIRE JOUER IA
		
		Image IAJoue = new Image("image/faireJouerIA.png");
		ImageView IVIA = new ImageView(IAJoue);
		IVIA.setFitHeight(20);
		IVIA.setFitWidth(20);
		Button faireJouerIA = new Button();
		
		faireJouerIA.setTooltip(tooltipFaireJouerIA);
		faireJouerIA.setGraphic(IVIA);
		faireJouerIA.setTranslateX(520);
		faireJouerIA.setTranslateY(11);
		faireJouerIA.setId("boutonInvisible");
		faireJouerIA.setOnAction((event)->{
			App.gameController.getPlateauController().isClicking(null, true);;
		});
		
		
		//***************************************************************//



		//***************************************************************//
		//GIF POUR L'ATTENTE

		waitingCursor = new ImageView();
		Image cursor = new Image("image/waiting_cursor.gif");
		waitingCursor.setImage(cursor);
		waitingCursor.setLayoutX(140);
		waitingCursor.setLayoutY(23);



		//***************************************************************//



		
		

		//***************************************************************//
		//BOUTON POUR COUPER SON
		
		Image sonCoupe = new Image("image/sonNonCoupe.png");
		Button couperSon = new Button();
		
		couperSon.setTooltip(tooltipCouperSon);
		couperSon.setId("boutonInvisible");
		couperSon.setGraphic(new ImageView(sonCoupe));
		couperSon.setTranslateX(560);
		couperSon.setTranslateY(10);
		couperSon.setOnAction((event)->{
			this.SwitchCouperSon(couperSon);
		});
		

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
		super.getPanel().getChildren().add(retourAuMenuFinPartie);
		super.getPanel().getChildren().add(rejouerPartie);
		super.getPanel().getChildren().add(couperSon);
		super.getPanel().getChildren().add(faireJouerIA);

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
		pion.setCircle(isSelected, Color.BROWN);

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
		this.aQuiLeTour.setText("a gagné !");

		//régler l'icone du jeu
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

	public boolean getCouperSon(){
		return this.couperSon;
	}

	public void SwitchCouperSon(Button sonCoupe){
		getPanel().getChildren().remove(sonCoupe);
		this.couperSon = !this.couperSon;
		Image son = null;
		if(couperSon){
			son = new Image("image/sonCoupe.png");
		}
		else{
			son = new Image("image/sonNonCoupe.png");
		}
		sonCoupe.setGraphic(new ImageView(son));
		getPanel().getChildren().add(sonCoupe);
	}
	
	
	public void displayEndGameButtons(){
		retourAuMenu.setVisible(false);
		retourAuMenuFinPartie.setVisible(true);
		rejouerPartie.setVisible(true);
	}

	public void displayGameButtons(){
		retourAuMenuFinPartie.setVisible(false);
		rejouerPartie.setVisible(false);
		retourAuMenu.setVisible(true);
	}
}
