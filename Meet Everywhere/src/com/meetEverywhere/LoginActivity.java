package com.meetEverywhere;

import java.util.List;

import com.meetEverywhere.common.User;
import com.meetEverywhere.database.AccountsDAO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private ValidationManager validationManager;
	private EditText passwordEditText;
	private EditText usernameEditText;
	private String username;
	private String password;
	private AccountsDAO accountsDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		accountsDAO = AccountsDAO.getInstance(this);
		validationManager = new ValidationManager();

		usernameEditText = (EditText) findViewById(R.id.ActivityLogin_userName);
		passwordEditText = (EditText) findViewById(R.id.ActivityLogin_password);

		usernameEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				username = usernameEditText.getText().toString();
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});

		passwordEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				password = passwordEditText.getText().toString();
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

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
		finish();
	}

	public void signInAction(View view) {
		List<ValidationError> errors = validationManager
				.validateLoginAndPassword(username, password);
		
		if(errors.isEmpty()) {
			User user;
			DAO dao = new DAO();
			if (accountsDAO.logIn(username, password)) {
				startActivity(new Intent(this, MeetEverywhere.class));
				finish();
			} else if((user = dao.loginOnExternalServer(username, password)) != null) { 
				//rejestracja w lokalnej bazie danych
				accountsDAO.register(user);
				if (!accountsDAO.logIn(username, password)) {
					Log.i("DB", "accounts doesnt exist");
					return;
				}
				startActivity(new Intent(this, MeetEverywhere.class));
				finish();
			} else {
				errors.add(new ValidationError(R.string.Validation_loginFailed));
				ErrorDialog.createDialog(this, errors).show();
				Log.i("login", "login failed");
			}
		}
		
		if (!errors.isEmpty()) {
			ErrorDialog.createDialog(this, errors).show();
		}
	}
}
