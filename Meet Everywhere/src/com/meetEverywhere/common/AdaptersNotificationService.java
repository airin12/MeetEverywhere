package com.meetEverywhere.common;

import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;

@SuppressWarnings("rawtypes")
public abstract class AdaptersNotificationService {
	private static List<ArrayAdapter> adapters = new ArrayList<ArrayAdapter>();
	
	public static boolean register(ArrayAdapter adapter){
		if(!adapters.contains(adapters)){
			return adapters.add(adapter);
		}
		return false;
	}
	
	public static void notifyAdapters(){
		for(ArrayAdapter a: adapters){
			a.notifyDataSetChanged();
		}
	}
	
	public static boolean unregister(ArrayAdapter adapter){
		return adapters.remove(adapter);
	}
	
}
