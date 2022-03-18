package com.kmsystem.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice
public class ErrorExceptionHandler extends ResponseEntityExceptionHandler{

//	@Override
//	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
//			HttpHeaders headers, HttpStatus status, WebRequest request){
//
//		ErrorInfo errorInfo = new ErrorInfo();
//		errorInfo.setMessage(ex.toString());
//		errorInfo.setStatus(status.value());
//		errorInfo.setError(status.name());
//		errorInfo.addDetailInfo(ex.getCause().toString(),ex.getMessage());
//
//		log.info("handleExceptionInternal : "+ ex.toString());
//		return super.handleExceptionInternal(ex, errorInfo, headers, status, request);
//	}

	//접근거부처리
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> hanleAccessDeniedException(AccessDeniedException ex,
															 WebRequest request){

		ErrorInfo errorInfo = new ErrorInfo();
		String message = "접근거부";
		errorInfo.setMessage(message);

		log.info("handleAccessDeniedException"+ ex.toString());
		return super.handleExceptionInternal(ex, errorInfo, null, HttpStatus.FORBIDDEN, request);

	}

	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public class acceptedEx extends RuntimeException{

	}

/*	//시스템예외처리  # 추후 수정 #
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Object> handleSystemException(Exception ex, WebRequest request){

		ErrorInfo errorInfo = new ErrorInfo();

		errorInfo.setMessage(ex.toString());
		errorInfo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorInfo.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
		errorInfo.addDetailInfo("System error is occured",ex.getMessage());
		log.info("handleSystemException :" + ex.toString());
		return super.handleExceptionInternal(ex, errorInfo, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}*/

//	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//		ErrorInfo errorInfo = new ErrorInfo();
//		StringBuffer sbMessage = new StringBuffer();
//
//		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//		for(FieldError fieldError : fieldErrors) {
//			String field = fieldError.getField();
//			String defaultMessage = fieldError.getDefaultMessage();
//
//			sbMessage.append(field + " : " + defaultMessage);
//			sbMessage.append(" \r\n ");
//		}
//
//		errorInfo.setMessage(sbMessage.toString());
//		errorInfo.setStatus(status.value());
//		errorInfo.setError(status.name());
//
//		log.info("handleMethodArgumentNotValid :" + sbMessage.toString());
//		return super.handleExceptionInternal(ex, errorInfo, headers, status, request);
//	}
//
//	@Override
//	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//		ErrorInfo errorInfo = new ErrorInfo();
//		errorInfo.setMessage(ex.toString());
//		errorInfo.setStatus(status.value());
//		errorInfo.setError(status.name());
//
//		List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
//
//		for(ObjectError globalError : globalErrors) {
//			errorInfo.addDetailInfo(globalError.getObjectName(), globalError.getDefaultMessage());
//		}
//
//		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//
//		for(FieldError fieldError : fieldErrors) {
//			errorInfo.addDetailInfo(fieldError.getField(), fieldError.getDefaultMessage());
//		}
//
//		log.info("handleBindException" + ex.toString());
//		return super.handleExceptionInternal(ex, errorInfo, headers, status, request);
//	}
//
//	@Override
//	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
//			HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//		ErrorInfo errorInfo = new ErrorInfo();
//
//		errorInfo.setStatus(status.value());
//		errorInfo.setError(status.name());
//		errorInfo.setMessage(ex.toString());
//
//		Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
//		if (!CollectionUtils.isEmpty(supportedMethods)) {
//			headers.setAllow(supportedMethods);
//		}
//		errorInfo.addDetailInfo(supportedMethods.toString(), ex.getMessage());
//
//		log.info("handleHttpRequestMethodNotSupported" + ex.toString());
//		return super.handleExceptionInternal(ex, errorInfo, headers, status, request);
//	}
	
}
