package com.vikki.recipe.listeners;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikki.recipe.R;
import com.vikki.recipe.RecipeModel;
import com.vikki.recipe.transaction.ImageDownloader;
import com.vikki.recipe.transaction.ImageHandler;
import com.vikki.recipe.transaction.RestURLBuilder;

public class RecipeListAdapter extends BaseAdapter {
	private Context context;
	private int resourceId;
	private List<RecipeModel> recipeModels;
	private ImageDownloader imageDownloader;

	public RecipeListAdapter(Context context, List<RecipeModel> recipeModels, int resourceId) {
		this.context = context;
		this.recipeModels = recipeModels;
		this.resourceId = resourceId;
		this.imageDownloader = new ImageDownloader(new ImageHandler(context, new RestURLBuilder()));
	}

	@Override
	public int getCount() {
		return recipeModels.size();
	}

	@Override
	public Object getItem(int position) {
		if (position < recipeModels.size()) 
			return recipeModels.get(position);
		return null;
	}

	@Override
	public long getItemId(int itemId) {
		return itemId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(resourceId, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RecipeModel recipe = (RecipeModel) getItem(position);
		if (recipe != null) {
			holder.title.setText(recipe.getTitle().trim());
			holder.description.setText(recipe.getIngredients().trim());
			if (!TextUtils.isEmpty(recipe.getThumbnail())) 
				imageDownloader.getImageForId(recipe.getThumbnail(), holder.recipeImage);
		}
		return convertView;
	}

	class ViewHolder {
		public TextView title;
		public ImageView recipeImage;
		public TextView description;
		public ViewHolder(View view) {
			title = (TextView) view.findViewById(R.id.recipe_title);
			description = (TextView) view.findViewById(R.id.recipe_description);
			recipeImage = (ImageView) view.findViewById(R.id.recipe_image);
		}
	}
}