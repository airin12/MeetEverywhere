package com.meetEverywhere.common;

import java.io.Serializable;

import android.widget.Checkable;

public class Tag implements Checkable, Serializable{

	private String name;
	private boolean checked=false;
	
	public Tag(String name){
		this.name = name;
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
		checked=arg0;
		
	}

	public void toggle() {
		if(checked)
			checked=false;
		else
			checked=true;
		
	}
	
	@Override
	public String toString(){
		return name;
	}
	
}
