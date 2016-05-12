package com.roughindustries.commonwealthcocktails;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazon.speech.ui.StandardCard;
import com.roughindustries.commonwealthcocktails.model.Cocktail;
import com.roughindustries.commonwealthcocktails.model.Ingredient;
import com.roughindustries.commonwealthcocktails.model.RecipeStep;

/**
 * This sample shows how to create a simple speechlet for handling speechlet
 * requests.
 */
public class CommonwealthCocktailSpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(CommonwealthCocktailSpeechlet.class);

	private static final String SLOT_NAME = "name";

	/**
	 * The key to find the current index from the session attributes.
	 */
	private static final String SESSION_CURRENT_ING_INDEX = "ing_index";

	/**
	 * The key to find the current category from the session attributes.
	 */
	private static final String SESSION_CURRENT_CATEGORY = "category";

	/**
	 * The key to find the current cocktail from the session attributes.
	 */
	private static final String SESSION_CURRENT_COCKTAIL = "cocktail";

	private static final List<Cocktail> cocktails = new ArrayList<Cocktail>();

	static {
		List<RecipeStep> recipeSteps = new ArrayList<RecipeStep>();
		Ingredient ingredient = new Ingredient("bourbon whiskey", "Jack Daniels Tennessee Whiskey");
		RecipeStep recipeStep = new RecipeStep(ingredient, 0, 2, "ounce");
		recipeSteps.add(recipeStep);
		ingredient = new Ingredient("lemon juice", null);
		recipeStep = new RecipeStep(ingredient, 1, 1, "ounce");
		recipeSteps.add(recipeStep);
		ingredient = new Ingredient("simple syrup", null);
		recipeStep = new RecipeStep(ingredient, 2, .5, "ounce");
		recipeSteps.add(recipeStep);
		ingredient = new Ingredient("aromatic bitters", null);
		recipeStep = new RecipeStep(ingredient, 3, 3, "dash");
		recipeSteps.add(recipeStep);
		Cocktail cocktail = new Cocktail("Whiskey Sour",
				"Shake all ingredients with ice and strain into an ice filled glass.", "Lemon slice and cherry on a stick.",
				"Old Fashioned Glass", recipeSteps, "https://s3.amazonaws.com/commonwealthcocktailbucket/Voice+003.mp3");
		cocktails.add(cocktail);
		recipeSteps = new ArrayList<RecipeStep>();
		ingredient = new Ingredient("tequila", null);
		recipeStep = new RecipeStep(ingredient, 0, 1.5, "ounce");
		recipeSteps.add(recipeStep);
		ingredient = new Ingredient("triple sec", null);
		recipeStep = new RecipeStep(ingredient, 1, .75, "ounce");
		recipeSteps.add(recipeStep);
		ingredient = new Ingredient("lime juice", null);
		recipeStep = new RecipeStep(ingredient, 2, .75, "ounce");
		recipeSteps.add(recipeStep);
		ingredient = new Ingredient("agave syrup", null);
		recipeStep = new RecipeStep(ingredient, 3, 1, "spoonfull");
		recipeSteps.add(recipeStep);
		ingredient = new Ingredient("salt", null);
		recipeStep = new RecipeStep(ingredient, 4, .5, "pinch");
		recipeSteps.add(recipeStep);
		ingredient = new Ingredient("lavender bitters", null);
		recipeStep = new RecipeStep(ingredient, 5, 1, "dash");
		recipeSteps.add(recipeStep);
		cocktail = new Cocktail("Margarita on the rocks",
				"Shake all ingredients with ice and strain into ice an filled glass.", "Salt rim and lime wedge.",
				"Old Fashioned Glass.", recipeSteps, null);
		cocktails.add(cocktail);
	}

	@Override
	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any initialization logic goes here
	}

	@Override
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		return getWelcomeResponse();
	}

	@Override
	public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		if ("CocktailRecipeIntent".equals(intentName)) {
			return getCocktailRecipeResponse(intent, session);
		} else if ("CocktailIngredientIntent".equals(intentName)) {
			session.setAttribute(SESSION_CURRENT_ING_INDEX, new Integer(0));
			return getCocktailIngredientResponse(intent, session);
		} else if ("HandlePositive".equals(intentName)) {
			if (session.getAttributes().containsKey(SESSION_CURRENT_CATEGORY)) {
				if ("CocktailRecipeIntent".equals(session.getAttribute(SESSION_CURRENT_CATEGORY))) {
					return getCocktailRecipeResponse(intent, session);
				} else if ("CocktailIngredientIntent".equals(session.getAttribute(SESSION_CURRENT_CATEGORY))) {
					return getCocktailIngredientResponse(intent, session);
				} else {
					throw new SpeechletException("Invalid Intent");
				}
			} else {
				throw new SpeechletException("Invalid Intent");
			}
		} else if ("HandleNegative".equals(intentName)) {
			PlainTextOutputSpeech output = new PlainTextOutputSpeech();
			output.setText("");
			return SpeechletResponse.newTellResponse(output);
		} else if ("AMAZON.HelpIntent".equals(intentName)) {
			return getHelpResponse();
		} else {
			throw new SpeechletException("Invalid Intent");
		}
	}

	@Override
	public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any cleanup logic goes here
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getWelcomeResponse() {
		String speechText = "Welcome to the Commonwealth Cocktail Skill, you can say cocktail";

		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Cocktail");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		// Create reprompt
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	/**
	 * Creates a {@code SpeechletResponse} for the cocktail recipe intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	protected SpeechletResponse getCocktailRecipeResponse(Intent intent, Session session) {
		String speechOutput = "";
		String cardOutput = "";
		String cardTitle = "";

		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
			File file = new File(classLoader.getResource("speechAssets/reponsePhrases.stg").getFile());

			STGroup group = new STGroupFile(file.getCanonicalPath(), '$', '$');

			Slot nameSlot = intent.getSlot(SLOT_NAME);
			String name = nameSlot.getValue().toLowerCase();
			speechOutput = "We don't have a recipe for " + name;

			boolean found = false;
			Iterator<Cocktail> cocktail_iter = cocktails.iterator();
			while (cocktail_iter.hasNext() && !found) {
				Cocktail cocktail = cocktail_iter.next();
				if (cocktail.name.toLowerCase().contains(name)) {
					ST st = group.getInstanceOf("echoSpeechRecipe");
					st.add("cocktail", cocktail);
					speechOutput = st.render();
					st = group.getInstanceOf("echoCardRecipe");
					st.add("cocktail", cocktail);
					cardOutput = st.render();
					cardTitle = cocktail.name;
					found = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		StandardCard card = new StandardCard();
		card.setText(cardOutput);
		card.setTitle(cardTitle);

		// Create the plain text output
		SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
		outputSpeech.setSsml("<speak>" + speechOutput + "</speak>");

		SpeechletResponse response = SpeechletResponse.newTellResponse(outputSpeech);
		response.setCard(card);
		return response;
	}

	/**
	 * Creates a {@code SpeechletResponse} for the cocktail ingredient intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	protected SpeechletResponse getCocktailIngredientResponse(Intent intent, Session session) {
		String repromptText = "";

		String name = "";
		if (session.getAttributes().containsKey((SESSION_CURRENT_COCKTAIL))) {
			name = session.getAttribute(SESSION_CURRENT_COCKTAIL).toString();
		} else {
			Slot nameSlot = intent.getSlot(SLOT_NAME);
			name = nameSlot.getValue().toLowerCase();
			session.setAttribute(SESSION_CURRENT_COCKTAIL, name);
		}

		// Configure the card and speech output.
		String cardTitle = "Ingredients for " + name;
		StringBuilder cardOutput = new StringBuilder();
		StringBuilder speechOutput = new StringBuilder();
		if (((Integer) (session.getAttribute(SESSION_CURRENT_ING_INDEX))).intValue() == 0) {
			cardOutput.append("The ingredients for ").append(name).append(" are: ");
			speechOutput.append("Here are the ingredients for ").append(name).append(". ");
		}
		// call this method again when we come back into this session
		session.setAttribute(SESSION_CURRENT_CATEGORY, "CocktailIngredientIntent");

		boolean found = false;
		Iterator<Cocktail> cocktail_iter = cocktails.iterator();
		while (cocktail_iter.hasNext() && !found) {
			Cocktail cocktail = cocktail_iter.next();
			if (cocktail.name.toLowerCase().contains(name)) {
				Iterator<RecipeStep> recipeSteps_iter = cocktail.recipeSteps.iterator();
				while (recipeSteps_iter.hasNext() && !found) {
					RecipeStep recipeStep = recipeSteps_iter.next();
					if (recipeStep.ordinal == ((Integer) (session.getAttribute(SESSION_CURRENT_ING_INDEX)))
							.intValue()) {
						speechOutput.append(recipeStep.ingredient.name);
						int index = ((Integer) (session.getAttribute(SESSION_CURRENT_ING_INDEX))).intValue();
						index++;
						session.setAttribute(SESSION_CURRENT_ING_INDEX, new Integer(index));
						found = true;
					}
				}

			}
		}
		if (!found && ((Integer) (session.getAttribute(SESSION_CURRENT_ING_INDEX))).intValue() == 0) {
			// There were no items returned for the specified item.
			SsmlOutputSpeech output = new SsmlOutputSpeech();
			output.setSsml("<speak>I'm sorry, we do not have a recipe for " + name
					+ " at this time. Please try again later, we are adding more every day.</speak>");
			return SpeechletResponse.newTellResponse(output);
		} else if (!found && ((Integer) (session.getAttribute(SESSION_CURRENT_ING_INDEX))).intValue() > 0) {
			// There were no items returned for the specified item.
			SsmlOutputSpeech output = new SsmlOutputSpeech();
			output.setSsml("<speak>There are no more ingredients.</speak>");
			return SpeechletResponse.newTellResponse(output);
		} else {
			speechOutput.append(" Would you like to hear more?");
			return newAskResponse(speechOutput.toString(), true, "Would you like to hear more? Please say yes or no.",
					false);
		}
	}

	/**
	 * Creates a {@code SpeechletResponse} for the help intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getHelpResponse() {
		String speechText = "You can say cocktail to me!";

		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Cocktail");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		// Create reprompt
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	/**
	 * Wrapper for creating the Ask response from the input strings.
	 * 
	 * @param stringOutput
	 *            the output to be spoken
	 * @param isOutputSsml
	 *            whether the output text is of type SSML
	 * @param repromptText
	 *            the reprompt for if the user doesn't reply or is
	 *            misunderstood.
	 * @param isRepromptSsml
	 *            whether the reprompt text is of type SSML
	 * @return SpeechletResponse the speechlet response
	 */
	private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml, String repromptText,
			boolean isRepromptSsml) {
		OutputSpeech outputSpeech, repromptOutputSpeech;
		if (isOutputSsml) {
			outputSpeech = new SsmlOutputSpeech();
			((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
		} else {
			outputSpeech = new PlainTextOutputSpeech();
			((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
		}

		if (isRepromptSsml) {
			repromptOutputSpeech = new SsmlOutputSpeech();
			((SsmlOutputSpeech) repromptOutputSpeech).setSsml(repromptText);
		} else {
			repromptOutputSpeech = new PlainTextOutputSpeech();
			((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
		}
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromptOutputSpeech);
		return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	}
}