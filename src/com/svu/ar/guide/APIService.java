/*
 ********** SVU **********
 ********** Barea_27786 **********
 *********** Handle JSON Class **********
 *********** API Connect to fetch Json Object and parse Result **********
 */

package com.svu.ar.guide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;

import android.annotation.SuppressLint;

public class APIService {

	private static final String API_URL = "http://virtimg.somee.com/api/Objct";

	public volatile boolean parsingComplete = true;

	public static Item getItemById(String id) {
		URL url;
		String json = "";
		try {
			// send call to api
			url = new URL(API_URL + "/" + id);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			InputStream stream = conn.getInputStream();
			json = convertStreamToString(stream);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ItemBuilder.build(json);
	}


	private static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
