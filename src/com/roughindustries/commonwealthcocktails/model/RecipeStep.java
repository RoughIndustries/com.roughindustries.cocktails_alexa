package com.roughindustries.commonwealthcocktails.model;

public class RecipeStep {
	
	public Ingredient ingredeint;
	public int ordinal;
	public double cardinal;
	public String doze;
	
	public RecipeStep(Ingredient ingredeint, int ordinal, double cardinal, String doze) {
		super();
		this.ingredeint = ingredeint;
		this.ordinal = ordinal;
		this.cardinal = cardinal;
		this.doze = doze;
	}

}
