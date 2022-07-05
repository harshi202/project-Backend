package com.edusystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edusystem.entity.StudentDetails;
import com.edusystem.entity.User;
import com.edusystem.exception.AuthenticationFailedException;
import com.edusystem.exception.PasswordMisMatchException;
import com.edusystem.exception.StudentNotFoundException;
import com.edusystem.exception.UserNotFoundException;
import com.edusystem.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User saveUser(User user) {
		User newUser = userRepository.save(user);
		return user;
	}

	@Override
	public User getUserById(int userId) {
		Optional<User> optionalUser = userRepository.findById(userId);

		if (optionalUser.isEmpty()) {
			throw new UserNotFoundException("User Not existing with id: " + userId);
		}

		User user = optionalUser.get();

		return user;
	}

	@Override
	public List<User> getAllUsers() {

		List<User> user = userRepository.findAll();

		return user;

	}

	@Override
	public User updateUser(User user) {
		Optional<User> optionalUser = userRepository.findById(user.getUserId());

		if (optionalUser.isEmpty()) {
			throw new UserNotFoundException("User Not found with id: " + user.getUserId());
		}

		User updatedUser = userRepository.save(user);

		return updatedUser;
	}

	@Override
	public void deleteUser(int userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isEmpty()) {
			throw new UserNotFoundException("User Not found with id: " + userId);
		}

		userRepository.deleteById(userId);

	}

	@Override
	public User doLogin(int userId, String userPassword) {
		User user = userRepository.login(userId, userPassword);
		if (user == null) {
			throw new AuthenticationFailedException("UserId or Password Invalid");
		}

		return user;

	}

	@Override
	public String recoverPassword(int userId) {
		@SuppressWarnings("deprecation")
		User user = userRepository.getById(userId);
		String password = user.getUserPassword();
		return password;
	}

	@Override
	public String changePassword(int userId, String oldPassword, String newPassword) {
		Optional<User> userById = userRepository.findById(userId);
		if (userById.isEmpty()) {
			throw new UserNotFoundException(userId + " user not found");
		}
		
		User user = userById.get();
		if(!user.getUserPassword().equals(oldPassword)) {
			throw new PasswordMisMatchException("old password not matching with new password");
		}
		
		User newUser = new User();
		
		newUser.setUserId(user.getUserId());
		newUser.setUserName(user.getUserName());
		newUser.setUserRole(user.getUserRole());
		newUser.setUserPassword(newPassword);
		userRepository.save(newUser);
		
		return "Password Updated Successfully.";
	}

}
