package com.meetEverywhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meetEverywhere.common.ServUser;
import com.meetEverywhere.common.Tag;

public class ServUserProfileActivity extends Activity{
	
	EditText inviteMessage ;
	ImageView image ;
	LinearLayout layout;
	ServUser user;
	boolean visible;
	
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.serv_user_profile_layout);
		Intent intent = getIntent();
		user = (ServUser) intent.getSerializableExtra("user");
		layout = (LinearLayout) findViewById(R.id.invited_layout);
		
		image = (ImageView) findViewById(R.id.send_icon2_profile);
		inviteMessage = (EditText) findViewById(R.id.invite_message);
		visible = false;
		
		if(user.getInvited())
			layout.setVisibility(ImageView.VISIBLE);
		else
			layout.setVisibility(ImageView.GONE);
		
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
		user.changeInvited();
		layout.setVisibility(LinearLayout.VISIBLE);
		visible=false;
		inviteMessage.setVisibility(EditText.GONE);
		image.setVisibility(EditText.GONE);
	}
	
	public void openChat(View view) {
		
	}
	
	public void showMoreFromView(View view) {
		if(visible){
			inviteMessage.setVisibility(EditText.GONE);
			image.setVisibility(EditText.GONE);
			visible=false;
		}
		else{
			inviteMessage.setVisibility(EditText.VISIBLE);
			image.setVisibility(EditText.VISIBLE);
			visible=true;
		}
	}

}
