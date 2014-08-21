package com.meetEverywhere;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetEverywhere.common.Configuration;
//import android.provider.MediaStore;


public class ProfileEdition extends Activity {

	private static final int USER_IMAGE_REQUEST_CODE = 1000;
	private static int RESULT_LOAD_IMAGE = 1;
	
	private EditText descriptionText;
	private TextView username;
	private ImageView userImage;
	private String imageUri;
	private String picturePath;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edition_layout);
        
        username = (TextView) findViewById(R.id.ProfileEdition_userName);
        descriptionText = (EditText) findViewById(R.id.ProfileEditionActivity_description);
       
        String description = Configuration.getInstance().getUser().getDescription();
        if(description != null) {
        	descriptionText.setText(description);
        }
        
        username.setText(Configuration.getInstance().getUser().getNickname());
        
        userImage = (ImageView)findViewById(R.id.ProfileEdition_profilePicture);
        
        if(Configuration.getInstance().getUser().getPicture() != null)
        	userImage.setImageBitmap(Configuration.getInstance().getUser().getPicture());
        
    }
    
    public void attachNewProfilePictureAction(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK, 
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, RESULT_LOAD_IMAGE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			
			ImageView imageView = (ImageView) findViewById(R.id.ProfileEdition_profilePicture);
			imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));	
		}
	}
	
	public void saveSettingsAndGoBackAction(View view) {
		Configuration.getInstance().getUser().setPicture(BitmapFactory.decodeFile(picturePath));
		Configuration.getInstance().getUser().setDescription(descriptionText.getText().toString());
		finish();
	}
    
    
}

