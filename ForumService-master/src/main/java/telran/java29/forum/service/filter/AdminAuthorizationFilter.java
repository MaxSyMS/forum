package telran.java29.forum.service.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import telran.java29.forum.dao.UserAccountRepository;
import telran.java29.forum.domain.UserAccount;

@Service
@Order(20)
public class AdminAuthorizationFilter implements Filter {
	
	@Autowired
	UserAccountRepository accountRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		String method = request.getMethod();
		if (checkPointCut(path, method)) {
			String login = request.getUserPrincipal().getName();
			UserAccount userAccount = accountRepository.findById(login).get();
			if (userAccount.getRoles().contains("Admin")) {
				chain.doFilter(request, response);
			} else {
				response.sendError(403, "User is not Admin");
				return;
			} 
		}else {
			chain.doFilter(request, response);
		}

	}

	private boolean checkPointCut(String path, String method) {
		String[] pathes = path.split("/"); 
		return pathes.length == 4 && "account".equals(pathes[1]) 
				&& ("Put".equalsIgnoreCase(method) || "Delete".equalsIgnoreCase(method));
	}

}
