package com.meetEverywhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.meetEverywhere.common.ServUser;
import com.meetEverywhere.common.Tag;

public class ServUserProfileActivity extends Activity{
	
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.serv_user_profile_layout);
		Intent intent = getIntent();
		ServUser user = (ServUser) intent.getSerializableExtra("user");
		
		TextView text = (TextView) findViewById(R.id.user_desc_profile);
		text.setText(user.getDescription());
		
		TextView text2 = (TextView) findViewById(R.id.tags_profile);
		StringBuffer buffer = new StringBuffer();
		
		boolean start = true;
		for(Tag tag : user.getTags()){
			
			if(start)
				start=false;
			else
				buffer.append(", ");
			
			buffer.append(tag.getName());
		}
		
		text2.setText(buffer.toString());
		
	}
	
	public void backToMainMenu(View view) {
		finish();
	}
	
	public void sendInvite(View view) {
		
	}

}
