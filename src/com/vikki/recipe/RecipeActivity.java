package com.vikki.recipe;

import com.vikki.recipe.transaction.ImageDownloader;
import com.vikki.recipe.transaction.ImageHandler;
import com.vikki.recipe.transaction.RestURLBuilder;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeActivity extends Activity {
	private TextView title;
	private ImageView recipeImage;
	private TextView description;
	private RecipeModel selectedRecipe;
	private ImageDownloader imageDownloader;
	public static final String SELECTED_RECIPE = "selected_recipe";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);
		this.imageDownloader = new ImageDownloader(new ImageHandler(this, new RestURLBuilder()));
		title = (TextView) findViewById(R.id.recipe_title);
		description = (TextView) findViewById(R.id.recipe_description);
		recipeImage = (ImageView) findViewById(R.id.recipe_image);
		selectedRecipe = (RecipeModel) getIntent().getSerializableExtra(SELECTED_RECIPE);
		constructView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	public void constructView() {
		getActionBar().setTitle(selectedRecipe.getTitle());
		title.setText(selectedRecipe.getTitle());
		imageDownloader.getImageForId(selectedRecipe.getThumbnail(), recipeImage);
		description.setText(selectedRecipe.getIngredients());
	}
}
