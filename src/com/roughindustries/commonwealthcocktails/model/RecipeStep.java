package com.roughindustries.commonwealthcocktails.model;

public class RecipeStep {
	
	public Ingredient ingredient;
	public int ordinal;
	public double cardinal;
	public String doze;
	
	public RecipeStep(Ingredient ingredient, int ordinal, double cardinal, String doze) {
		super();
		this.ingredient = ingredient;
		this.ordinal = ordinal;
		this.cardinal = cardinal;
		this.doze = doze;
	}

}
