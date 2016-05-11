package com.roughindustries.commonwealthcocktails.model;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;

public class RecipeStep {
	
	public Ingredient ingredient;
	public int ordinal;
	public double cardinal;
	public String cardinal_frac;
	public String doze;
	
	public RecipeStep(Ingredient ingredient, int ordinal, double cardinal, String doze) {
		super();
		FractionFormat format = new FractionFormat(); // default format
		this.ingredient = ingredient;
		this.ordinal = ordinal;
		this.cardinal = cardinal;
		this.cardinal_frac = format.format( new Fraction(this.cardinal)); 
		this.doze = doze;
	}

}
