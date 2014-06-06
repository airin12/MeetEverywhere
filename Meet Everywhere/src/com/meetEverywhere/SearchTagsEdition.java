package com.meetEverywhere;

import android.content.Intent;
import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SearchTagsEdition extends TagsView {

	private int percentage; 
	
	public void searchTags(View view) {
		Intent intent = new Intent(this, FoundTagsActivity.class);
    	intent.putStringArrayListExtra("tags", getTagsAsStrings());
    	intent.putExtra("perc", percentage);
    	intent.putExtra("typeOfAdapter", "bySpecifiedTags");
    	startActivity(intent);
		
	}

	@Override
	public int getLayout() {
		return R.layout.search_tags_edition_layout;
	}

	@Override
	public void additionalConfig() {
		
		SeekBar percentageControl;
		
		percentageControl = (SeekBar) findViewById(R.id.perc_slider);
		final TextView textView = (TextView) findViewById(R.id.perc_text);
		 
        percentageControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	String text="Minimalna zgodnoœæ tagów: ";
        	
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
            	percentage=progress;
                textView.setText(text+progress+"%");
            }
 
            public void onStartTrackingTouch(SeekBar seekBar) {
                
            }
 
            public void onStopTrackingTouch(SeekBar seekBar) {
                
            }
        });
	}

}
