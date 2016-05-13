package com.roughindustries.commonwealthcocktails.mybatis.model;

import java.util.ArrayList;
import java.util.List;

public class ArticleExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public ArticleExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andIdarticleIsNull() {
			addCriterion("idarticle is null");
			return (Criteria) this;
		}

		public Criteria andIdarticleIsNotNull() {
			addCriterion("idarticle is not null");
			return (Criteria) this;
		}

		public Criteria andIdarticleEqualTo(Integer value) {
			addCriterion("idarticle =", value, "idarticle");
			return (Criteria) this;
		}

		public Criteria andIdarticleNotEqualTo(Integer value) {
			addCriterion("idarticle <>", value, "idarticle");
			return (Criteria) this;
		}

		public Criteria andIdarticleGreaterThan(Integer value) {
			addCriterion("idarticle >", value, "idarticle");
			return (Criteria) this;
		}

		public Criteria andIdarticleGreaterThanOrEqualTo(Integer value) {
			addCriterion("idarticle >=", value, "idarticle");
			return (Criteria) this;
		}

		public Criteria andIdarticleLessThan(Integer value) {
			addCriterion("idarticle <", value, "idarticle");
			return (Criteria) this;
		}

		public Criteria andIdarticleLessThanOrEqualTo(Integer value) {
			addCriterion("idarticle <=", value, "idarticle");
			return (Criteria) this;
		}

		public Criteria andIdarticleIn(List<Integer> values) {
			addCriterion("idarticle in", values, "idarticle");
			return (Criteria) this;
		}

		public Criteria andIdarticleNotIn(List<Integer> values) {
			addCriterion("idarticle not in", values, "idarticle");
			return (Criteria) this;
		}

		public Criteria andIdarticleBetween(Integer value1, Integer value2) {
			addCriterion("idarticle between", value1, value2, "idarticle");
			return (Criteria) this;
		}

		public Criteria andIdarticleNotBetween(Integer value1, Integer value2) {
			addCriterion("idarticle not between", value1, value2, "idarticle");
			return (Criteria) this;
		}

		public Criteria andArticleTitleIsNull() {
			addCriterion("article_title is null");
			return (Criteria) this;
		}

		public Criteria andArticleTitleIsNotNull() {
			addCriterion("article_title is not null");
			return (Criteria) this;
		}

		public Criteria andArticleTitleEqualTo(String value) {
			addCriterion("article_title =", value, "articleTitle");
			return (Criteria) this;
		}

		public Criteria andArticleTitleNotEqualTo(String value) {
			addCriterion("article_title <>", value, "articleTitle");
			return (Criteria) this;
		}

		public Criteria andArticleTitleGreaterThan(String value) {
			addCriterion("article_title >", value, "articleTitle");
			return (Criteria) this;
		}

		public Criteria andArticleTitleGreaterThanOrEqualTo(String value) {
			addCriterion("article_title >=", value, "articleTitle");
			return (Criteria) this;
		}

		public Criteria andArticleTitleLessThan(String value) {
			addCriterion("article_title <", value, "articleTitle");
			return (Criteria) this;
		}

		public Criteria andArticleTitleLessThanOrEqualTo(String value) {
			addCriterion("article_title <=", value, "articleTitle");
			return (Criteria) this;
		}

		public Criteria andArticleTitleLike(String value) {
			addCriterion("article_title like", value, "articleTitle");
			return (Criteria) this;
		}

		public Criteria andArticleTitleNotLike(String value) {
			addCriterion("article_title not like", value, "articleTitle");
			return (Criteria) this;
		}

		public Criteria andArticleTitleIn(List<String> values) {
			addCriterion("article_title in", values, "articleTitle");
			return (Criteria) this;
		}

		public Criteria andArticleTitleNotIn(List<String> values) {
			addCriterion("article_title not in", values, "articleTitle");
			return (Criteria) this;
		}

		public Criteria andArticleTitleBetween(String value1, String value2) {
			addCriterion("article_title between", value1, value2, "articleTitle");
			return (Criteria) this;
		}

		public Criteria andArticleTitleNotBetween(String value1, String value2) {
			addCriterion("article_title not between", value1, value2, "articleTitle");
			return (Criteria) this;
		}

		public Criteria andArticleContentIsNull() {
			addCriterion("article_content is null");
			return (Criteria) this;
		}

		public Criteria andArticleContentIsNotNull() {
			addCriterion("article_content is not null");
			return (Criteria) this;
		}

		public Criteria andArticleContentEqualTo(String value) {
			addCriterion("article_content =", value, "articleContent");
			return (Criteria) this;
		}

		public Criteria andArticleContentNotEqualTo(String value) {
			addCriterion("article_content <>", value, "articleContent");
			return (Criteria) this;
		}

		public Criteria andArticleContentGreaterThan(String value) {
			addCriterion("article_content >", value, "articleContent");
			return (Criteria) this;
		}

		public Criteria andArticleContentGreaterThanOrEqualTo(String value) {
			addCriterion("article_content >=", value, "articleContent");
			return (Criteria) this;
		}

		public Criteria andArticleContentLessThan(String value) {
			addCriterion("article_content <", value, "articleContent");
			return (Criteria) this;
		}

		public Criteria andArticleContentLessThanOrEqualTo(String value) {
			addCriterion("article_content <=", value, "articleContent");
			return (Criteria) this;
		}

		public Criteria andArticleContentLike(String value) {
			addCriterion("article_content like", value, "articleContent");
			return (Criteria) this;
		}

		public Criteria andArticleContentNotLike(String value) {
			addCriterion("article_content not like", value, "articleContent");
			return (Criteria) this;
		}

		public Criteria andArticleContentIn(List<String> values) {
			addCriterion("article_content in", values, "articleContent");
			return (Criteria) this;
		}

		public Criteria andArticleContentNotIn(List<String> values) {
			addCriterion("article_content not in", values, "articleContent");
			return (Criteria) this;
		}

		public Criteria andArticleContentBetween(String value1, String value2) {
			addCriterion("article_content between", value1, value2, "articleContent");
			return (Criteria) this;
		}

		public Criteria andArticleContentNotBetween(String value1, String value2) {
			addCriterion("article_content not between", value1, value2, "articleContent");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table article
	 * @mbggenerated  Fri May 13 13:15:11 CDT 2016
	 */
	public static class Criterion {
		private String condition;
		private Object value;
		private Object secondValue;
		private boolean noValue;
		private boolean singleValue;
		private boolean betweenValue;
		private boolean listValue;
		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}

	/**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table article
     *
     * @mbggenerated do_not_delete_during_merge Fri May 13 12:51:23 CDT 2016
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
}