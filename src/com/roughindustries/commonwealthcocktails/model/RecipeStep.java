package com.roughindustries.commonwealthcocktails.model;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionFormat;
import org.atteo.evo.inflector.English;

public class RecipeStep {
	
	public Ingredient ingredient;
	public int ordinal;
	public double cardinal;
	public String echo_cardinal_frac;
	public String doze;
	public String cardinal_doze;
	
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
				this.echo_cardinal_frac = icardinal+"+"+format.format( new Fraction(fcardinal)).replaceAll("\\s+",""); 
			} else {
				this.echo_cardinal_frac = format.format( new Fraction(fcardinal)).replaceAll("\\s+",""); 
			}
		} else {
			this.echo_cardinal_frac = Long.toString(icardinal).replaceAll("\\s+",""); 
		}
		this.doze = doze;
		this.cardinal_doze = English.plural(doze, (int)Math.ceil(cardinal));
	}

}
