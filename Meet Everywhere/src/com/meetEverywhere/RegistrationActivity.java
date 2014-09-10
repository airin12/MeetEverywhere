package com.meetEverywhere;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.meetEverywhere.common.User;
import com.meetEverywhere.database.AccountsDAO;

public class RegistrationActivity extends Activity {

	private static int RESULT_LOAD_IMAGE = 1;
	private ImageView userImage;
	private String picturePath;
	private EditText descriptionEditText;
	private ValidationManager validationManager;
	private EditText passwordEditText;
	private EditText usernameEditText;
	private EditText confirmedPasswordEditText;
	private String username;
	private String password;
	private String description;
	private String confirmedPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		validationManager = new ValidationManager();

		descriptionEditText = (EditText) findViewById(R.id.ActivityRegistration_userDescription);
		usernameEditText = (EditText) findViewById(R.id.ActivityRegistration_userNameEditText);
		passwordEditText = (EditText) findViewById(R.id.ActivityRegistration_passwordEditText);
		confirmedPasswordEditText = (EditText) findViewById(R.id.ActivityRegistration_confirmPasswordEditText);
		userImage = (ImageView) findViewById(R.id.profilePicture);

		// nie latwiej zamiast dodawac kilka listenerow zebrac po prostu dane w
		// funkcji registerUserAction?

		usernameEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				username = usernameEditText.getText().toString();
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});

		descriptionEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				description = descriptionEditText.getText().toString();
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});

		passwordEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				password = passwordEditText.getText().toString();
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});

		confirmedPasswordEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				confirmedPassword = confirmedPasswordEditText.getText().toString();
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}

	public void attachPictureAction(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

	public void registerUserAction(View view) {
		List<ValidationError> errors = validationManager
		        .validateRegisterActivity(username, password, confirmedPassword);
		if (errors.isEmpty()) {
			DAO dao = new DAO();
			User user;

			if ((user = dao.register(username, password, description)) == null) {
				errors.add(new ValidationError(R.string.Validation_userAlreadyExists));
			} else {
				AccountsDAO accountsDAO = AccountsDAO.getInstance(this);
				accountsDAO.register(user);
				if (!accountsDAO.logIn(username, password)) {
					Log.i("DB", "accounts doesnt exist");
					return;
				}
				startActivity(new Intent(this, MeetEverywhere.class));
				finish();
			}
			
		}
		
		if (!errors.isEmpty()) {
			ErrorDialog.createDialog(this, errors).show();
		}
	}

}