package com.vikki.recipe.transaction;

import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageHandler implements ImageFetcher{
	private RestURLBuilder urlBuilder;
	private Context context;
	private int size = 120;

	public ImageHandler(Context context, RestURLBuilder urlBuilder) {
		this.urlBuilder = urlBuilder;
		this.context = context;
	}
	@Override
	public Bitmap fetchFromLocal(String imageLink) {
		// TODO: Local file system cache not done
		return null;
	}

	@Override
	public Bitmap fetchFromCloud(String imageLink) {
		Bitmap result = null;
		try {
			urlBuilder.buildURI(imageLink);
			URLConnection urlConnection = urlBuilder.getURLConnection();
			RestClientTask clientTask = new RestClientTask();
			clientTask.setURLConnection(urlConnection).setPostParams(null)
					.setRequestMethod("GET");
			clientTask.execute();
			Bitmap original = BitmapFactory.decodeStream(clientTask.readInputStream());
			result = Bitmap.createScaledBitmap(original, size, size, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
