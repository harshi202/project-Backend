package com.edusystem.service;

import java.util.List;

import com.edusystem.entity.User;

public interface UserService {

	public User doLogin(int userId, String userPassword);

	public User saveUser(User user);

	public User getUserById(int userId);

	public List<User> getAllUsers();

	public User updateUser(User user);

	public void deleteUser(int userId);

	public String recoverPassword(int userId);

	public String changePassword(int userId, String oldPassword, String newPassword);

}
