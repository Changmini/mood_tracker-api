package kr.co.moodtracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotDeletedException;
import kr.co.moodtracker.exception.ImageLoadException;
import kr.co.moodtracker.exception.SessionNotFoundException;

@RestControllerAdvice
public class ExceptionController {
	
	@ExceptionHandler(value = SessionNotFoundException.class)
	public ResponseEntity<?> sessioNotFound(SessionNotFoundException e) {
		Map<String, Object> result = new HashMap<>();
		result.put("msg", e.getMessage());
		result.put("success", false);
		return ResponseEntity.ok().body(result); 
	}
	
	@ExceptionHandler(value = ImageLoadException.class)
	public ResponseEntity<?> imageError(ImageLoadException e) {
		Map<String, Object> result = new HashMap<>();
		result.put("msg", e.getMessage());
		result.put("success", false);
		return ResponseEntity.ok().body(result); 
	}
	
	@ExceptionHandler(value = {DataNotDeletedException.class, DataMissingException.class})
	public ResponseEntity<?> dataError(Exception e) {
		e.printStackTrace();
		Map<String, Object> result = new HashMap<>();
		result.put("msg", e.getMessage());
		result.put("success", false);
		return ResponseEntity.ok().body(result); 
	}
	
}
