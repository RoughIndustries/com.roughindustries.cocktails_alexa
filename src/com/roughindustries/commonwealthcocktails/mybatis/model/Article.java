package com.roughindustries.commonwealthcocktails.mybatis.model;

public class Article {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column article.idarticle
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	private Integer idarticle;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column article.article_title
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	private String articleTitle;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column article.article_content
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	private String articleContent;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column article.idarticle
	 * @return  the value of article.idarticle
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public Integer getIdarticle() {
		return idarticle;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column article.idarticle
	 * @param idarticle  the value for article.idarticle
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public void setIdarticle(Integer idarticle) {
		this.idarticle = idarticle;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column article.article_title
	 * @return  the value of article.article_title
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public String getArticleTitle() {
		return articleTitle;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column article.article_title
	 * @param articleTitle  the value for article.article_title
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column article.article_content
	 * @return  the value of article.article_content
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public String getArticleContent() {
		return articleContent;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column article.article_content
	 * @param articleContent  the value for article.article_content
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}
}