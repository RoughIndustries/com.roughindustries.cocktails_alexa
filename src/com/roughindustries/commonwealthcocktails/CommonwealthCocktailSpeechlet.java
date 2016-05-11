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
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
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
		Ingredient ingredient = new Ingredient("bourbon whiskey");
		RecipeStep recipeStep = new RecipeStep(ingredient, 0, 2, "ounces");
		recipeSteps.add(recipeStep);
		ingredient = new Ingredient("bourbon whiskey");
		recipeStep = new RecipeStep(ingredient, 0, 2, "ounces");
		recipeSteps.add(recipeStep);
		ingredient = new Ingredient("lemon juice");
		recipeStep = new RecipeStep(ingredient, 1, 1, "ounce");
		recipeSteps.add(recipeStep);
		ingredient = new Ingredient("simple syrup");
		recipeStep = new RecipeStep(ingredient, 2, .5, "ounce");
		recipeSteps.add(recipeStep);
		ingredient = new Ingredient("aromatic bitters");
		recipeStep = new RecipeStep(ingredient, 3, 3, "dashes");
		recipeSteps.add(recipeStep);
		Cocktail cocktail = new Cocktail("whiskey sour",
				"shake all ingredients with ice and strain into ice-filled glass", "lemon slice and cherry on stick",
				"old fasioned glass", recipeSteps);
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
		
		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
			File file = new File(classLoader.getResource("speechAssets/reponsePhrases.stg").getFile());
			
			STGroup group = new STGroupFile(file.getCanonicalPath(), '$', '$');
			ST st = group.getInstanceOf("recipe");

			Slot nameSlot = intent.getSlot(SLOT_NAME);
			String name = nameSlot.getValue().toLowerCase();
			speechOutput = "We don't have a recipe for " + name;
			
			boolean found = false;
			Iterator<Cocktail> cocktail_iter = cocktails.iterator();
			while (cocktail_iter.hasNext() && !found) {
				Cocktail cocktail = cocktail_iter.next();
				if (cocktail.name.contains(name)) {
					st.add("cocktail", cocktail);
					speechOutput = st.render();
					found = true;
				}
			}
//			if (name.contains("whiskey sour")) {
//				//st.add("cocktail", cocktail);
//				//speechOutput = st.render();
//				speechOutput = "to make a " + name
//						+ "<break strength='strong' />  ingredients<break strength='strong' /> two ounces bourbon whiskey<break strength='weak' /> one ounce freshly squeezed lemon juice<break strength='weak' />  half an ounce simple syrup<break strength='weak' />  three dashes of aromatic bitters<break strength='strong' />  garnish<break strength='medium' /> lemon slice and cherry on stick<break strength='strong' />  shake all ingredients with ice and strain into ice-filled glass";
//			} else if (name.contains("margarita on the rocks")) {
//				speechOutput = " to make a " + name
//						+ "<break strength='strong' />  ingredients<break strength='strong' /> <say-as interpret-as=\"fraction\">1+1/2</say-as> ounces tequila<break strength='weak' /> <say-as interpret-as=\"fraction\">3/4</say-as> ounces triple sec<break strength='weak' /> <say-as interpret-as=\"fraction\">3/4</say-as> ounces freshly squeezed lime juice<break strength='weak' /> <say-as interpret-as=\"fraction\">1</say-as> spoonfull agave syrup<break strength='weak' /> <say-as interpret-as=\"fraction\">1/2</say-as> pinch salt<break strength='weak' /> <say-as interpret-as=\"fraction\">1</say-as> dash lavender bitters<break strength='strong' /> garnish<break strength='medium' /> salt rim optional and lime wedge<break strength='strong' /> shake all ingredients with ice and strain into ice-filled glass";
//			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		// Create the plain text output
		SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
		outputSpeech.setSsml("<speak>" + speechOutput + "</speak>");

		return SpeechletResponse.newTellResponse(outputSpeech);
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
			if (cocktail.name.contains(name)) {
				Iterator<RecipeStep> recipeSteps_iter = cocktail.recipeSteps.iterator();
				while (recipeSteps_iter.hasNext() && !found) {
					RecipeStep recipeStep = recipeSteps_iter.next();
					if (recipeStep.ordinal == ((Integer) (session.getAttribute(SESSION_CURRENT_ING_INDEX)))
									.intValue()) {
						speechOutput.append(recipeStep.ingredeint.name);
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
					+ " at this time. Please try again later. Goodbye.</speak>");
			return SpeechletResponse.newTellResponse(output);
		} else if (!found && ((Integer) (session.getAttribute(SESSION_CURRENT_ING_INDEX))).intValue() > 0) {
			// There were no items returned for the specified item.
			SsmlOutputSpeech output = new SsmlOutputSpeech();
			output.setSsml("<speak>There are not more ingredients. Goodbye.</speak>");
			return SpeechletResponse.newTellResponse(output);
		} else {
			speechOutput.append(" Would you like to hear more?");
			return newAskResponse(speechOutput.toString(), true,
					"Would you like to hear more top sellers? Please say yes or no.", false);
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