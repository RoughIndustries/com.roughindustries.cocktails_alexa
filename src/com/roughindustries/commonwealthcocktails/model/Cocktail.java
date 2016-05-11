package com.roughindustries.commonwealthcocktails.model;

import java.util.List;

public class Cocktail {

	public String name;
    public String method;
    public double rating;
    public String garnish;
    public String drinkware;
    public String comment;
    public String aka;
    public String variant;
    public String origin;
    public String source;
    public List<RecipeStep> recipeSteps;

	public Cocktail(String name, String method, String garnish, String drinkware, List<RecipeStep> recipeSteps) {
		super();
		this.name = name;
		this.method = method;
		this.garnish = garnish;
		this.drinkware = drinkware;
		this.recipeSteps = recipeSteps;
	}
}
