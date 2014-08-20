package com.meetEverywhere.common;

import java.io.Serializable;

import android.widget.Checkable;

public class Tag implements Checkable, Serializable {

	private static final long serialVersionUID = -1197643809398962299L;
	private final String userID;
	private String name;
	private boolean checked = false;

	/**
	 * 
	 * @param name
	 *            - nazwa, zawartoœæ tagu
	 * @param userID
	 *            - null, gdy tag s³u¿y do wyszukiwania (porównywanie tekstów).
	 *            W p.p. - id zdalnego usera do, którego jest przypisany.
	 */
	public Tag(String name, String userID) {
		this.name = name;
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean arg0) {
		checked = arg0;
	}

	public void toggle() {
		if (checked)
			checked = false;
		else
			checked = true;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o1) {
		if (o1 != null && o1 instanceof Tag) {
			Tag tag = (Tag) o1;
			if (tag.getUserID() == null || getUserID() == null) {
				if (tag.getName().equals(getName())) {
					return true;
				} else {
					return false;
				}
			}
			if (tag.getName().equals(getName())
					&& tag.getUserID().equals(getName())) {
				return true;
			}
		}
		return false;
	}

	public String getUserID() {
		return userID;
	}

}
