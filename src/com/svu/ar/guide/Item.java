package com.svu.ar.guide;


public class Item {

	private String Name;

	private String Description;
	
	private static Item item;

	private Item() {
	}
	
	public static Item getInstance() {
		if (item == null)
			item = new Item();
		return item;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		this.Description = description;
	}
}
