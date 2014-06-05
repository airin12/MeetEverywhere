package com.meetEverywhere.common;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import android.graphics.Bitmap;

public class ServUser implements Serializable{

	private final String nick;
	private int percentage;
	private int id;
	private Bitmap photo;
	private String description;
	private List<Tag> tags;

	public ServUser(String nick, int percentage, Bitmap image,
			String description, List<Tag> tags, int id) {
		this.nick=nick;
		this.percentage=percentage;
		this.photo=image;
		this.description=description;
		this.tags=tags;
		this.id=id;
	}

	public Bitmap getBitmap() {
		return photo;
	}

	public String getNick() {
		return nick;
	}

	public String getPercentage() {
		return percentage+"";
	}

	
	public static class ServUserComparator implements Comparator<ServUser>
	 {
	     public int compare(ServUser s1, ServUser s2)
	     {
	         return Integer.parseInt(s2.getPercentage())-Integer.parseInt(s1.getPercentage());
	     }
	 }


	public String getDescription() {
		return description;
	}

	public List<Tag> getTags() {
		return tags;
	}
}
