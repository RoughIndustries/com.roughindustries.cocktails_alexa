package com.roughindustries.commonwealthcocktails;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.amazon.speech.slu.Intent;

public class CommonwealthCocktailSpeechletTest {

	@Test
	public void testGetCocktailResponse() {
		CommonwealthCocktailSpeechlet ccs = new CommonwealthCocktailSpeechlet();
		ccs.getCocktailResponse(null, null);
		fail("Not yet implemented");
	}

}
