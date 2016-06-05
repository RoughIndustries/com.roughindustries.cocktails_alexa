package com.roughindustries.commonwealthcocktails;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.roughindustries.commonwealthcocktails.model.ccCocktail;
import com.roughindustries.commonwealthcocktails.mybatis.client.CocktailMapper;
import com.roughindustries.commonwealthcocktails.mybatis.client.Component2cocktailMapper;
import com.roughindustries.commonwealthcocktails.mybatis.model.CocktailExample;
import com.roughindustries.commonwealthcocktails.mybatis.model.CocktailWithBLOBs;
import com.roughindustries.commonwealthcocktails.mybatis.model.Component2cocktail;
import com.roughindustries.commonwealthcocktails.mybatis.model.Component2cocktailExample;
import com.roughindustries.utils.NumberToWord;

public class CocktailsUtil {
	private static final Logger log = LoggerFactory.getLogger(CocktailsUtil.class);

	public List<ccCocktail> getCocktails(SqlSession dbsession, String queryFromString) {
		List<CocktailWithBLOBs> initList = null;
		List<ccCocktail> retList = null;
		try {
			CocktailMapper cm = dbsession.getMapper(CocktailMapper.class);
			CocktailExample ce = new CocktailExample();
			List<String> elements = new ArrayList<String>(Arrays.asList(queryFromString.split(" ")));
			for (String element : elements) {
				if (ce.getOredCriteria() != null && ce.getOredCriteria().size() > 0) {
					ce.getOredCriteria().get(0).andCocktailAlternateNameLike("%" + element + "%");
				} else {
					ce.createCriteria().andCocktailAlternateNameLike("%" + element + "%");

				}
			}
			initList = cm.selectByExampleWithBLOBs(ce);
			if(initList != null && initList.size() == 1){
				retList = new ArrayList<ccCocktail>();
				ccCocktail temp = new ccCocktail();
				BeanUtils.copyProperties(temp, initList.get(0));
				Component2cocktailMapper c2cm = dbsession.getMapper(Component2cocktailMapper.class);
				Component2cocktailExample c2ce = new Component2cocktailExample();
				c2ce.createCriteria().andCocktailIdEqualTo(temp.getIdcocktail());
				List<Component2cocktail> comps = c2cm.selectByExample(c2ce);
				
				retList.add(temp);
			} else if (initList != null){
				retList = new ArrayList<ccCocktail>();
				for(CocktailWithBLOBs item : initList){
					ccCocktail temp = new ccCocktail();
					BeanUtils.copyProperties(temp, initList.get(0));
					retList.add(temp);
				}
			}
			// Testing output
			ClassLoader classLoader = this.getClass().getClassLoader();
			File file = new File(classLoader.getResource("speechAssets/reponsePhrases.stg").getFile());

			STGroup group = new STGroupFile(file.getCanonicalPath(), '$', '$');

			ST st = group.getInstanceOf("echoSpeechMultiRecipe");
			st.add("cocktails", retList);
			String speechOutput = st.render();
			log.debug(speechOutput);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return retList;

	}
}
