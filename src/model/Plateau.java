package model;

public class Plateau extends Elements{
	
	private Case cases[];
	private Pion pions[];
	
	public Plateau(int x, int y) {
		super(x, y);
	}

	//getters et setters
	public Case[] getCases() {
		return cases;
	}

	public void setCases(Case[] cases) {
		this.cases = cases;
	}

	public Pion[] getPions() {
		return pions;
	}

	public void setPions(Pion[] pions) {
		this.pions = pions;
	}
}
