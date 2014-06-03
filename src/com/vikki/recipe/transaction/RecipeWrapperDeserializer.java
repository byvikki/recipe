package com.vikki.recipe.transaction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.vikki.recipe.RecipeModel;
import com.vikki.recipe.RecipeWrapper;

public class RecipeWrapperDeserializer implements JsonDeserializer<RecipeWrapper> {
	
	public RecipeWrapperDeserializer() {
		
	}

	@Override
	public RecipeWrapper deserialize(JsonElement element, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		JsonElement jsonRecipes = element.getAsJsonObject().get("results");
		if(!jsonRecipes.isJsonArray())
			return null;
		JsonArray jsonArray = jsonRecipes.getAsJsonArray();
		List<RecipeModel> result = new ArrayList<RecipeModel>();
		Gson gson = new Gson();
		for (JsonElement jsonEntry: jsonArray) {
			RecipeModel recipe = gson.fromJson(jsonEntry, RecipeModel.class);
			result.add(recipe);
		}
		RecipeWrapper wrapper = new RecipeWrapper();
		wrapper.setRecipeModel(result);
		return wrapper;
	}

}
