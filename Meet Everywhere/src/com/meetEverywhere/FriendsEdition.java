package com.meetEverywhere;

import java.util.ArrayList;
import java.util.List;

import com.meetEverywhere.common.Tag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FriendsEdition extends Activity {

	private final int PICK_CONTACT = 1;

	private List<String> contacts = new ArrayList<String>();
	private List<Tag> fullContacts = new ArrayList<Tag>();
	private ArrayAdapter<String> adapter;
	private int initialSetContactsNum = 0;

	private ListView listView;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_edition_layout);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, contacts);

		// Getting the reference to the listview object of the layout
		listView = (ListView) findViewById(R.id.listview);

		// Setting adapter to the listview
		listView.setAdapter(adapter);
		for (int i = 0; i < initialSetContactsNum; i++)
			listView.setItemChecked(i, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.friends_edition, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.find:
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, PICK_CONTACT);
			break;
		case R.id.save:
			finish();
			break;
		case R.id.cancel:
			finish();
			break;
		case R.id.selectAll:
			for (int i = 0; i < contacts.size(); i++)
				listView.setItemChecked(i, true);
			break;
		case R.id.deleteAll:
			for (int i = 0; i < contacts.size(); i++)
				listView.setItemChecked(i, false);
			break;
		case R.id.order:
			SparseBooleanArray checkedPos = listView.getCheckedItemPositions();
			int lastChecked = -1;
			for (int i = 0; i < contacts.size(); i++)
				if (checkedPos.get(i)) {
					lastChecked++;
					if (lastChecked != i) {
						String tmpC = contacts.get(lastChecked);
						contacts.remove(lastChecked);
						contacts.add(lastChecked, contacts.get(i - 1));
						contacts.remove(i);
						contacts.add(i, tmpC);

						Tag tmpC2 = fullContacts.get(lastChecked);
						fullContacts.remove(lastChecked);
						fullContacts.add(lastChecked, fullContacts.get(i - 1));
						fullContacts.remove(i);
						fullContacts.add(i, tmpC2);
					}
				}

			adapter.notifyDataSetChanged();

			for (int i = 0; i <= lastChecked; i++)
				listView.setItemChecked(i, true);
			for (int i = lastChecked + 1; i < contacts.size(); i++)
				listView.setItemChecked(i, false);
			break;
		default:
			break;
		}
		return true;
	}
	
}
