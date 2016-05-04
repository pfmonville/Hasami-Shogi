package model;

import java.util.ArrayList;

import controller.iaUtils.EvaluatePosition;


public class Deplacement implements Comparable<Deplacement>{
	private Pion pion;
	private Case casePlateau;
	private double estimatedScore;
	
	//TODO: a virer apres
	private ArrayList<Case>cases;
	
	public Deplacement(Pion pion, Case casePlateau){
		this.pion = pion;
		this.casePlateau = casePlateau;
		this.estimatedScore = -EvaluatePosition.maxValuePossible();
	}
			
	public Pion getPion(){
		return pion;
	}
	
	public void setEstimatedScore(double score){
		this.estimatedScore = score;
	}
	
	public double getEstimatedScore(){
		return this.estimatedScore;
	}
	
	public Case getCase(){
		return this.casePlateau;
	}
	
	public void setCase(Case casePlateau){
		this.casePlateau = casePlateau;
	}
	
	public ArrayList<Case> getCases(){
		return this.cases;
	}

	@Override
	public int compareTo(Deplacement o) {
		if(this.estimatedScore - o.getEstimatedScore() > 0){
			return 1;
		}else if(this.estimatedScore - o.getEstimatedScore() < 0){
			return -1;
		}
		return 0;
	}
}