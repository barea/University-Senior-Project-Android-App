package com.svu.ar.guide;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemBuilder {


	public static final String NAME_ATTR = "Name";
	public static final String DESCRIPTION_ATTR = "Description";
	
	private ItemBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public static Item build(String json) {
		JSONObject reader;
		try {
			reader = new JSONObject(json);
			Item.getInstance().setName(reader.getString(NAME_ATTR));
			Item.getInstance().setDescription(reader.getString(DESCRIPTION_ATTR));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Item.getInstance();
	}
	
	
}
