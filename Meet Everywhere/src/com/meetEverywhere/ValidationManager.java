package com.meetEverywhere;

import java.util.ArrayList;
import java.util.List;


public class ValidationManager {

	public List<ValidationError> validateLoginAndPassword (String username, String password) {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if(username == null || username.isEmpty()) {
			errors.add(new ValidationError(R.string.Validation_nameNotProvied));
		}
		if(password == null || password.isEmpty()) {
			errors.add(new ValidationError(R.string.Validation_passwordNotProvided));
		}
		return errors;
		
	}
	
	public List<ValidationError> validateRegisterActivity (String username, String password, String confirmedPassword) {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		errors = validateLoginAndPassword(username, password);
		if(confirmedPassword == null || confirmedPassword.isEmpty()) {
			errors.add(new ValidationError(R.string.Validation_confirmedPasswordNotProvided));
		}
		if(password != null && !password.equals(confirmedPassword)) {
			errors.add(new ValidationError(R.string.Validation_passwordAndConfirmedPasswordAreDifferent));
		}
		return errors;
	}
}
