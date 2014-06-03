package com.vikki.recipe.transaction;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloader {

	private static final int bitmapCacheSize = 8 * 1024 * 1024;

	public static LruCache<String, Bitmap> bitmapCache = new LruCache<String, Bitmap>(bitmapCacheSize);

	public static final String TAG = ImageDownloader.class.getSimpleName();

	public ImageFetcher imageFetcher;

	public ImageDownloader(ImageFetcher imageFetcher){
		this.imageFetcher = imageFetcher;  
	}

	public void getImageForId(String imageId, ImageView imageView){

		if(TextUtils.isEmpty(imageId) || imageView == null){
			return;
		}

		Bitmap bitmap = bitmapCache.get(imageId);

		if(bitmap == null){
			bitmap = imageFetcher.fetchFromLocal(imageId);

			if(bitmap == null){
				forceDownloadImage(imageId, imageView);
				return;
			}
		}

		if(bitmap != null){
			cancelPotentialDownload(imageId, imageView);
			imageView.setImageBitmap(bitmap);
		}

	}

	public static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView){
		if(imageView == null)
			return null;
		Drawable drawable = imageView.getDrawable();
		if(drawable instanceof ImageHolderDrawable){

			return ((ImageHolderDrawable)drawable).getBitmapDownloaderTask();
		}

		return null;
	}

	public void forceDownloadImage(String imageId, ImageView imageView){

		if(cancelPotentialDownload(imageId, imageView)){
			BitmapDownloaderTask bitmapDownloader = new BitmapDownloaderTask(imageView);
			ImageHolderDrawable drawable = new ImageHolderDrawable(bitmapDownloader);

			imageView.setImageDrawable(drawable);

			bitmapDownloader.execute(imageId);
		}

	}

	private static boolean cancelPotentialDownload(String imageId, ImageView imageView){
		BitmapDownloaderTask bitmapDownloader = getBitmapDownloaderTask(imageView);

		if(bitmapDownloader != null){

			if(bitmapDownloader.imageId == null || (!bitmapDownloader.imageId.equals(imageId))){
				bitmapDownloader.cancel(true);
			}else{

				//Make no sense to cancel the task and to initiate a new task for the same image
				return false;
			}
		}

		return true;
	}


	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap>{

		private String imageId;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapDownloaderTask(ImageView imageView){
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			imageId = params[0];
			Bitmap result = imageFetcher.fetchFromCloud(imageId);
			return result;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(isCancelled()){
				result = null;
				Log.i(TAG, "ImageDownloader task has been cancelled");
			}

			if(result != null){
				bitmapCache.put(imageId, result);
			}

			if(imageViewReference != null){
				ImageView imageView = imageViewReference.get();

				if(this == getBitmapDownloaderTask(imageView)){

					if(result != null){
						imageView.setImageBitmap(result);
					}
				}
			}
		}
	}

	static class ImageHolderDrawable extends ColorDrawable{
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public ImageHolderDrawable(BitmapDownloaderTask bitmapDownloaderTask){
			super(Color.BLACK);
			bitmapDownloaderTaskReference = new WeakReference<ImageDownloader.BitmapDownloaderTask>(
					bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask(){
			return bitmapDownloaderTaskReference.get();
		}
	}

}
