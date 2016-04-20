package controller;

import java.util.ArrayList;

import mainPackage.App;
import model.Case;
import model.Joueur;
import model.Pion;

public class HumanController implements PlayerController{
	private Case casePlateau;
	private Pion actualPion;
	private Joueur joueur;
	
	public HumanController(Joueur joueur) {
		super();
		actualPion = null;
		this.joueur = joueur;
	}
	
	/**
	 * renseigne le controller de la case à étudier
	 * @param casePlateau: la case à étudier
	 */
	public void setCasePlateau(Case casePlateau){
		this.casePlateau = casePlateau;
	}
	
	
	private boolean canPlayAMove(){
		if(this.joueur.isPremier()){
			for(Pion pion: App.gameController.getPlateauController().getPionsNoirs()){
				if(!PlateauController.getPossibleMoves(pion).isEmpty()){
					return true;
				}
			}
		}else{
			for(Pion pion: App.gameController.getPlateauController().getPionsBlancs()){
				if(!PlateauController.getPossibleMoves(pion).isEmpty()){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * essaye de jouer un coup en fonction de la case cliquée.
	 * cela peut nécessiter plusieurs appels avant de mettre fin au tour.
	 */
	@SuppressWarnings("static-access")
	@Override
	public void playAMove() {
		
		//test si le joueur peut bouger au moins un pion
		if(canPlayAMove()){
			//si un pion avait été selectionné
			if(this.actualPion != null){
				//on essaye de déplacer le pion
				if(App.gameController.getPlateauController().deplacerPion(this.actualPion, this.casePlateau, false)){
					
					//on déselectionne le pion
					App.pv.makePionSelected(false, this.actualPion);
					//on vérifie si le déplacement résulte en une capture
					ArrayList<Pion> pionASupprimer = App.gameController.getPlateauController().verifierCapture(this.actualPion, PlateauController.getCases());
					App.gameController.getPlateauController().supprimerPion(pionASupprimer);
					//On enlève ce pion de la varible pion courant
					this.actualPion = null;
					App.gameController.getPlateauController().resetAllHighlight();
					App.gameController.finTour();
					
				}else{//si le déplacement n'est pas valide, on remet à zéro toutes les cases et on déselectionne le pion
					
					//on déselectionne le pion
					App.pv.makePionSelected(false, this.actualPion);
					App.gameController.getPlateauController().resetAllHighlight();
					
					//mais que la case selectionnée contient un autre de ses pions alors on switch le pion actuel avec le nouveau
					if(this.casePlateau.getPion() != null && this.casePlateau.getPion().getNumeroJoueur() == App.gameController.getNumeroActualJoueur()){
						
						this.actualPion = this.casePlateau.getPion();
						App.pv.makePionSelected(true, this.actualPion);
						App.gameController.getPlateauController().highlightPossibleMoves(this.actualPion);
						
					}else{ //sinon on déselectionne
						
						this.actualPion = null;
					
					}
				}
			}else{
				//si la case contient un pion appartenant au joueur on le selectionne et on met ses déplacements possible en surbrillance
				if(this.casePlateau.getPion() != null && this.casePlateau.getPion().getNumeroJoueur() == App.gameController.getNumeroActualJoueur()){
					this.actualPion = this.casePlateau.getPion();
					//on note le pion comme selectionné
					App.pv.makePionSelected(true, this.actualPion);
					//on surligne tous les déplacements possibles
					App.gameController.getPlateauController().highlightPossibleMoves(this.actualPion);
				}
			}	
		}else{
			//TODO: faire un affichage montrant au joueur qu'il ne peut pas jouer et qu'il lui faut passer
		}
	}	
}
