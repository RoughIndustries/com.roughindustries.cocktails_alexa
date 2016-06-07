package com.roughindustries.commonwealthcocktails;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
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
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.roughindustries.commonwealthcocktails.model.ccCocktail;
import com.roughindustries.commonwealthcocktails.model.ccComponent;
import com.roughindustries.commonwealthcocktails.model.ccRecipe;
import com.roughindustries.commonwealthcocktails.model.ccStyle;
import com.roughindustries.commonwealthcocktails.mybatis.client.CocktailMapper;
import com.roughindustries.commonwealthcocktails.mybatis.model.Cocktail;
import com.roughindustries.commonwealthcocktails.mybatis.model.CocktailExample;

/**
 * This sample shows how to create a simple speechlet for handling speechlet
 * requests.
 */
public class CommonwealthCocktailSpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(CommonwealthCocktailSpeechlet.class);

	private static final String SLOT_NAME = "name";

	final static String mybatisConfigFileName = "mybatis/mybatis-config.xml";
	final static String yamlCocktailFileName = "data/cocktails.yml";

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

	/**
	 * The key to find the current sql factory from the session attributes.
	 */
	private static final String SESSION_SQL_FACTORY = "cocktail";

	private static final List<ccCocktail> cocktails = new ArrayList<ccCocktail>();

	@SuppressWarnings("unchecked")
	public ArrayList<ccCocktail> readYamlFromFile(String filename) {
		Object object = null;
		try {
			File file = Resources.getResourceAsFile(filename);
			YamlReader reader = new YamlReader(new FileReader(file));
			reader.getConfig().setClassTag("cockatil", ccCocktail.class);
			reader.getConfig().setClassTag("recipeStep", ccRecipe.class);
			reader.getConfig().setClassTag("component", ccComponent.class);
			reader.getConfig().setClassTag("style", ccStyle.class);
			object = reader.read();
			System.out.println(object);
			reader.close();
		} catch (YamlException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (ArrayList<ccCocktail>) object;
	}

	@Override
	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		try {
			InputStream inputStream = Resources.getResourceAsStream(mybatisConfigFileName);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			session.setAttribute(SESSION_SQL_FACTORY, sqlSessionFactory);
			inputStream.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
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

		SqlSessionFactory ssf = (SqlSessionFactory) session.getAttribute(SESSION_SQL_FACTORY);
		SqlSession dbsession = ssf.openSession();
		log.info("DB Session " + dbsession);

		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
			File file = new File(classLoader.getResource("speechAssets/reponsePhrases.stg").getFile());

			STGroup group = new STGroupFile(file.getCanonicalPath(), '$', '$');

			Slot nameSlot = intent.getSlot(SLOT_NAME);
			String name = nameSlot.getValue().toLowerCase();
			speechOutput = "We don't have a recipe for " + name;

			boolean found = false;
			// Iterator<ccCocktail> cocktail_iter = cocktails.iterator();
			// while (cocktail_iter.hasNext() && !found) {
			// ccCocktail cocktail = cocktail_iter.next();
			// if (cocktail.getCocktailName().toLowerCase().contains(name)) {
			// ST st = group.getInstanceOf("echoSpeechRecipe");
			// st.add("cocktail", cocktail);
			// speechOutput = st.render();
			// st = group.getInstanceOf("echoCardRecipe");
			// st.add("cocktail", cocktail);
			// cardOutput = st.render();
			// cardTitle = cocktail.getCocktailName();
			// found = true;
			// }
			// }
			List<ccCocktail> list = ccCocktail.getCocktails(dbsession, name);
			if (list != null && list.size() == 1) {
				Cocktail cocktail = list.get(0);
				ST st = group.getInstanceOf("echoSpeechRecipe");
				st.add("cocktail", cocktail);
				speechOutput = st.render();
				// st = group.getInstanceOf("echoCardRecipe");
				// st.add("cocktail", cocktail);
				// cardOutput = st.render();
				// cardTitle = cocktail.getCocktailName();
				found = true;
			} else if (list != null && 
					list.size() > 1){
				//tell user we have a couple of options and they should pick
				ST st = group.getInstanceOf("echoSpeechMultiRecipe");
				st.add("cocktails", list);
				speechOutput = st.render();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (dbsession != null) {
				dbsession.close();
				dbsession = null;
			}
		}

		// StandardCard card = new StandardCard();
		// card.setText(cardOutput);
		// card.setTitle(cardTitle);

		// Create the plain text output
		SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
		outputSpeech.setSsml("<speak>" + speechOutput + "</speak>");

		SpeechletResponse response = SpeechletResponse.newTellResponse(outputSpeech);
		// response.setCard(card);
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
		Iterator<ccCocktail> cocktail_iter = cocktails.iterator();
		while (cocktail_iter.hasNext() && !found) {
			ccCocktail cocktail = cocktail_iter.next();
			if (cocktail.getCocktailName().toLowerCase().contains(name)) {
				Iterator<ccRecipe> recipeSteps_iter = cocktail.recipeSteps.iterator();
				while (recipeSteps_iter.hasNext() && !found) {
					ccRecipe recipeStep = recipeSteps_iter.next();
					if (recipeStep.getRecipeOrdinal() == ((Integer) (session.getAttribute(SESSION_CURRENT_ING_INDEX)))
							.intValue()) {
						speechOutput.append(recipeStep.getIngredient().getComponentName());
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