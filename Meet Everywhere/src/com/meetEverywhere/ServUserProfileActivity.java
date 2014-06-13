package com.meetEverywhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meetEverywhere.bluetooth.BluetoothDispatcher;
import com.meetEverywhere.common.Tag;
import com.meetEverywhere.common.User;

public class ServUserProfileActivity extends Activity{
	
	private EditText inviteMessage ;
	private ImageView image ;
	private LinearLayout layout;
	private User user;
	private boolean visible;
	
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.serv_user_profile_layout);
		user = BluetoothDispatcher.getInstance().getTempUserHolder();
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
		for(Tag tag : user.getHashTags()){
			
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
		DAO dao = new DAO();
		dao.sendInvite(user.getId(), inviteMessage.getText().toString());
	}
	
	public void openChat(View view) {
		Intent intent = new Intent(this, GeneralChat.class);
    	startActivity(intent);
	}
	
	public void showMoreFromView(View view) {
		if(user.getInvited())
			Toast.makeText(getApplicationContext(), "Zaproszenie zosta�o ju� wys�ane", Toast.LENGTH_SHORT).show();
		else{
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

}
