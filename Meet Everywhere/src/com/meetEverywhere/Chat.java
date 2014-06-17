package com.meetEverywhere;

import android.widget.ListView;

public interface Chat {
	public void sendMessage(String message, ListView listView);
	public void messageReceived(String message);
}
