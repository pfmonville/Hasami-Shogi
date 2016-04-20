package view;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import mainPackage.App;

public class StartView extends View{

	private Button jouer;
	private Button quitter;
	private Text titre;
	
	public StartView(int x, int y){
		super(x, y);
		
		//initialisation des éléments à afficher
		jouer = new Button("Jouer");
		quitter = new Button("Quitter");
		titre = new Text("Hasami Shogi");
		titre.setId("titre");
		
		//mise en page du titre
		super.miseEnPageTexteCentre(titre, (int)(super.getPanel().getPrefWidth()), 150);
		
		//mise en page des boutons
		ArrayList<Button> menu = new ArrayList<Button>();
		menu.add(jouer);
		menu.add(quitter);
		super.menuRapide(menu, 1, 100, 300, 100);
		
		//ajout des éléments au panel
		ajouterElement(jouer);
		ajouterElement(quitter);
		ajouterElement(titre);

		quitter.setOnAction((event)->{
			Platform.exit();
		});
		
		jouer.setOnAction((event)->{
			try {
				App.changementFenetre(super.getPanel(), App.ov.getPanel());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
		
	
	private void ajouterElement(Node element){
		super.getPanel().getChildren().add(element);
	}
}
