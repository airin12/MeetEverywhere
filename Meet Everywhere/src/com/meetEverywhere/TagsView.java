package com.meetEverywhere;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public abstract class TagsView extends Activity {

	private ListView tagListView;
	private List<Tag> tags = new ArrayList<Tag>();
	private List<Tag> deletedTags = new ArrayList<Tag>();
	private ArrayAdapter<Tag> listAdapter;

	private DatabaseAdapter myDBAdapter;

	private List<Tag> getTagsFromDatabase() {
		List<Tag> list = new ArrayList<Tag>();

		Cursor dbCursor = myDBAdapter
				.getAllTags(DatabaseAdapter.TagType.SEARCH);
		startManagingCursor(dbCursor);
		dbCursor.requery();

		if (dbCursor.moveToFirst()) {
			do {
				int id = dbCursor.getInt(myDBAdapter.ID_COLUMN);
				String tag = dbCursor.getString(myDBAdapter.TAG_COLUMN);
				int checkedInt = dbCursor.getInt(myDBAdapter.ACTIVE_TAG_COLUMN);

				// list.add(new Contact(id, tag, checkedInt != 0,
				// Contact.Status.SAVED, false));
			} while (dbCursor.moveToNext());
		}

		return list;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_tags_edition_layout);

		myDBAdapter = new DatabaseAdapter(getApplicationContext());
		myDBAdapter.open();

		tagListView = (ListView) findViewById(R.id.tagsListView);
		tagListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		tags = getTagsFromDatabase();
		// listAdapter = new MyCustomAdapter(this,
		// R.layout.content_info,(ArrayList<Contact>) tags);
		
		tagListView.setAdapter(listAdapter);
		putTagsIntoList(tags);

		registerForContextMenu(tagListView);
	}

	@Override
	protected void onDestroy() {
		Toast.makeText(getApplicationContext(), "Czy zapisaæ?",
				Toast.LENGTH_LONG).show();

		myDBAdapter.close();
		super.onDestroy();
	}

	public void backToMainMenu(View view) {
		finish();
	}

	public void addTags(View view) {
		EditText editText = (EditText) findViewById(R.id.tagsTextField);
		String message = editText.getText().toString();

		if (TextUtils.isEmpty(message)) {
			editText.setError(getString(R.string.error_tags_required));
			editText.requestFocus();
			return;
		}

		String[] newTags = parseInput(message);

		for(String string : newTags){
			Log.d("parsowanie",string);
		}
		
		for (String tag : newTags) {
			if (!TextUtils.isEmpty(tag) && !tagsListContains(tag))
				tags.add(new Tag(tag));
		}

		putTagsIntoList(tags);
	}

	private void putTagsIntoList(List<Tag> tags2) {
		listAdapter = new MyCustomAdapter(this, R.layout.content_info,
				(ArrayList<Tag>) tags2);

		// listAdapter = new ArrayAdapter<String>(getApplicationContext(),
		// android.R.layout.simple_list_item_1, tags);
		tagListView.setAdapter(listAdapter);
		// listAdapter.notifyDataSetChanged();

	}

	private boolean tagsListContains(String tag) {

		for (Tag contact : tags) {
			if (contact.getName().equals(tag))
				return true;
		}

		return false;
	}

	public abstract void saveTags(View view);

	private String[] parseInput(String message) {
		String[] tags = message.split(",");
		for (int i = 0; i < tags.length; i++) {
			tags[i] = tags[i].trim();
		}

		return tags;
	}

	public void deleteTags(View view) {
		List<Tag> newTags = new ArrayList<Tag>();

		for (Tag tag : tags) {
			if (!tag.isChecked())
				newTags.add(tag);
		}

		tags = newTags;

		putTagsIntoList(newTags);
	}

}
