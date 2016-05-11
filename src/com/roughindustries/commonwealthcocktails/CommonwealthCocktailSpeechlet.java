package com.roughindustries.commonwealthcocktails;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;

/**
 * This sample shows how to create a simple speechlet for handling speechlet
 * requests.
 */
public class CommonwealthCocktailSpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(CommonwealthCocktailSpeechlet.class);

	private static final String SLOT_NAME = "name";

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

		if ("CocktailIntent".equals(intentName)) {
			return getCocktailResponse(intent, session);
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
	 * Creates a {@code SpeechletResponse} for the cocktail intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	protected SpeechletResponse getCocktailResponse(Intent intent, Session session) {
		String speechOutput = "We don't have a recipe for ";

		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("ST/recipe.st").getFile());
			
			Scanner scan = new Scanner(file);
			String content = scan.useDelimiter("\\Z").next();
			scan.close();
			
			Slot nameSlot = intent.getSlot(SLOT_NAME);
			String name = nameSlot.getValue().toLowerCase();
			speechOutput += name;

			if (name.contains("whiskey sour")) {
				speechOutput = "to make a " + name
						+ "<break strength='strong' />  ingredients<break strength='strong' /> two ounces bourbon whiskey<break strength='weak' /> one ounce freshly squeezed lemon juice<break strength='weak' />  half an ounce simple syrup<break strength='weak' />  three dashes of aromatic bitters<break strength='strong' />  garnish<break strength='medium' /> lemon slice and cherry on stick<break strength='strong' />  shake all ingredients with ice and strain into ice-filled glass";
			} else if (name.contains("margarita on the rocks")) {
				speechOutput = " to make a " + name
						+ "<break strength='strong' />  ingredients<break strength='strong' /> <say-as interpret-as=\"fraction\">1+1/2</say-as> ounces tequila<break strength='weak' /> <say-as interpret-as=\"fraction\">3/4</say-as> ounces triple sec<break strength='weak' /> <say-as interpret-as=\"fraction\">3/4</say-as> ounces freshly squeezed lime juice<break strength='weak' /> <say-as interpret-as=\"fraction\">1</say-as> spoonfull agave syrup<break strength='weak' /> <say-as interpret-as=\"fraction\">1/2</say-as> pinch salt<break strength='weak' /> <say-as interpret-as=\"fraction\">1</say-as> dash lavender bitters<break strength='strong' /> garnish<break strength='medium' /> salt rim optional and lime wedge<break strength='strong' /> shake all ingredients with ice and strain into ice-filled glass";
			}

			// Create the plain text output

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
		outputSpeech.setSsml("<speak>" + speechOutput + "</speak>");
		return SpeechletResponse.newTellResponse(outputSpeech);
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
}