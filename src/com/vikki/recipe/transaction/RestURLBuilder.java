package com.vikki.recipe.transaction;

import java.net.URI;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class RestURLBuilder {
	public static final String REST_HOST = "www.recipepuppy.com";
	public static final String REST_SCHEME = "http";
	public static final int REST_PORT = 80;

	private URI uri;

	public void buildURI(String relativePath, String adParams) throws Exception {
		
		uri = new URI(REST_SCHEME, null, REST_HOST, REST_PORT, relativePath, adParams, null);
		Log.i("TAGS", uri.toString());
	}
	
	public void buildURI(String url) throws Exception {
		uri = new URI(url);
	}

	public URLConnection getURLConnection() throws Exception {
		return uri.toURL().openConnection();
	}
}
