package telran.java29.forum;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import telran.java29.forum.dao.UserAccountRepository;
import telran.java29.forum.domain.UserAccount;

@SpringBootApplication
public class ForumServiceSpringSecurityApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ForumServiceSpringSecurityApplication.class, args);
	}
	@Autowired
	UserAccountRepository accountRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		if(!accountRepository.existsById("admin")) {
			String hashPassword = passwordEncoder.encode("admin");
			UserAccount admin = UserAccount.builder()
					.login("admin")
					.password(hashPassword)
					.firstName("Super")
					.lastName("Admin")
					.role("ROLE_ADMIN")
					.role("ROLE_USER")
					.role("ROLE_MODERATOR")
					.expdate(LocalDateTime.now().plusYears(25))
					.build();
			accountRepository.save(admin);
					
		}
		
	}

}
