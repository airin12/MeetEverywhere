package com.meetEverywhere;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.meetEverywhere.common.Configuration;
import com.meetEverywhere.common.User;
//import android.provider.MediaStore;


public class ProfileEdition extends Activity {

	private static final int USER_IMAGE_REQUEST_CODE = 1000;
	private static int RESULT_LOAD_IMAGE = 1;
	
	private SharedPreferences userSettings;
	private EditText descriptionText;
	private EditText usernameText;
	private ImageView userImage;
	private String imageUri;
	private String picturePath;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edition_layout);
        
        userSettings = getSharedPreferences(SharedPreferencesKeys.preferencesName, Activity.MODE_PRIVATE);
        descriptionText = (EditText) findViewById(R.id.ProfileEditionActivity_description);
        usernameText = (EditText) findViewById(R.id.ProfileEdition_userName);
       
        String description = Configuration.getInstance().getUser().getDescription();
        if(description != null) {
        	descriptionText.setText(description);
        }
        
        String username = Configuration.getInstance().getUser().getNickname();
        if(username != null) {
        	usernameText.setText(username);
        }
        
        userImage = (ImageView)findViewById(R.id.profilePicture);
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
			
			ImageView imageView = (ImageView) findViewById(R.id.profilePicture);
			imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));	
		}
	}
	
	public void saveSettingsAndGoBackAction(View view) {
		Configuration.getInstance().getUser().setPicture(BitmapFactory.decodeFile(picturePath));
		Configuration.getInstance().getUser().setDescription(descriptionText.getText().toString());
		Configuration.getInstance().getUser().setNickname(usernameText.getText().toString());
		finish();
	}
    
    
    private class ImageLoader extends AsyncTask<Uri, Object, Bitmap>{

		@Override
		protected Bitmap doInBackground(Uri... params) {
			try {
				return BitmapFactory.decodeStream(getContentResolver().openInputStream(params[0]));
			} catch (FileNotFoundException e) {
				return null;
			}
		}
    	
		@Override
		protected void onPostExecute(Bitmap param){
			if(param != null)
				userImage.setImageBitmap(param);
		}
    }
}

