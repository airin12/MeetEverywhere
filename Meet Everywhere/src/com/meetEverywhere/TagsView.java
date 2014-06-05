package com.meetEverywhere;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.meetEverywhere.common.Tag;

public abstract class TagsView extends Activity {

	private ListView tagListView;
	private List<Tag> tags = new ArrayList<Tag>();
	private ArrayAdapter<Tag> listAdapter;

	private DatabaseAdapter myDBAdapter;

	private List<Tag> getTagsFromDatabase() {
		List<Tag> list = new ArrayList<Tag>();

		

		return list;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayout());
		
		View view = findViewById(R.id.tags_view);
		setupUI(view);

		myDBAdapter = new DatabaseAdapter(getApplicationContext());
		myDBAdapter.open();

		tagListView = (ListView) findViewById(R.id.tagsListView);
		tagListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		tags = getTagsFromDatabase();

		additionalConfig();
		
		tagListView.setAdapter(listAdapter);
		tagListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox1);
            	cb.toggle();
                ((MyCustomAdapter)listAdapter).changeCheckBox(position);
            }
        });
		
		putTagsIntoList(tags);

		registerForContextMenu(tagListView);
	}

	abstract public void additionalConfig();

	public abstract int getLayout();
	
	private void setupUI(View view) {
		if(!(view instanceof EditText)) {

	        view.setOnTouchListener(new OnTouchListener() {

	            public boolean onTouch(View v, MotionEvent event) {
	                hideSoftKeyboard(TagsView.this);
	                return false;
	            }

				

	        });
	    }

	    
	    if (view instanceof ViewGroup) {

	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

	            View innerView = ((ViewGroup) view).getChildAt(i);

	            setupUI(innerView);
	        }
	    }
	}

	@Override
	protected void onDestroy() {

		myDBAdapter.close();
		super.onDestroy();
	}
	
	public void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
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

		tagListView.setAdapter(listAdapter);

	}

	private boolean tagsListContains(String tag) {

		for (Tag contact : tags) {
			if (contact.getName().equals(tag))
				return true;
		}

		return false;
	}

	

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
	
	public ArrayList<String> getTagsAsStrings(){
		ArrayList<String> stringTags = new ArrayList<String>();
		
		for(Tag tag : tags){
			stringTags.add(tag.getName());
		}
		
		return stringTags;
	}
	
	

}
