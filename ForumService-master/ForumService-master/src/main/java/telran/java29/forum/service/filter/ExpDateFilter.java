package telran.java29.forum.service.filter;

import java.io.IOException;
import java.time.LocalDateTime;

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
@Order(15)
public class ExpDateFilter implements Filter {
	
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
			chain.doFilter(request, response);
			return;
		}
		UserAccount userAccount = accountRepository.findById(request.getUserPrincipal().getName()).get();
		LocalDateTime expDate = userAccount.getExpdate();
		if (expDate.isBefore(LocalDateTime.now())) {
			response.sendError(403, "Password expired");
		}else {
			chain.doFilter(request, response);
		}
	}
	private boolean checkPointCut(String path, String method) {
		boolean check = ("/account/".equalsIgnoreCase(path) && "Post".equalsIgnoreCase(method))
				|| ("/account/password".equalsIgnoreCase(path));
		return check;
	}

}
