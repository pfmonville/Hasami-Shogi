package controller;

import java.util.ArrayList;

import javafx.application.Platform;
import mainPackage.App;
import model.Case;
import model.Joueur;

public class GameController {
	private ArrayList<Joueur> joueurs = new ArrayList<>();
	private static int joueurActuel = 0;
	private ArrayList<PlayerController> controllers = new ArrayList<>();
	private PlateauController plateauController; 
	private static boolean allIA = false;
	
	public GameController(){
		
	}
	
	
	/**
	 * r�cup�re les options sp�ciales aux r�gles entr�es par l'utilisateur dans optionView et modifie App.regles en cons�quence
	 */
	private void getRegles(){
		//possibilit� de faire changer les r�gles dans l'UI
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
	 * r�cup�re les informations du panneau d'option et cr�er deux joueurs correspondants
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
	 * @return l'entier 0: premier joueur, 1: deuxi�me joueur
	 */
	public int getNumeroActualJoueur(){
		return joueurActuel;
	}
	
	
	/**
	 * permet de savoir si le joueur actuel est humain
	 * @param joueur (int) : l'indice du joueur actuel 
	 * @return vrai si le joueur est humain faux si le joueur est controll� par l'ia
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
	public int autreJoueur(){
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
	 * appel�e si le clic est valide est d�clanche le coup du joueur humain
	 * @param casePlateau la case sur laquelle on a cliqu�
	 */
	public void validClick(Case casePlateau){
		((HumanController)(controllers.get(joueurActuel))).setCasePlateau(casePlateau);
		controllers.get(joueurActuel).playAMove();
	}
	
	
	
	/**
	 * permet de savoir si la partie est gagn� par le joueur actuel
	 * @return vrai si le coup que le joueur actuel vient d'effectuer entraine une victoire. Faux sinon
	 */
	private boolean testVictoire(){
		//si le joueur adverse � trop peu de pions (pendant son tour on ne peut perdre un de ses pions)
		if(joueurs.get(autreJoueur()).getNbPions() <= App.regles.getNbPiecesAvantDefaite()){
			return true;
		}
		
		//si la r�gle sur l'�cart trop grand est selectionn�e
		if(App.regles.isEcartAvantDefaite()){
			if(joueurs.get(joueurActuel).getNbPions() - joueurs.get(autreJoueur()).getNbPions() > App.regles.getNbEcartAvantDefaite()){
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
		//si la partie est finie, on d�sactive les clics et on affiche le gagnant
		
		if(testVictoire()){ 
			Platform.runLater(()-> App.pv.setWinnerTextInTopBanner(this.getNumeroActualJoueur()));
			App.pv.stopMouseListener();
		}else{
			this.nextJoueur();
			//On met � jour l'image du joueur actuel
			Platform.runLater(()-> App.pv.switchImageJoueur(joueurActuel));
			//si le prochain joueur est une IA 
			if(!this.isActualJoueurHuman(joueurActuel)){
				Thread thread = new Thread((IAController)(controllers.get(joueurActuel)));
				thread.start();
				//controllers.get(joueurActuel).playAMove();
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
			//controllers.get(joueurActuel).playAMove();
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
	 * fonction m�re du controlleur lancant la partie
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
		this.startGame();
	}
	
	
	/**
	 * fonction de fin de partie ou lors du retour 
	 */
	public void finish(){
		controllers = new ArrayList<>();
		joueurs = new ArrayList<>();
		joueurActuel = 0;
		App.pv.resetCursor();
		if(App.pv.getPanel().getChildren().contains(App.pv.getCursor())){
			App.pv.removeWaitingCursor();
		}
	}
}
