package com.meetEverywhere.common;

import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;

@SuppressWarnings("rawtypes")
public abstract class AdaptersNotificationService {
	private static List<ArrayAdapter> adapters = new ArrayList<ArrayAdapter>();
	
	public static boolean register(ArrayAdapter adapter){
		return adapters.add(adapter);
	}
	
	public static void notifyAdapters(){
		for(ArrayAdapter a: adapters){
			a.notifyDataSetChanged();
		}
	}
	
	public boolean unregister(ArrayAdapter adapter){
		return adapters.remove(adapter);
	}
	
}
