package com.meetEverywhere;

import java.util.ArrayList;
import java.util.List;

import com.meetEverywhere.common.User;

public class LoginActivityManagement {

	public List<ValidationError> validate(User user) {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if(user.getNickname() == null || user.getNickname().isEmpty()) {
			errors.add(new ValidationError(R.string.Validation_nameNotProvied));
		}
		if(user.getPassword() == null || user.getPassword().isEmpty()) {
			errors.add(new ValidationError(R.string.Validation_passwordNotProvided));
		}
		return errors;
		
	}
}
