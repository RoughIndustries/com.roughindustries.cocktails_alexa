package com.roughindustries.commonwealthcocktails.model;

public class Ingredient {

	public String name;
	public boolean use_sponsored_name = false;
	public String sponsored_name = "";

	public Ingredient(String name, String sponsored_name) {
		super();
		this.name = name;
		if(sponsored_name != null){
			this.use_sponsored_name = true;
			this.sponsored_name = sponsored_name;
		} else {
			this.use_sponsored_name = false;
			this.sponsored_name = "";
		}
	}
}
