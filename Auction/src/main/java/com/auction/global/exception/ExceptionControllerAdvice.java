package com.auction.global.exception;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.auction.api.response.ApiResponse;
import com.auction.api.response.ConstraintError;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ConstraintError> constraintErrors = new ArrayList<>();
		ex.getBindingResult().getFieldErrors().stream().forEach(error -> {
			constraintErrors.add(new ConstraintError(error.getField(), error.getDefaultMessage()));
		});
		ApiResponse apiErrorVO = new ApiResponse();
		apiErrorVO.setStatus(HttpStatus.BAD_REQUEST.value());
		apiErrorVO.setBody(constraintErrors);
		apiErrorVO.setMessage("Fields are required");
		apiErrorVO.setUri(request.getDescription(false).replace("uri=", ""));
		printLog(request.getDescription(false).replace("uri=", ""), ex);
		return new ResponseEntity<>(apiErrorVO, status);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> resourceNotFoundExceptionHandler(Exception ex, WebRequest request) {
		ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), ex,
				request.getDescription(false).replace("uri=", ""));
		printLog(request.getDescription(false).replace("uri=", ""), ex);
		return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(JwtExecption.class)
	public ResponseEntity<Object> jwtTokenExpired(Exception ex, WebRequest request) {
		ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), ex,
				request.getDescription(false).replace("uri=", ""));
		printLog(request.getDescription(false).replace("uri=", ""), ex);
		return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		printLog(request.getDescription(false).replace("uri=", ""), ex);
		String message = ex.getCause().getMessage().split("Detail: Key", 2)[1];
		ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), message, ex,
				request.getDescription(false).replace("uri=", ""));
		return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> globalExceptionHandler(Exception ex, WebRequest request) {
		ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), ex,
				request.getDescription(false).replace("uri=", ""));
		printLog(request.getDescription(false).replace("uri=", ""), ex);
		return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void printLog(String uri, Object exception) {

		LOGGER.error("User email :  " + SecurityContextHolder.getContext().getAuthentication().getName()
				+ "    and path :   " + uri, exception);
	}

}
