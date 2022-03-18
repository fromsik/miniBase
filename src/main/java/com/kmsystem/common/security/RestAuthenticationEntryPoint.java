package com.kmsystem.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmsystem.common.exception.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//인증 헤더를 실패한 경우를 정의
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMappger = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
        res.setContentType("application/json;charset=UTF-8");

        ErrorInfo errorInfo = new ErrorInfo();
        if (InsufficientAuthenticationException.class == authException.getClass()) {
            errorInfo.setMessage("Not Logined!");
            res.setStatus(HttpStatus.UNAUTHORIZED.value());

        }
        else {
            errorInfo.setMessage("Bad Request!");
            res.setStatus(HttpStatus.BAD_REQUEST.value());
        }

        String jsonString = objectMappger.writeValueAsString(errorInfo);
        res.getWriter().write(jsonString);
    }
}
