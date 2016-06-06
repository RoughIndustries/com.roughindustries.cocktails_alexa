package com.roughindustries.commonwealthcocktails;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.roughindustries.commonwealthcocktails.model.ccCocktail;

public class CommonwealthCocktailSpeechletTest {

	final static String mybatisConfigFileName = "mybatis/mybatis-config.xml";

	@Test
	public void testGetCocktailRecipeResponse() {
		InputStream inputStream;
		long lStartTime = System.currentTimeMillis();
		SqlSession dbsession = null;
		List<ccCocktail> cocktailList = null;
		try {
			inputStream = Resources.getResourceAsStream(mybatisConfigFileName);

			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			dbsession = sqlSessionFactory.openSession();
			// cu.getCocktails(dbsession, "After 8");
			// cu.getCocktails(dbsession, "After Eight");
			// cu.getCocktails(dbsession, "Whiskey Sour");
			//cocktailList = ccCocktail.getCocktails(dbsession, "margarita on the rocks");
			// cu.getCocktails(dbsession, "margarita");
			// cu.getCocktails(dbsession, "Eighteen 97");
			// cu.getCocktails(dbsession, "1897");
			cocktailList = ccCocktail.getCocktails(dbsession, "Ricaço");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (dbsession != null) {
				dbsession.close();
				dbsession = null;
			}
		}
		ClassLoader classLoader = this.getClass().getClassLoader();
		File file = new File(classLoader.getResource("speechAssets/reponsePhrases.stg").getFile());

		STGroup group = null;
		try {
			group = new STGroupFile(file.getCanonicalPath(), '$', '$');
		} catch (IOException e) {
			e.printStackTrace();
		}

		ST st = group.getInstanceOf("echoSpeechRecipe");
		st.add("cocktail", cocktailList.get(0));
		String speechOutput = st.render();
		System.out.println(speechOutput);
		long lEndTime = System.currentTimeMillis();
		long difference = lEndTime - lStartTime;
		System.out.println("Elapsed seconds: " + difference / 1000);
	}

	@Test
	public void testGetCocktailIngredientResponse() {
		fail("Not yet implemented");
	}

}
