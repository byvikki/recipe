package com.vikki.recipe;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.vikki.recipe.listeners.RecipeListAdapter;
import com.vikki.recipe.transaction.RestRecipeTransaction;
import com.vikki.recipe.transaction.RestURLBuilder;

public class MainActivity extends Activity implements OnQueryTextListener, OnItemClickListener{
	private static RecipeListAdapter recipeAdapter;
	private ProgressDialog progressDialog;
	private ListView listView;
	SearchView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_list);
		listView = (ListView)findViewById(android.R.id.list);
		listView.setOnItemClickListener(this);
		startFetch(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(this);
		return true;
	}

	public void startFetch(String query) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Downloading ..");
		progressDialog.setMessage("fetching recipes from the api");
		progressDialog.setCancelable(false);
		progressDialog.show();
		new FetchRecipe().execute(query);
	}

	public void setListAdapter(List<RecipeModel> recipeModels) {
		recipeAdapter = new RecipeListAdapter(this, recipeModels, R.layout.recipe_list_row);
		getListView().setAdapter(recipeAdapter);
	}

	public ListView getListView() {
		return listView;
	}

	public class FetchRecipe extends AsyncTask<String, Void, List<RecipeModel>> {

		@Override
		protected List<RecipeModel> doInBackground(String... params) {
			List<RecipeModel> recipes = null;
			try {
				RestRecipeTransaction transaction = new RestRecipeTransaction(new RestURLBuilder());
				recipes = transaction.getRecipes(params[0]);
				Log.i("SOME", recipes.size()+ " size of the list");
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("SOME", "error : " + e.toString());
			}
			return recipes;
		}

		@Override
		protected void onPostExecute(List<RecipeModel> result) {
			super.onPostExecute(result);
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (result != null) {
				setListAdapter(result);
			} else {
				Toast.makeText(MainActivity.this, "Some problem reaching the server", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchView.getWindowToken(), 
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
		startFetch(query);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		RecipeModel selectedRecipe = (RecipeModel) recipeAdapter.getItem(position);
		if (selectedRecipe != null) {
			Intent intent = new Intent(this, RecipeActivity.class);
			intent.putExtra(RecipeActivity.SELECTED_RECIPE, (Serializable) selectedRecipe);
			startActivity(intent);
		}
	}
}
