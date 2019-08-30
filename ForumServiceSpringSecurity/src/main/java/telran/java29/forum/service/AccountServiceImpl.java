package telran.java29.forum.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import telran.java29.forum.configuration.AccountConfiguration;
import telran.java29.forum.dao.UserAccountRepository;
import telran.java29.forum.domain.UserAccount;
import telran.java29.forum.dto.UserEditDto;
import telran.java29.forum.dto.UserProfileDto;
import telran.java29.forum.dto.UserRegDto;
import telran.java29.forum.exceptions.UserConflictException;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	UserAccountRepository userRepository;

	@Autowired
	AccountConfiguration accountConfiguration;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public UserProfileDto addUser(UserRegDto userRegDto) {
		if (userRepository.existsById(userRegDto.getLogin())) {
			throw new UserConflictException();
		}
		String hashPassword = passwordEncoder.encode(userRegDto.getPassword());
		UserAccount userAccount = UserAccount.builder()
				.login(userRegDto.getLogin())
				.password(hashPassword)
				.firstName(userRegDto.getFirstName())
				.lastName(userRegDto.getLastName())
				.role("ROLE_USER")
				.expdate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()))
				.build();
		userRepository.save(userAccount);
		return convertToUserProfileDto(userAccount);
	}

	@Override
	public UserProfileDto findUserById(String id) {
		UserAccount userAccount = userRepository.findById(id).get();
		return convertToUserProfileDto(userAccount);
	}

	@Override
	public UserProfileDto editUser(UserEditDto userEditDto, String login) {
		UserAccount userAccount = userRepository.findById(login).get();
		if (userEditDto.getFirstName() != null) {
			userAccount.setFirstName(userEditDto.getFirstName());
		}
		if (userEditDto.getLastName() != null) {
			userAccount.setLastName(userEditDto.getLastName());
		}
		userRepository.save(userAccount);

		return convertToUserProfileDto(userAccount);
	}

	@Override
	public UserProfileDto removeUser(String login) {
		UserAccount userAccount = userRepository.findById(login).get();
		userRepository.delete(userAccount);
		return convertToUserProfileDto(userAccount);
	}
	
	@Override
	public Set<String> addRole(String id, String role) {
		UserAccount userAccount = userRepository.findById(id)
				.orElseThrow(() -> new UserConflictException());
		userAccount.addRole("ROLE_"+role.toUpperCase());
		userRepository.save(userAccount);
		return userAccount.getRoles();
	}
	
	@Override
	public void changePassword(String login, String password) {
		UserAccount userAccount = userRepository.findById(login).get();
		String hashPassword = passwordEncoder.encode(password);
		userAccount.setPassword(hashPassword);
		userAccount.setExpdate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()));
		userRepository.save(userAccount);
	}

	@Override
	public Set<String> deleteRole(String id, String role) {
		UserAccount userAccount = userRepository.findById(id)
				.orElseThrow(() -> new UserConflictException());
		userAccount.removeRole("ROLE_"+role.toUpperCase());
		userRepository.save(userAccount);
		return userAccount.getRoles();
	}

	private UserProfileDto convertToUserProfileDto(UserAccount userAccount) {
		return UserProfileDto.builder()
				.firstName(userAccount.getFirstName())
				.lastName(userAccount.getLastName())
				.login(userAccount.getLogin())
				.roles(userAccount.getRoles())
				.build();
	}

	
}
