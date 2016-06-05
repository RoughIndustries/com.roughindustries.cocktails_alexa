package com.roughindustries.commonwealthcocktails;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.roughindustries.commonwealthcocktails.model.ccCocktail;

public class CommonwealthCocktailSpeechletTest {

	final static String mybatisConfigFileName = "mybatis/mybatis-config.xml";

	@Test
	public void testGetCocktailRecipeResponse() {
		InputStream inputStream;
		long lStartTime = System.currentTimeMillis();

		try {
			inputStream = Resources.getResourceAsStream(mybatisConfigFileName);

			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			SqlSession dbsession = sqlSessionFactory.openSession();
			// cu.getCocktails(dbsession, "After 8");
			// cu.getCocktails(dbsession, "After Eight");
			// cu.getCocktails(dbsession, "Whiskey Sour");
			ccCocktail.getCocktails(dbsession, "margarita on the rocks");
			// cu.getCocktails(dbsession, "margarita");
			// cu.getCocktails(dbsession, "Eighteen 97");
			// cu.getCocktails(dbsession, "1897");
			// cu.getCocktails(dbsession, "Ricaço");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long lEndTime = System.currentTimeMillis();
		long difference = lEndTime - lStartTime;
		System.out.println("Elapsed seconds: " + difference / 1000);
	}

	@Test
	public void testGetCocktailIngredientResponse() {
		fail("Not yet implemented");
	}

}
