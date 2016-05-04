package model;

public class Score{
	private double score;
	private Pion pion;
	private Case casePlateau;
	
	public Score(Pion pion, Case casePlateau, Double score) {
		this.pion = pion;
		this.casePlateau = casePlateau;
		this.score = score;
	}
	
	public Pion getPion(){
		return pion;
	}
	
	public Case getCase(){
		return casePlateau;
	}
	
	public double getScore(){
		return score;
	}
	
	public void randomizeScore(){
		this.score += (Math.random()*0.01) - 0.005;
	}
}