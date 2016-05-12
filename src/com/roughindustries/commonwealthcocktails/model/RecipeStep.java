package com.roughindustries.commonwealthcocktails.model;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.atteo.evo.inflector.English;

public class RecipeStep {
	
	public Ingredient ingredient;
	public int ordinal;
	public double cardinal;
	public String speech_cardinal_frac;
	public String text_cardinal_frac;
	public String doze;
	public String pluralized_doze;
	
	public RecipeStep(Ingredient ingredient, int ordinal, double cardinal, String doze) {
		super();
		FractionFormat format = new FractionFormat(); // default format
		this.ingredient = ingredient;
		this.ordinal = ordinal;
		this.cardinal = cardinal;
		long icardinal = (long) cardinal;
		double fcardinal = cardinal - icardinal;
		if(fcardinal != 0){
			if(icardinal > 0){
				this.speech_cardinal_frac = icardinal+"+"+format.format( new Fraction(fcardinal)).replaceAll("\\s+",""); 
				this.text_cardinal_frac = icardinal+" "+format.format( new Fraction(fcardinal)).replaceAll("\\s+","");
			} else {
				this.speech_cardinal_frac = format.format( new Fraction(fcardinal)).replaceAll("\\s+",""); 
				this.text_cardinal_frac = format.format( new Fraction(fcardinal)).replaceAll("\\s+","");

			}
		} else {
			this.speech_cardinal_frac = 
			this.text_cardinal_frac = Long.toString(icardinal).replaceAll("\\s+",""); 

		}
		this.doze = doze;
		this.pluralized_doze = English.plural(doze, (int)Math.ceil(cardinal));
	}

}
