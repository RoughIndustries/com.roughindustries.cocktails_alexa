package com.roughindustries.commonwealthcocktails;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommonwealthCocktailSpeechletTest {

	@Test
	public void testGetCocktailRecipeResponse() {
		CommonwealthCocktailSpeechlet ccs = new CommonwealthCocktailSpeechlet();
		ccs.getCocktailRecipeResponse(null, null);
	}

	@Test
	public void testGetCocktailIngredientResponse() {
		fail("Not yet implemented");
	}

}
