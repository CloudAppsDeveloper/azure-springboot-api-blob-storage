package com.esms.ms;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping(path = "/add")
	public @ResponseBody String addNewUser(@RequestParam String name) {
		User n = new User();
		n.setName(name);
		userRepository.save(n);
		return "Saved";
	}
	
	@PostMapping(path = "/")
	public @ResponseBody String addUser(@RequestBody User user) {
		
		userRepository.save(user);
		return "Saved";
	}
	
	@PutMapping(path = "/{id}")
	public @ResponseBody String updateUser(@RequestBody User user,@PathVariable Integer id) {
		user.setId(id);
		userRepository.save(user);
		return "Saved";
	}

	@GetMapping(path = "/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@DeleteMapping(path = "/{id}")
	public String deleteUsers(@PathVariable Integer id) {
		userRepository.deleteById(id);
		
		return "deleted user id"+id;
	}
	
	@GetMapping(path = "/{id}")
	public @ResponseBody Optional<User> getUser(@PathVariable Integer id) {
		return userRepository.findById(id);
	}
}
