package com.meetEverywhere;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class ManageContactsActivity extends TabActivity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_contacts_layout);
 
		//Resources ressources = getResources(); 
		TabHost tabHost = getTabHost(); 
 
		Intent intentFriendsList = new Intent().setClass(this, FriendsListFragment.class);
		TabSpec friendsListTab = tabHost
		  .newTabSpec("Znajomi")
		  .setIndicator("Znajomi")
		  .setContent(intentFriendsList);
 
		Intent intentBlockedList = new Intent().setClass(this, BlockedContactsActivity.class);
		TabSpec blockedListTab = tabHost
		  .newTabSpec("Blokowane")
		  .setIndicator("Blokowane")
		  .setContent(intentBlockedList);
		
		Intent invSentList = new Intent().setClass(this, InvitationsSentActivity.class);
		TabSpec invSentTab = tabHost
		  .newTabSpec("Wys³ane")
		  .setIndicator("Wys³ane zaproszenia")
		  .setContent(invSentList);
		
		Intent invRecvList = new Intent().setClass(this, InvitationsReceivedActivity.class);
		TabSpec invRecvTab = tabHost
		  .newTabSpec("Otrzymane")
		  .setIndicator("Otrzymane zaproszenia")
		  .setContent(invRecvList);
		
		// add all tabs 
		tabHost.addTab(friendsListTab);
		tabHost.addTab(blockedListTab);
		tabHost.addTab(invSentTab);
		tabHost.addTab(invRecvTab);

		TextView x = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
	    x.setTextSize(10);
	    x.setTextColor(Color.BLACK);
	    
	    x = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
	    x.setTextSize(7);
	    x.setTextColor(Color.BLACK);

	    
	    x = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
	    x.setTextSize(7);
	    x.setTextColor(Color.BLACK);

	    
	    x = (TextView) tabHost.getTabWidget().getChildAt(3).findViewById(android.R.id.title);
	    x.setTextSize(7);
	    x.setTextColor(Color.BLACK);

	    
		//set Windows tab as default (zero based)
		tabHost.setCurrentTab(0);
	}

}
