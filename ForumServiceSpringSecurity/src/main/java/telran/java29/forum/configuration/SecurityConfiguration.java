package telran.java29.forum.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(HttpMethod.POST,"/account");
		web.ignoring().antMatchers("/forum/posts/**");
	}
	
	@Override 
	protected void configure(HttpSecurity http) throws Exception {
		 http.httpBasic();

		 http.csrf().disable();//allows post paths
		 http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		 http.authorizeRequests().anyRequest().authenticated();
//		 http.authorizeRequests().antMatchers(HttpMethod.POST,"/account").permitAll();
		 http.authorizeRequests().antMatchers("/account/id/{role}").hasRole("ADMIN");
		 http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/account/{id}")
		 .access("@customSecurity.checkAuthorityForUser(authentication, #id)");
		 http.authorizeRequests().antMatchers(HttpMethod.PUT, "/account/password/{id}")
		 .access("@customSecurity.checkAuthorityForUser(authentication, #id)");
		 http.authorizeRequests().antMatchers(HttpMethod.PUT, "/forum/post/{id}/like").hasAnyRole("ADMIN","MODERATOR","USER");//edit
		 http.authorizeRequests().antMatchers(HttpMethod.PUT, "/account/{id}")
		 .access("@customSecurity.checkAuthorityForUser(authentication, #id)");

		 
		 http.authorizeRequests().antMatchers(HttpMethod.GET, "/forum/post/{id}").hasAnyRole("ADMIN","MODERATOR","USER");
		 http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/forum/post/{id}")
		 .access("@customSecurity.checkAuthorityForDeletePost(authentication, #id)");
		 http.authorizeRequests().antMatchers(HttpMethod.PUT, "/forum/post/{id}")
		 .access("@customSecurity.checkAuthorityForUpdatePost(authentication, #id)");
//		 http.authorizeRequests().antMatchers(HttpMethod.PUT,"/forum/post").hasAnyRole("ADMIN","MODERATOR","USER");
//		 http.authorizeRequests().antMatchers(HttpMethod.PUT,"/forum/post/{id}/like").hasAnyRole("ADMIN","MODERATOR","USER");
		 http.authorizeRequests().antMatchers("/actuator/**").hasRole("ADMIN");
	} 
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
