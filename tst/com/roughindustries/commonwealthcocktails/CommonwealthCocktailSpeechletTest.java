package com.roughindustries.commonwealthcocktails;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

public class CommonwealthCocktailSpeechletTest {

	final static String mybatisConfigFileName = "mybatis/mybatis-config.xml";

	@Test
	public void testGetCocktailRecipeResponse() {
		InputStream inputStream;
		try {
			inputStream = Resources.getResourceAsStream(mybatisConfigFileName);

			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			SqlSession dbsession = sqlSessionFactory.openSession();
			CocktailsUtil cu = new CocktailsUtil();
			//cu.getCocktails(dbsession, "After 8");
			//cu.getCocktails(dbsession, "After Eight");
			//cu.getCocktails(dbsession, "Whiskey Sour");
			cu.getCocktails(dbsession, "margarita on the rocks");
			//cu.getCocktails(dbsession, "margarita");
			//cu.getCocktails(dbsession, "Eighteen 97");
			//cu.getCocktails(dbsession, "1897");
			//cu.getCocktails(dbsession, "Ricaço");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetCocktailIngredientResponse() {
		fail("Not yet implemented");
	}

}
