package com.vikki.recipe;

import java.io.Serializable;

public class RecipeModel implements Serializable{
	private static final long serialVersionUID = -3913629036907715099L;
	private String title;
	private String href;
	private String ingredients;
	private String thumbnail;
	
	public RecipeModel() {
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}
