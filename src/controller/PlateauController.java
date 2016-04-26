package controller;

import java.io.InputStream;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import mainPackage.App;
import model.Case;
import model.CouleurPion;
import model.Pion;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class PlateauController {
	private static Case cases[][];
	private ArrayList<Pion> pionsBlancs = new ArrayList<>();
	private ArrayList<Pion> pionsNoirs = new ArrayList<>();

	public PlateauController() {
		cases = new Case[App.regles.getNbDeCaseParLigne()][App.regles.getNbDeCaseParLigne()];

		//création des cases. Vides pour le moment
		for(int i=0;i<App.regles.getNbDeCaseParLigne();i++){
			for(int j=0;j<App.regles.getNbDeCaseParLigne();j++){
				cases[i][j] = new Case(null);
				cases[i][j].init(App.theme.getTailleCase(), i, j);
			}
		}

		//Création des pions noirs et blancs
		for(int i = 0; i < App.regles.getNbDeCaseParLigne(); i++){
			pionsNoirs.add(new Pion(CouleurPion.NOIR, cases[i][App.regles.getNbDeCaseParLigne() - 1], App.regles.getNumeroJoueurNoir(), i));
			pionsBlancs.add(new Pion(CouleurPion.BLANC, cases[i][0], App.regles.getNumeroJoueurBlanc(), i));
		}

		//mise à jour des différentes classes qui dépendent de la création des pions
		App.pv.begin(cases, pionsBlancs, pionsNoirs);
		try {
			App.changementFenetre(App.ov.getPanel(), App.pv.getPanel());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	

	public static Case[][] getCases() {
		return cases;
	}




	public ArrayList<Pion> getPionsBlancs() {
		return pionsBlancs;
	}




	public ArrayList<Pion> getPionsNoirs() {
		return pionsNoirs;
	}




	/**
	 * reset toutes les cases en surbrillance
	 */
	public static void resetAllHighlight(){
		for (Case[] casesLignes: cases){
			for(Case casePlateau: casesLignes){
				casePlateau.resetHighlight();
			}
		}
	}

	/**
	 * mets les cases en paramètre en surbrillance
	 * @param casesCiblees: les cases à mettre en surbrillance
	 */
	public static void putCasesInHighlight(ArrayList<Case> casesCiblees){
		for(Case caseCiblee:casesCiblees)
		caseCiblee.putInhighlight();
	}

	/**
	 * renvoie une liste contenant toutes les cases où le pion passé en paramètre peut aller
	 * @param pion: le pion qui veut voir les cases où il peut aller
	 */
	public static ArrayList<Case> getPossibleMoves(Pion pion){
		//récupère les coodronnées du pion
		int pionX = pion.getCasePlateau().getCoordonneeX();
		int pionY = pion.getCasePlateau().getCoordonneeY();
		ArrayList<Case> possibleCases = new ArrayList<Case>();


		//du pion vers la droite
		for(int i = pionX + 1; i < App.regles.getNbDeCaseParLigne(); i++){
			//si la case est libre
			if(cases[i][pionY].getPion() == null){
				possibleCases.add(cases[i][pionY]);
			}else{
				//à partir de là il n'y a plus de déplacements valides
				break;
			}
		}


		//du pion vers la gauche
		for(int i = pionX - 1; i >= 0; i--){
			if(cases[i][pionY].getPion() == null){
				possibleCases.add(cases[i][pionY]);
			}else{
				//à partir de là il n'y a plus de déplacements valides
				break;
			}
		}

		//du pion vers le haut
		for(int i = pionY - 1;i >= 0 ; i--){
			if(cases[pionX][i].getPion() == null){
				possibleCases.add(cases[pionX][i]);
			}else{
				//à partir de là il n'y a plus de déplacements valides
				break;
			}
		}

		//du pion vers le bas
		for(int i = pionY + 1;i < App.regles.getNbDeCaseParLigne(); i++){
			if(cases[pionX][i].getPion() == null){
				possibleCases.add(cases[pionX][i]);
			}else{
				//à partir de là il n'y a plus de déplacements valides
				break;
			}
		}
		
		return possibleCases;
	}
	
	
	
	/**
	 * met en surbrillance toutes les cases où le pion passé en paramètre peut aller
	 * @param pion: le pion qui veut voir les cases où il peut aller
	 */
	public void highlightPossibleMoves(Pion pion){
		putCasesInHighlight(getPossibleMoves(pion));
	}
	
	
	
	/**
	 * Regarde la nouvelle position du pion donnée en paramètre et regarde si cela resulte en une capture et supprime les pions correspondant
	 * @param pion: le pion qui vient de se déplacer
	 * @param cases: le plateau sur lequel les vérifications sont effectuées
	 * @param toDisplay: vrai si 
	 */
	public static ArrayList<Pion> verifierCapture(Pion pion, Case[][] cases){
		
		int indiceMax = App.regles.getNbDeCaseParLigne() - 1;
		
		//récupère les coodronnées du pion
		int pionX = pion.getCasePlateau().getCoordonneeX();
		int pionY = pion.getCasePlateau().getCoordonneeY();
		
		ArrayList<Pion> listeFinale = new ArrayList<>();
		ArrayList<Pion> pionsCaptures = new ArrayList<>();
		
		//du pion vers la droite
		for(int i = pionX + 1; i <= indiceMax; i++){

			//si la case est libre c'est qu'il n'y a rien à capturer 
			if(cases[i][pionY].getPion() == null){
				break;

			}else{

				//Si le pion rencontré appartient au joueur et qu'il y a au moins un pion pris en sandwich on supprime les pions capturés
				if(pion.getNumeroJoueur() == cases[i][pionY].getPion().getNumeroJoueur()){
					listeFinale.addAll(pionsCaptures);
					break;
				}

				//sinon si le pion rencontré appartient à l'adversaire on le met dans la liste de pions potentiellement capturés
				else if(pion.getNumeroJoueur() != cases[i][pionY].getPion().getNumeroJoueur()){
					pionsCaptures.add(cases[i][pionY].getPion());
				}
			}
		}

		//on réinitialise la liste pionCapturés
		pionsCaptures = new ArrayList<>();

		//du pion vers la gauche
		for(int i = pionX - 1; i >= 0; i--){

			//si la case est libre c'est qu'il n'y a rien à capturer 
			if(cases[i][pionY].getPion() == null){
				break;

			}else{

				//Si le pion rencontré appartient au joueur et qu'il y a au moins un pion pris en sandwich on supprime les pions capturés
				if(pion.getNumeroJoueur() == cases[i][pionY].getPion().getNumeroJoueur()){
					listeFinale.addAll(pionsCaptures);
					break;
				}

				//sinon si le pion rencontré appartient à l'adversaire on le met dans la liste de pions potentiellement capturés
				else if(pion.getNumeroJoueur() != cases[i][pionY].getPion().getNumeroJoueur()){
					pionsCaptures.add(cases[i][pionY].getPion());
				}

			}
		}

		pionsCaptures = new ArrayList<>();
		
		//du pion vers le haut
		for(int i = pionY - 1;i >= 0 ; i--){

			//si la case est libre c'est qu'il n'y a rien à capturer 
			if(cases[pionX][i].getPion() == null){
				break;

			}else{

				//Si le pion rencontré appartient au joueur et qu'il y a au moins un pion pris en sandwich on supprime les pions capturés
				if(pion.getNumeroJoueur() == cases[pionX][i].getPion().getNumeroJoueur()){
					listeFinale.addAll(pionsCaptures);
					break;
				}

				//sinon si le pion rencontré appartient à l'adversaire on le met dans la liste de pions potentiellement capturés
				else if(pion.getNumeroJoueur() != cases[pionX][i].getPion().getNumeroJoueur()){
					pionsCaptures.add(cases[pionX][i].getPion());
				}

			}
		}
		
		pionsCaptures = new ArrayList<>();

		//du pion vers le bas
		for(int i = pionY + 1;i <= indiceMax; i++){

			//si la case est libre c'est qu'il n'y a rien à capturer 
			if(cases[pionX][i].getPion() == null){
				break;

			}else{

				//Si le pion rencontré appartient au joueur et qu'il y a au moins un pion pris en sandwich on supprime les pions capturés
				if(pion.getNumeroJoueur() == cases[pionX][i].getPion().getNumeroJoueur()){
					listeFinale.addAll(pionsCaptures);
					break;
				}

				//sinon si le pion rencontré appartient à l'adversaire on le met dans la liste de pions potentiellement capturés
				else if(pion.getNumeroJoueur() != cases[pionX][i].getPion().getNumeroJoueur()){
					pionsCaptures.add(cases[pionX][i].getPion());
				}

			}
		}
		
		if(App.regles.isCapturerDiagonale()){
			//TODO: faire les 4 directions 
			//diagonale haut-gauche
			//diagonale haut-droite
			//diagonale bas-gauche
			//diagonale bas-droite
		}
		
		
		//vérifier les pions dans les coins extremes
		
		
		if(App.regles.isCapturerCoins()){
			//TODO: 
			
			
		}else{
			//si le pion en haut à gauche appartient à l'adversaire
			if(cases[0][0].getPion() != null && cases[0][0].getPion().getNumeroJoueur() != pion.getNumeroJoueur()){
				Pion pion1 = cases[0][1].getPion();
				Pion pion2 = cases[1][0].getPion();
				//si il existe deux pions du joueurs qui bloquent le pion adverse qui est dans le coin alors on capture ce pion
				if(pion1 != null && pion2 != null && (pion1.getNumeroJoueur() == pion2.getNumeroJoueur()) && pion1.getNumeroJoueur() == pion.getNumeroJoueur()){
					listeFinale.add(cases[0][0].getPion());
				}
			}
					
			//si le pion en bas à gauche appartient à l'adversaire
			if(cases[0][indiceMax].getPion() != null && cases[0][indiceMax].getPion().getNumeroJoueur() != pion.getNumeroJoueur()){
				Pion pion1 = cases[0][indiceMax - 1].getPion();
				Pion pion2 = cases[1][indiceMax].getPion();
				//si il existe deux pions du joueurs qui bloquent le pion adverse qui est dans le coin alors on capture ce pion
				if(pion1 != null && pion2 != null && (pion1.getNumeroJoueur() == pion2.getNumeroJoueur()) && pion1.getNumeroJoueur() == pion.getNumeroJoueur()){
					listeFinale.add(cases[0][indiceMax].getPion());
				}
			}
			
			
			//si le pion en haut à droite appartient à l'adversaire
			if(cases[indiceMax][0].getPion() != null && cases[indiceMax][0].getPion().getNumeroJoueur() != pion.getNumeroJoueur()){
				Pion pion1 = cases[indiceMax - 1][0].getPion();
				Pion pion2 = cases[indiceMax][1].getPion();
				//si il existe deux pions du joueurs qui bloquent le pion adverse qui est dans le coin alors on capture ce pion
				if(pion1 != null && pion2 != null && (pion1.getNumeroJoueur() == pion2.getNumeroJoueur()) && pion1.getNumeroJoueur() == pion.getNumeroJoueur()){
					listeFinale.add(cases[indiceMax][0].getPion());
				}
			}
			
			
			//si le pion en bas à droite appartient à l'adversaire
			if(cases[indiceMax][indiceMax].getPion() != null && cases[indiceMax][indiceMax].getPion().getNumeroJoueur() != pion.getNumeroJoueur()){
				Pion pion1 = cases[indiceMax - 1][indiceMax].getPion();
				Pion pion2 = cases[indiceMax][indiceMax - 1].getPion();
				//si il existe deux pions du joueurs qui bloquent le pion adverse qui est dans le coin alors on capture ce pion
				if(pion1 != null && pion2 != null && (pion1.getNumeroJoueur() == pion2.getNumeroJoueur()) && pion1.getNumeroJoueur() == pion.getNumeroJoueur()){
					listeFinale.add(cases[indiceMax][indiceMax].getPion());
				}
			}
		}

		
		return listeFinale;	
	}


	/**
	 * essaye de déplacer le pion sur une nouvelle case
	 * met à jour la case de départ et d'arrivée
	 * @param pion: le pion qui doit être déplacé
	 * @param caseCiblee: la case d'arrivée du pion
	 * @param toDisplay: vrai si le déplacement doit se refléter sur le plateau.
	 * @return vrai si le déplacement est autorisé, false sinon
	 */
	public boolean deplacerPion(Pion pion, Case caseCiblee, boolean isIA){
		
		if (caseCiblee.isHighlighted() || isIA){
			
			//d'abord on supprime la reference du pion dans l'ancienne case
			pion.getCasePlateau().setPion(null);
			
			//on met à jour le pion
			pion.setCasePlateau(caseCiblee);
			
			//on met à jour la nouvelle case
			caseCiblee.setPion(pion);
			
			//on remet tous les pions en normal (le cercle qui pouvait apparaitre disparait)
			for(Pion pionBlanc:this.pionsBlancs){
				Platform.runLater(()->pionBlanc.setCircle(false, Color.TRANSPARENT));
			}
			for(Pion pionNoir:this.pionsNoirs){
				Platform.runLater(()->pionNoir.setCircle(false, Color.TRANSPARENT));
			}
			//on met un rond coloré sur le dernier pion joué
			if(pion.getNumeroJoueur()==App.regles.getNumeroJoueurBlanc()){
				Platform.runLater(()->pion.setCircle(true, Color.rgb(56, 58, 55)));
			}
			else{
				Platform.runLater(()->pion.setCircle(true, Color.rgb(211, 215, 207)));
			}
			
			Platform.runLater(()-> App.pv.deplacerPion(pion, caseCiblee.getCoordonneeX(), caseCiblee.getCoordonneeY()));
			
			//joue un son aléatoire parmi une banque de son stockée dans App.règles
			if(!App.pv.getCouperSon()){
				playSound(App.regles.getASound());
			}
			return true;
		}
		return false;
	}

	/**
	 * supprime le pion du plateau
	 * @param pion: le pion qui sera supprimé
	 */
	public void supprimerPion(Pion pion){
		//suppression graphique du pion
		App.pv.suppressionPion(pion);

		//on supprime la référence du pion pour la case
		pion.getCasePlateau().setPion(null);
		//si le pion est noir
		if(pion.getNumeroJoueur() == App.regles.getNumeroJoueurNoir()){
			//on supprime le pion de la base
			pionsNoirs.remove(pion);
			//On met à jour le nombre de pions pour le joueur
			App.gameController.getJoueurs().get(App.regles.getNumeroJoueurNoir()).loseOnePion();
			App.pv.setTexteJ1(pionsNoirs.size());
		}else{
			pionsBlancs.remove(pion);
			//On met à jour le nombre de pions pour le joueur
			App.gameController.getJoueurs().get(App.regles.getNumeroJoueurBlanc()).loseOnePion();
			App.pv.setTexteJ2(pionsBlancs.size());
		}
	}
	
	
	/**
	 * supprime une liste de pion du plateau en mettant à jour tout ce qu'il faut
	 * @param pions: la liste de pion
	 */
	public void supprimerPion (ArrayList<Pion> pions){
		for(Pion pion: pions){
			//suppression graphique du pion
			Platform.runLater(()-> App.pv.suppressionPion(pion));

			//on supprime la référence du pion pour la case
			pion.getCasePlateau().setPion(null);
			//si le pion est noir
			if(pion.getNumeroJoueur() == App.regles.getNumeroJoueurNoir()){
				//on supprime le pion de la base
				pionsNoirs.remove(pion);
				//On met à jour le nombre de pions pour le joueur
				App.gameController.getJoueurs().get(App.regles.getNumeroJoueurNoir()).loseOnePion();
			}else{
				pionsBlancs.remove(pion);
				//On met à jour le nombre de pions pour le joueur
				App.gameController.getJoueurs().get(App.regles.getNumeroJoueurBlanc()).loseOnePion();
			}
		}
		
		//mise à jour graphique du score
		Platform.runLater(()-> App.pv.setTexteJ1(pionsNoirs.size()));
		Platform.runLater(()-> App.pv.setTexteJ2(pionsBlancs.size()));
	}


	/**
	 * méthode appelée par la vue afin de notifier un clique du joueur
	 * @param casePlateau: la case sur laquelle il a cliqué, null si le clique est en dehors
	 * @param letIAPlayAMove: vrai si le joueur a cliqué sur le bouton pour que l'IA joue pour lui faux sinon
	 */
	public void isClicking(Case casePlateau, boolean letIAPlayAMove){

		//Si c'est à l'ia de jouer on ne prend pas en compte les clics.
		if(App.gameController.isActualJoueurHuman(App.gameController.getNumeroActualJoueur())){
			//si le joueur veut que l'IA joue pour lui
			if(letIAPlayAMove){
				App.gameController.IAPlayForHuman();
			}
			else{
				App.gameController.validClick(casePlateau);
			}
		}
	}
	
	
	/**
	 * permet de jouer un son lorsque le joueur joue un coup
	 * @param sound le chemin du son à jouer
	 */
	public void playSound(String sound){
	    try{
		    InputStream in = getClass().getResourceAsStream("/"+sound);
		 
		    // create an audiostream from the inputstream
		    AudioStream audioStream = new AudioStream(in);
		 
		    // play the audio clip with the audioplayer class
		    AudioPlayer.player.start(audioStream);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}

}
