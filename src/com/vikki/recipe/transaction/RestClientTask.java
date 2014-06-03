package com.vikki.recipe.transaction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;

public class RestClientTask {
	private HttpURLConnection urlConnection;
	private String postParams;
	private String requestMethod;

	public RestClientTask() {
		
	}
	
	public RestClientTask setURLConnection(URLConnection urlConnection) {
		this.urlConnection = (HttpURLConnection) urlConnection;
		return this;
	}

	public RestClientTask setPostParams(String params) {
		this.postParams = params;
		return this;
	}

	public RestClientTask setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
		return this;
	}
	
	public RestClientTask execute() throws Exception {
		urlConnection.setRequestMethod(requestMethod);
		if (postParams != null) {
			urlConnection.setDoOutput(true);
			OutputStream dataStream = urlConnection.getOutputStream();
			dataStream.write(postParams.getBytes());
			dataStream.flush();
			dataStream.close();
		}
		return this;
	}

	public InputStream readInputStream() throws Exception {
		return urlConnection.getInputStream();
	}

	public String getResponse() throws Exception {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		if (isResponseAvailable()) {
			String read;
			while ( (read = reader.readLine()) != null) {
				builder.append(read);
			}
		}
		return builder.toString();
	}
	
	public boolean isResponseAvailable() throws Exception {
		return urlConnection.getResponseCode() == 200;
	}
}