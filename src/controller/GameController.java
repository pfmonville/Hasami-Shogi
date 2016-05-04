package controller;

import java.util.ArrayList;

import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.util.Duration;
import mainPackage.App;
import model.Case;
import model.History;
import model.Joueur;
import model.Pion;


public class GameController {
	private ArrayList<Joueur> joueurs = new ArrayList<>();
	private IAController IAForHuman;
	private static int joueurActuel = 0;
	private ArrayList<PlayerController> controllers = new ArrayList<>();
	private PlateauController plateauController; 
	private static boolean allIA = false;
	private History history;
	private int actualTurn;

	
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
		Thread thread = new Thread((HumanController)(controllers.get(joueurActuel)));
		thread.start();
		//controllers.get(joueurActuel).playAMove();
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
		//si c'est un humain qui vient de jouer on conserve son état au cas où il décide d'annuler son coup
		if(isActualJoueurHuman(joueurActuel)){
			history.addTurn(plateauController.getPionsNoirs(), plateauController.getPionsBlancs(), joueurActuel);
		}
		//si la partie est finie, on désactive les clics et on affiche le gagnant
		if(testVictoire()){ 
			Platform.runLater(()-> App.pv.setWinnerTextInTopBanner(this.getNumeroActualJoueur()));
			App.pv.stopMouseListener();
			App.pv.displayEndGameButtons();
		}else{
			this.actualTurn++;
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
	 * remet l'état du jeu tel qu'il était un tour auparavant
	 */
	public void discardMove(){
		//TODO:
		//si c'est le tout premier tour de jeu, on ne peut revenir en arrière ou si c'est le deuxième tour mais que l'IA joue en premier
		if(this.actualTurn == 1 || (this.actualTurn == 2 && ! this.isActualJoueurHuman(getNumeroAutreJoueur())) ){
			Platform.runLater(() -> Notifications.create().title("Informations").text("Vous ne pouvez annuler ce coup").hideAfter(new Duration(2000)).hideCloseButton().owner(App.mainStage).show());
			return;
		}
	
		//suppression graphique des pions actuels
		for(Pion pion: plateauController.getPionsNoirs()){
			App.pv.suppressionPion(pion);
		}
		for(Pion pion: plateauController.getPionsBlancs()){
			App.pv.suppressionPion(pion);
		}
		
		//suppression de l'état actuel et remise dans l'état précédent
		history.discardLastMove(PlateauController.getCases(), plateauController.getPionsNoirs(), plateauController.getPionsBlancs());
		joueurs.get(App.regles.getNumeroJoueurNoir()).setNbPions(plateauController.getPionsNoirs().size());
		joueurs.get(App.regles.getNumeroJoueurBlanc()).setNbPions(plateauController.getPionsBlancs().size());

		if(! this.isActualJoueurHuman(joueurActuel) || ! this.isActualJoueurHuman(getNumeroAutreJoueur())){
			this.actualTurn -= 2;
		}else{
			this.actualTurn--;
		}
		
		//plus d'actualité
		//PlateauController.setCases(history.getPlateauAtLastTurn());
		//plateauController.setPionsNoirs(history.getPionsNoirsAtLastTurn());
		//plateauController.setPionsBlancs(history.getPionsBlancsAtLastTurn());
		
		//réaffichage des pions sur le plateau
		App.pv.displayNewBoardPosition(plateauController.getPionsNoirs(), plateauController.getPionsBlancs());
		
		//le joueur précédent reprend la main
		GameController.joueurActuel = history.getJoueurNumberAtLastTurn();
		
		//On met à jour l'image du joueur actuel
		Platform.runLater(()-> App.pv.switchImageJoueur(GameController.joueurActuel));
		
		//on remet la bannière correctement en cas de retour après fin du jeu
		App.pv.fixBannerAfterDiscard();
		
		//on remet les boutons
		App.pv.displayGameButtons();
		
		//on réactive la souris
		App.pv.startMouseListener();
		
		//on attend l'input de l'humain (on ne revient jamais sur l'état d'une IA)
	}
	
	
	/**
	 * Commence le premier tour de jeu.
	 * typiquement si c'est une ia on appelle son controlleur sinon on attend le clique du joueur humain
	 */
	public void startGame(){
		this.actualTurn = 1;
		this.history = new History(PlateauController.getCases(), plateauController.getPionsNoirs(), plateauController.getPionsBlancs());
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
