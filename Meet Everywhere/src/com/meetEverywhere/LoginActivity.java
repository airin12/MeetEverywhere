package com.meetEverywhere;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.meetEverywhere.common.User;

public class LoginActivity extends Activity {

	private LoginActivityManagement loginActivityManagement;
	private EditText passwordEditText;
	private EditText usernameEditText;
	private String username;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginActivityManagement = new LoginActivityManagement();
		
		usernameEditText = (EditText) findViewById(R.id.ActivityLogin_userName);
		passwordEditText = (EditText) findViewById(R.id.ActivityLogin_password);
		
		usernameEditText.addTextChangedListener(new TextWatcher() {
			
			public void afterTextChanged(Editable s) {
				username = usernameEditText.getText().toString();
			}
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});
		
		passwordEditText.addTextChangedListener(new TextWatcher() {
			
			public void afterTextChanged(Editable s) {
				password = passwordEditText.getText().toString();
			}
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {}	
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void registerIfNoAccountAction(View view) {
		startActivity(new Intent(this, RegistrationActivity.class));
	}
	
	public void signInAction(View view) {
		//normalnie tu bedziemy dostawac obiekt User, ale póki co tworze tak na chama
		User user = new User(username, null, null, null);
		user.setPassword(password);
		List<ValidationError> errors = loginActivityManagement.validate(user);
		if(!errors.isEmpty()) {
			ErrorDialog.createDialog(this, errors).show();
		}
	}
}


