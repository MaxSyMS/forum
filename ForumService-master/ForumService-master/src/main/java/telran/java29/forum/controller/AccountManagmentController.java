package telran.java29.forum.controller;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.java29.forum.dto.UserEditDto;
import telran.java29.forum.dto.UserProfileDto;
import telran.java29.forum.dto.UserRegDto;
import telran.java29.forum.service.AccountService;

@RestController
@RequestMapping("/account")

public class AccountManagmentController {

	@Autowired
	AccountService accountService;

	@PostMapping
	public UserProfileDto register(@RequestBody UserRegDto userRegDto) {
		return accountService.addUser(userRegDto);
	}

	@PostMapping("/{id}")
	public UserProfileDto userLogin(@PathVariable String id, Principal principal) {
		return accountService.findUserById(id, principal.getName());
	}

	@PutMapping
	public UserProfileDto edit(@RequestBody UserEditDto userEditDto, Principal principal) {
		return accountService.editUser(userEditDto, principal.getName());
	}

	@DeleteMapping
	public UserProfileDto remove(Principal principal) {
		return accountService.removeUser(principal.getName());
	}

	@PutMapping("/{id}/{role}")
	public Set<String> addRole(@PathVariable String id, @PathVariable String role) {
		return accountService.addRole(id, role);
	}
	
	@DeleteMapping("/{id}/{role}")
	public Set<String> removeRole(@PathVariable String id, @PathVariable String role) {
		return accountService.deleteRole(id, role);
	}
	
	@PutMapping("/password")
	public void changePassword(Principal principal,
			@RequestHeader("X-Password") String password) {
		accountService.changePassword(principal.getName(), password);
	}

}
