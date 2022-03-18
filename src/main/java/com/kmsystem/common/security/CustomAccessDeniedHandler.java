package com.kmsystem.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmsystem.common.exception.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	
	//접근 거부 처리자 메서드 
	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res, 
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setMessage("Access denied");

		ObjectMapper mapper = new ObjectMapper();

		String jsonString = mapper.writeValueAsString(errorInfo);

		res.setContentType("application/json;charset=UTF-8");
		res.setStatus(HttpStatus.FORBIDDEN.value());
		res.getWriter().write(jsonString);
	}
	
}
