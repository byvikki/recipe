package com.vikki.recipe.transaction;

import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vikki.recipe.RecipeModel;
import com.vikki.recipe.RecipeWrapper;

public class RestRecipeTransaction {

	private RestURLBuilder urlBuilder;
	private Gson gson;
	private static final String RECIPE_API_PATH = "/api";

	public RestRecipeTransaction(RestURLBuilder urlBuilder) {
		this.urlBuilder = urlBuilder; 
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(RecipeWrapper.class, new RecipeWrapperDeserializer());
		gson = builder.create();
	}

	public List<RecipeModel> getRecipes(String query) throws Exception {
		String adParams = null;
		if (query != null) {
			adParams = "i="+query;
		}
		urlBuilder.buildURI(RECIPE_API_PATH, adParams);
		RestClientTask clientTask = new RestClientTask();
		clientTask.setRequestMethod("GET").setPostParams(null)
				.setURLConnection(urlBuilder.getURLConnection());
		
		String response = clientTask.execute().getResponse();
		Log.i("TAGS", response);
		RecipeWrapper recipeModels = gson.fromJson(response, RecipeWrapper.class);
		return recipeModels.getRecipeModel();
	}
}
