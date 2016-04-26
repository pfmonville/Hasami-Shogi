package controller;

import java.util.ArrayList;

import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.util.Duration;
import mainPackage.App;
import model.Case;
import model.Joueur;


public class GameController {
	private ArrayList<Joueur> joueurs = new ArrayList<>();
	private IAController IAForHuman;
	private static int joueurActuel = 0;
	private ArrayList<PlayerController> controllers = new ArrayList<>();
	private PlateauController plateauController; 
	private static boolean allIA = false;

	
	public GameController(){
		
	}
	
	
	/**
	 * récupère les options spéciales aux règles entrées par l'utilisateur dans optionView et modifie App.regles en conséquence
	 */
	private void getRegles(){
		//possibilité de faire changer les règles dans l'UI
		//if(App.os.getEcartAvantDefaite() == true){
		//	App.regles.setEcartAvantDefaite(true);
		//}
		//TODO : si le temps
	}
	
	/**
	 * 
	 * @return le controlleur du plateau pour ce gameController
	 */
	public PlateauController getPlateauController(){
		return this.plateauController;
	}
	
	
	/**
	 * 
	 * @return la liste des joueurs sous forme de Joueur
	 */
	public ArrayList<Joueur> getJoueurs() {
		return joueurs;
	}
	
	
	
	/**
	 * récupère les informations du panneau d'option et créer deux joueurs correspondants
	 */
	private void getJoueur(){
		String iahuJ1 = App.ov.getIahuJ1().getSelectedToggle().getUserData().toString();
		String iahuJ2 = App.ov.getIahuJ2().getSelectedToggle().getUserData().toString();
		String nvj1 = App.ov.getNvj1().getSelectedToggle().getUserData().toString();
		String nvj2 = App.ov.getNvj2().getSelectedToggle().getUserData().toString();
		boolean isHumain;
		isHumain = (iahuJ1 == "ia") ? false: true;
		joueurs.add(new Joueur(true, isHumain, Double.valueOf(nvj1).intValue(), 9));
		isHumain = (iahuJ2 == "ia") ? false: true;
		joueurs.add(new Joueur(false, isHumain, Double.valueOf(nvj2).intValue(), 9));
	}
	
	
	/**
	 * renvoie l'indice du joueur dont c'est le tour
	 * @return l'entier 0: premier joueur, 1: deuxième joueur
	 */
	public int getNumeroActualJoueur(){
		return joueurActuel;
	}
	
	
	/**
	 * permet de savoir si le joueur actuel est humain
	 * @param joueur (int) : l'indice du joueur actuel 
	 * @return vrai si le joueur est humain faux si le joueur est controllé par l'ia
	 */
	public boolean isActualJoueurHuman(int joueur){
		if (joueurs.get(joueur).isHumain()){
			return true;
		}
		return false;
	}
	
	/**
	 * renvoie l'index de l'autre joueur (celui pour qui ce n'est pas le tour)
	 * @return
	 */
	public int getNumeroAutreJoueur(){
		return joueurActuel == 0 ? 1 : 0;	
	}
	
	/**
	 * modifie la variable joueur indiquant l'index du joueur dans le tableau joueurs
	 * typiquement un toggle 0;1
	 */
	private void nextJoueur(){
		joueurActuel = 1 - joueurActuel;
	}
	
	/**
	 * appelée si le clic est valide est déclanche le coup du joueur humain
	 * @param casePlateau la case sur laquelle on a cliqué
	 */
	public void validClick(Case casePlateau){
		((HumanController)(controllers.get(joueurActuel))).setCasePlateau(casePlateau);
		controllers.get(joueurActuel).playAMove();
	}
	
	
	/**
	 * appel si le joueur humain clique sur l'icone pour laisser l'ia jouer à sa place
	 */
	public void IAPlayForHuman(){
		joueurs.get(joueurActuel).setHumain(false);
		if(joueurActuel == App.regles.getNumeroJoueurNoir()){
			IAForHuman.initToPlayForHuman(plateauController.getPionsNoirs(), plateauController.getPionsBlancs(), joueurs.get(joueurActuel));
		}
		else{
			IAForHuman.initToPlayForHuman(plateauController.getPionsBlancs(), plateauController.getPionsNoirs(), joueurs.get(joueurActuel));
		}
		Thread thread = new Thread((IAController)(IAForHuman));
		thread.start();
	}
	
	
	/**
	 * permet de savoir si la partie est gagné par le joueur actuel
	 * @return vrai si le coup que le joueur actuel vient d'effectuer entraine une victoire. Faux sinon
	 */
	private boolean testVictoire(){
		//si le joueur adverse à trop peu de pions (pendant son tour on ne peut perdre un de ses pions)
		if(joueurs.get(getNumeroAutreJoueur()).getNbPions() <= App.regles.getNbPiecesAvantDefaite()){
			return true;
		}
		
		//si la règle sur l'écart trop grand est selectionnée
		if(App.regles.isEcartAvantDefaite()){
			if(joueurs.get(joueurActuel).getNbPions() - joueurs.get(getNumeroAutreJoueur()).getNbPions() > App.regles.getNbEcartAvantDefaite()){
				return true;
			}
		}
		
		return false;
	}
	


	/**
	 * Met fin au tour en lancant le controlleur du prochain joueur si c'est une ia
	 * sinon il suffit d'attendre le clique de l'humain
	 */
	public void finTour(){
		//si la partie est finie, on désactive les clics et on affiche le gagnant
		
		if(testVictoire()){ 
			Platform.runLater(()-> App.pv.setWinnerTextInTopBanner(this.getNumeroActualJoueur()));
			App.pv.stopMouseListener();
			App.pv.displayEndGameButtons();
		}else{
			this.nextJoueur();
			//On met à jour l'image du joueur actuel
			Platform.runLater(()-> App.pv.switchImageJoueur(joueurActuel));
			//si le prochain joueur est une IA 
			if(!this.isActualJoueurHuman(joueurActuel)){
				Thread thread = new Thread((IAController)(controllers.get(joueurActuel)));
				thread.start();
			}else if (! ((HumanController) controllers.get(joueurActuel)).canPlayAMove()){
				String couleurJoueur;
				if(((HumanController) controllers.get(joueurActuel)).getJoueur().getNumeroJoueur() == App.regles.getNumeroJoueurNoir()){
					couleurJoueur = "Noir";
				}else{
					couleurJoueur = "Blanc";
				}
				Platform.runLater(() -> Notifications.create().title("Informations").text("Le joueur " + couleurJoueur + " a du passer").hideAfter(new Duration(2000)).hideCloseButton().owner(App.mainStage).show());
				finTour();
			}
			//sinon on attend le clique du joueur humain
			
		}
	}
	
	
	/**
	 * Commence le premier tour de jeu.
	 * typiquement si c'est une ia on appelle son controlleur sinon on attend le clique du joueur humain
	 */
	public void startGame(){
		if(!this.isActualJoueurHuman(joueurActuel)){
			Thread thread = new Thread((IAController)(controllers.get(joueurActuel)));
			thread.start();
		}
	}

	
	/**
	 * appelle les controlleurs correspondant aux joueurs et les stock dans le tableau controllers
	 */
	private void launchControllers(){
		
		plateauController = new PlateauController();
		for(Joueur joueur : joueurs){
			if (joueur.isHumain()){
				controllers.add(new HumanController(joueur));
			}
			else{
				controllers.add(new IAController(joueur));
				((IAController)(controllers.get(controllers.size() - 1))).init(plateauController.getPionsNoirs(), plateauController.getPionsBlancs());;
			}
		}
	}
	
	/**
	 * 
	 * @return vrai si seules des IA jouent faux sinon
	 */
	public static boolean isAllIA(){
		return allIA;
	}
	
	/**
	 * fonction mère du controlleur lancant la partie
	 */
	public void begin(){
		this.getRegles();
		this.getJoueur();
		this.launchControllers();
		if(!joueurs.get(0).isHumain() && !joueurs.get(1).isHumain()){
			GameController.allIA = true;
		}else{
			GameController.allIA = false;
		}
		//on règle une IA spéciale pour jouer à la place du joueur
		if(!allIA){
			IAForHuman = new IAController(null);
		}
		this.startGame();
	}
	
	
	public void restart(){
		this.finish();
		this.begin();
	}
	
	/**
	 * fonction de fin de partie ou lors du retour 
	 */
	public void finish(){
		controllers = new ArrayList<>();
		joueurs = new ArrayList<>();
		joueurActuel = 0;
		//App.pv.resetCursor();
		if(App.pv.getPanel().getChildren().contains(App.pv.getCursor())){
			App.pv.removeWaitingCursor();
		}
	}
}
