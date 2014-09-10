package com.meetEverywhere.common;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class DataChangedNotificationService {
	private static List<NotifiableLayoutElement> elements = new ArrayList<NotifiableLayoutElement>();
	
	public static boolean register(NotifiableLayoutElement element){
		if(!elements.contains(element)){
			return elements.add(element);
		}
		return false;
	}
	
	public static void notifyAdapters(){
		for(NotifiableLayoutElement e: elements){
			e.notifyDataChanged();
		}
	}
	
	public static boolean unregister(NotifiableLayoutElement element){
		return elements.remove(element);
	}
	
}
