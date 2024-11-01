package kr.co.moodtracker.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotDeletedException;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.DataNotUpdatedException;
import kr.co.moodtracker.exception.ImageLoadException;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.exception.SettingDataException;
import kr.co.moodtracker.exception.ZeroDataException;

@RestControllerAdvice
public class ExceptionController {
	
	private static Logger logger = LoggerFactory.getLogger(ExceptionController.class);
	
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
	
	@ExceptionHandler(value = {
			DataNotDeletedException.class
			, DataNotInsertedException.class
			, DataNotUpdatedException.class
			, DataMissingException.class
			, ZeroDataException.class
			, SettingDataException.class})
	public ResponseEntity<?> dataError(Exception e) {
		Map<String, Object> result = new HashMap<>();
		logger.info(e.getMessage());
		result.put("msg", e.getMessage());
		result.put("success", false);
		return ResponseEntity.ok().body(result); 
	}
	
	@ExceptionHandler(value = {
			Exception.class
			, IllegalStateException.class
			, IOException.class})
	public ResponseEntity<?> Error(Exception e) {
		e.printStackTrace();
		Map<String, Object> result = new HashMap<>();
		result.put("msg", "서버 오류: 관리자에게 문의해주세요.");
		result.put("success", false);
		return ResponseEntity.ok().body(result); 
	}
	
}
