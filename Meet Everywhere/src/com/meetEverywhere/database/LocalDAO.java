package com.meetEverywhere.database;

import com.meetEverywhere.common.User;

public interface LocalDAO {
	public boolean saveUser(User user);

	public boolean removeUser(User user);
}
