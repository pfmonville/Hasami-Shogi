package view;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import mainPackage.App;

public class OptionView extends View{
	private RadioButton iaJ1, iaJ2, huJ1, huJ2;
	private RadioButton nv1j1, nv2j1, nv3j1, nv4j1, nv5j1, nv6j1;
	private RadioButton nv1j2, nv2j2, nv3j2, nv4j2, nv5j2, nv6j2;
	private Label labelnvj1, labelnvj2;
	private Button valider;
	private Button quitter;
	private ToggleGroup iahuJ1;
	private ToggleGroup iahuJ2;
	private ToggleGroup nvj1;
	private ToggleGroup nvj2;


	public OptionView(int x, int y){
		super(x, y);
		
		
		GridPane gridPanel = new GridPane();
		gridPanel.setTranslateX(50);
		gridPanel.setTranslateY(250);
		
		//*********************************************************//
		//LISTE ET PANEL CONTENANT LES OPTIONS RELATIVES AU JOUEUR 2
		ArrayList<Node> listej1 = new ArrayList<Node>();

		//*********************************************************//

		
		
		
		//*********************************************************//
		//LISTE ET PANEL CONTENANT LES OPTIONS RELATIVES AU JOUEUR 2
		ArrayList<Node> listej2 = new ArrayList<Node>();

		//*********************************************************//


		//initialisation des élements à afficher
		
		//intstancie les tooltips
		final Tooltip tooltipJoueurNoir = new Tooltip("Joueur Noir");
		final Tooltip tooltipJoueurBlanc = new Tooltip("Joueur Blanc");
		final Tooltip tooltipNiveau = new Tooltip("Niveau de difficulté");
		final Tooltip tooltipIA = new Tooltip("Intelligence Artificielle");
		final Tooltip tooltipHU = new Tooltip("Joueur Humain");
		
		//instancie les icones humain, ia, niveau
		Image iconHU = new Image("image/humanIcon.png");
		Image iconIA = new Image("image/IAIcon.png");
		Image niveau = new Image("image/strength1.png");
		@SuppressWarnings("unused")
		Image niveau2 = new Image("image/strength2.png");
		
		//*********************************************************//
		//CHOIX HUMAIN OU IA J1
		
		Image imageJ1 = new Image("image/gogui-black-32x32.png");
		
		ImageView imagevJ1 = new ImageView(imageJ1);
		Tooltip.install(imagevJ1, tooltipJoueurNoir);
		imagevJ1.setFitHeight(28);
		imagevJ1.setPreserveRatio(true);
		iaJ1 = new RadioButton();
		iaJ1.setUserData("ia");
		iaJ1.setGraphic(new ImageView(iconIA));
		iaJ1.setTooltip(tooltipIA);
		huJ1 = new RadioButton();
		huJ1.setUserData("hu");
		huJ1.setGraphic(new ImageView(iconHU));
		huJ1.setTooltip(tooltipHU);
		iahuJ1 = new ToggleGroup(); 
		iaJ1.setToggleGroup(iahuJ1); 
		huJ1.setToggleGroup(iahuJ1);
		
		huJ1.setSelected(true);
		
		//tant que le thread n'est pas fonctionnel
		//iaJ1.setDisable(true);
		
		//AJOUT DE CES ELEMENTS A LA LISTE
		listej1.addAll(Arrays.asList(imagevJ1,new Text("\t"), iaJ1, huJ1));
				
		//*********************************************************//
		

		
		
		//*********************************************************//
		//CHOIX HUMAIN OU IA J2
		
		Image imageJ2 = new Image("image/gogui-white-32x32.png");
		ImageView imagevJ2 = new ImageView(imageJ2);
		Tooltip.install(imagevJ2, tooltipJoueurBlanc);
		imagevJ2.setFitHeight(28);
		imagevJ2.setPreserveRatio(true);
		iaJ2 = new RadioButton();
		iaJ2.setUserData("ia");
		iaJ2.setGraphic(new ImageView(iconIA));
		iaJ2.setTooltip(tooltipIA);
		huJ2 = new RadioButton();
		huJ2.setUserData("hu");
		huJ2.setGraphic(new ImageView(iconHU));
		huJ2.setTooltip(tooltipHU);
		iahuJ2 = new ToggleGroup();
		iaJ2.setToggleGroup(iahuJ2); 
		huJ2.setToggleGroup(iahuJ2);
		
		iaJ2.setSelected(true);
		
		//AJOUT DE CES ELEMENTS A LA LISTE
		listej2.addAll(Arrays.asList(imagevJ2,new Text("\t"), iaJ2, huJ2));
		
		//*********************************************************//

		
		
		
		//*********************************************************//
		//CHOIX NIVEAU J1
		
		labelnvj1 = new Label("");
		labelnvj1.setGraphic(new ImageView(niveau));
		labelnvj1.setId("niveau");
		labelnvj1.setTooltip(tooltipNiveau);
		nv1j1 = new RadioButton("1");
		nv1j1.setUserData("1");
		nv2j1 = new RadioButton("2");
		nv2j1.setUserData("2");
		nv3j1 = new RadioButton("3");
		nv3j1.setUserData("3");
		nv4j1 = new RadioButton("4");
		nv4j1.setUserData("4");
		nv5j1 = new RadioButton("5");
		nv5j1.setUserData("5");
		nv6j1 = new RadioButton("6");
		nv6j1.setUserData("6");
		
		nvj1 = new ToggleGroup();
		nv1j1.setToggleGroup(nvj1);
		nv2j1.setToggleGroup(nvj1);
		nv3j1.setToggleGroup(nvj1);
		nv4j1.setToggleGroup(nvj1);
		nv5j1.setToggleGroup(nvj1);
		nv6j1.setToggleGroup(nvj1);
		
		nv1j1.setDisable(true);
		nv2j1.setDisable(true);
		nv3j1.setDisable(true);
		nv4j1.setDisable(true);
		nv5j1.setDisable(true);
		nv6j1.setDisable(true);
		
		nv3j1.setSelected(true);
		
		//AJOUT DES ELEMENTS A LA LISTE
		listej1.addAll(Arrays.asList(new Text("\t"), labelnvj1, new Text("   "), nv1j1, nv2j1, nv3j1, nv4j1, nv5j1, nv6j1));
		
		//*********************************************************//

		
		
		
		
		
		
		//*********************************************************//
		//CHOIX NIVEAU J2

		labelnvj2 = new Label();
		labelnvj2.setGraphic(new ImageView(niveau));
		labelnvj2.setId("niveau");
		labelnvj2.setTooltip(tooltipNiveau);
		nv1j2 = new RadioButton("1");
		nv1j2.setUserData("1");
		nv2j2 = new RadioButton("2");
		nv2j2.setUserData("2");
		nv3j2 = new RadioButton("3");
		nv3j2.setUserData("3");
		nv4j2 = new RadioButton("4");
		nv4j2.setUserData("4");
		nv5j2 = new RadioButton("5");
		nv5j2.setUserData("5");
		nv6j2 = new RadioButton("6");
		nv6j2.setUserData("6");

		nvj2 = new ToggleGroup();
		nv1j2.setToggleGroup(nvj2);
		nv2j2.setToggleGroup(nvj2);
		nv3j2.setToggleGroup(nvj2);
		nv4j2.setToggleGroup(nvj2);
		nv5j2.setToggleGroup(nvj2);
		nv6j2.setToggleGroup(nvj2);
		
		nv3j2.setSelected(true);
		
		//AJOUT DES ELEMENTS A LA LISTE
		listej2.addAll(Arrays.asList(new Text("\t"), labelnvj2, new Text("   "),nv1j2, nv2j2, nv3j2, nv4j2, nv5j2, nv6j2));
		
		//*********************************************************//
		
		
		//AJOUT DES LISTENER SUR LES BOUTONS IA ET HU PERMETTANT DE GRISER LES NIVEAUX
		
		iaJ1.setOnAction((event)->{
			nv1j1.setDisable(false);
			nv2j1.setDisable(false);
			nv3j1.setDisable(false);
			nv4j1.setDisable(false);
			nv5j1.setDisable(false);
			nv6j1.setDisable(false);
		});
		
		huJ1.setOnAction((event)->{
			nv1j1.setDisable(true);
			nv2j1.setDisable(true);
			nv3j1.setDisable(true);
			nv4j1.setDisable(true);
			nv5j1.setDisable(true);
			nv6j1.setDisable(true);
		});
		
		iaJ2.setOnAction((event)->{
			nv1j2.setDisable(false);
			nv2j2.setDisable(false);
			nv3j2.setDisable(false);
			nv4j2.setDisable(false);
			nv5j2.setDisable(false);
			nv6j2.setDisable(false);
		});
		
		huJ2.setOnAction((event)->{
			nv1j2.setDisable(true);
			nv2j2.setDisable(true);
			nv3j2.setDisable(true);
			nv4j2.setDisable(true);
			nv5j2.setDisable(true);
			nv6j2.setDisable(true);
		});
		
		//*********************************************************//

		
		
		
		
		
		//*********************************************************//
		//AJOUT DES ELEMENTS AU GRIDPANEL
		
		for(int i=0;i<listej2.size();i++){
			gridPanel.add(listej2.get(i), i, 2);
		}
		
		//pour marquer l'espace entre les deux options
		gridPanel.add(new Text(""), 0 ,1);
		
		for(int i=0;i<listej1.size();i++){
			gridPanel.add(listej1.get(i), i, 0);
		}

		super.getPanel().getChildren().add(gridPanel);
		
		//*********************************************************//
		
		
		
		
		
		//*********************************************************//
		//AJOUT DU BOUTON POUR VALIDER
		
		valider = new Button("Valider");
		
		// action du bouton: changement de la fenètre vers le plateau
		valider.setOnAction((event)->{
			//Lancement du controlleur de jeu
			App.gameController.begin();
		});
		
		//*********************************************************//
		
		
		
		
		//*********************************************************//
		//AJOUT DU BOUTON QUITTER
		
		quitter = new Button("Quitter");
		
		//action du bouton: quitter l'application
		quitter.setOnAction((event)->{
			Platform.exit();
		});
		
		
		//*********************************************************//
		
		
		
		
		//*********************************************************//
		//AJOUT DE LA LISTE DE BOUTONS
		ArrayList<Button> validation = new ArrayList<Button>();
		validation.addAll(Arrays.asList(valider, quitter));
		
		super.menuRapide(validation, 1, 75, 380, 150);
		
		super.getPanel().getChildren().add(valider);
		super.getPanel().getChildren().add(quitter);
				
		//*********************************************************//
		
		
		
		//*********************************************************//
		//AJOUT D UN TITRE A LA PAGE
		
		Text titre = new Text("Hasami Shogi");
		titre.setId("titre");
		//mise en page du titre
		super.miseEnPageTexteCentre(titre, (int)(super.getPanel().getPrefWidth()), 150);
		
		//ajout du titre à optionview
		super.getPanel().getChildren().add(titre);
		
		//*********************************************************//
		
	}

	public ToggleGroup getIahuJ1() {
		return iahuJ1;
	}

	public ToggleGroup getIahuJ2() {
		return iahuJ2;
	}

	public ToggleGroup getNvj1() {
		return nvj1;
	}

	public ToggleGroup getNvj2() {
		return nvj2;
	}

	@Override
	public Pane getPanel(){
		return super.getPanel();
	}

}
