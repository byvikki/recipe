package com.vikki.recipe.transaction;

import android.graphics.Bitmap;

public interface ImageFetcher {
	public Bitmap fetchFromLocal(String imageId);

	public Bitmap fetchFromCloud(String imageId);
}
