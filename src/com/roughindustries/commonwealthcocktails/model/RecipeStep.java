package com.roughindustries.commonwealthcocktails.model;

public class RecipeStep {
	
	public Cocktail cocktail;
	public Ingredient ingredeint;
	public int ordinal;
	public double cardinal;
	public String doze;
	
	public RecipeStep(Cocktail cocktail, Ingredient ingredeint, int ordinal, double cardinal, String doze) {
		super();
		this.cocktail = cocktail;
		this.ingredeint = ingredeint;
		this.ordinal = ordinal;
		this.cardinal = cardinal;
		this.doze = doze;
	}

}
