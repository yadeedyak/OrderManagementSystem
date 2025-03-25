package com.Order.OrderManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Order.OrderManagementSystem.entity.User;
import com.Order.OrderManagementSystem.enums.Role;
import com.Order.OrderManagementSystem.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder; // Inject password encoder
	

	// ------------------------   SEARCH THE USER BY USERNAME     --------------------------
	public User searchUserByUserName(String username) {
	
		User user=userRepository.findByUsername(username).orElse(null);
	 
		return user;
	}

	
//	--------------------- CREATE USER ------------------------------------

	public ResponseEntity<String> createUser(User user) {

		User listedUser = searchUserByUserName(user.getUsername());

		if (listedUser != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
		user.setRole(Role.USER);
		userRepository.save(user);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("registration Sucessful");
	}
// --------------------------------- CREATE SELLER ------------------------------------------
	
	public ResponseEntity<String> createSeller(User user) {

		User listedUser = searchUserByUserName(user.getUsername());

		if (listedUser != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
		user.setRole(Role.SELLER);
		userRepository.save(user);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("registration Sucessful");
	}
	
//--------------------------   UPDATE USER    -------------------------------------
	public ResponseEntity<String> updateUser(String username, User user) {

		User listedUser = searchUserByUserName(username);

		if (listedUser == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid credentials or no user found");
		}
		listedUser.setUsername(user.getUsername());
		
		   // Encrypt and update password if provided
	    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
	    	listedUser.setPassword(passwordEncoder.encode(user.getPassword()));
	    }

		
	    listedUser.setRole(Role.USER);
		userRepository.save(listedUser);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("updated Sucessfully");
		
	}
	
	//---------------------- DELETE USER  ---------------------------------
	
	public ResponseEntity<String> deleteUser(String username) {
		
		User user=searchUserByUserName(username);
		
		if(user!=null) {
			
			userRepository.delete(user);
			
			return ResponseEntity.status(HttpStatus.OK).body("deleted sucessfully");
			
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user found");
		
	}


	public ResponseEntity<?> getCustomerOrders(String username) {

		User user=userRepository.findByUsername(username).orElse(null);

		if(user==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no user");
		}

		return  ResponseEntity.status(HttpStatus.OK).body(user.getCustomerOrders());
	}
	
	
	
	


}