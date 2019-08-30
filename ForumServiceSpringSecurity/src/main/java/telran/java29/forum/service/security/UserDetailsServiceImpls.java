package telran.java29.forum.service.security;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import telran.java29.forum.dao.UserAccountRepository;
import telran.java29.forum.domain.UserAccount;
@Service
public class UserDetailsServiceImpls implements UserDetailsService {
	
	@Autowired
	UserAccountRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount userAccount = repository.findById(username)
				.orElseThrow(()->new UsernameNotFoundException(username));
		String password = userAccount.getPassword();
		Set<String> setRoles = userAccount.getRoles();
		if(userAccount.getExpdate().isBefore(LocalDateTime.now())) {
			setRoles = new HashSet<>();
		}
		return new User(username, password, 
				AuthorityUtils.createAuthorityList(setRoles.toArray(new String[setRoles.size()])));
	}

}
