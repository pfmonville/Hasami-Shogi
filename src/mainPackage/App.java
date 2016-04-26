package mainPackage;

import java.io.IOException;

import controller.GameController;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Regles;
import model.Theme;
import view.OptionView;
import view.PlateauView;
import view.StartView;

public class App extends Application {
	public static Pane mainLayout;
	public static Stage mainStage;
	
	//Instancie les vues
	public static OptionView ov;
	public static StartView sv;
	public static PlateauView pv;
	
	public static Scene scene;
	public static Cursor oldCursor;
	
	//instancie les controlleurs
	public static GameController gameController = new GameController();
	
	//instancie la regle du jeu
	public static Regles regles = new Regles();
	
	//Instancie le thème pour le plateau et les pions
	public static Theme theme = new Theme();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws IOException{
		mainStage = stage;
		ov = new OptionView(600, 600);
		pv = new PlateauView(600, 600);
		sv = new StartView(600, 600);

		mainLayout = new Pane();
		mainLayout.getChildren().add(sv.getPanel());
		
		scene = new Scene(mainLayout);
		scene.addEventFilter(KeyEvent.ANY, KeyEvent::consume);

		chargerCSS("css/appli.css", scene);
		
		stage.setScene(scene);
		oldCursor = scene.getCursor();
		stage.getIcons().add(new Image("image/icone.png"));
		stage.setResizable(false);
		stage.show();
	}
	
	public static void reset(){
		ov = new OptionView(600, 600);
		pv = new PlateauView(600, 600);
		sv = new StartView(600, 600);
		gameController.finish();
	}
	
	public static void replay(){
		//supprimer l'ancien PlateauView de la vue
		mainLayout.getChildren().remove(pv.getPanel());
		//recharger le PlateauView
		pv = new PlateauView(600, 600);
		//on relance le jeu
		gameController.restart();
	}

	public static void changementFenetre(Pane aEffacer, Pane aAfficher) throws IOException{
		mainLayout.getChildren().remove(aEffacer);
		mainLayout.getChildren().add(aAfficher);
	}
	
	public static void chargerCSS(String chemin, Scene scene){
		scene.getStylesheets().add(chemin);
	}
	
}
