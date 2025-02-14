package kr.co.moodtracker.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotDeletedException;
import kr.co.moodtracker.exception.ImageLoadException;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.handler.AuthUserHandler;
import kr.co.moodtracker.service.ImageService;

@Controller
@RequestMapping("/view")
public class ImageController {
	
	@Autowired
	ImageService imageService;
	
	@GetMapping(value = "/image")
	public ResponseEntity<?> getImage(
			@RequestParam("path") String path
			, HttpSession sess
	) throws ImageLoadException, IOException 
	{
		InputStreamResource imageData = imageService.getImage(path, AuthUserHandler.getUserId(sess));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE) // 이미지 타입에 맞게 설정
                .body(imageData);
	}
	
	@GetMapping(value = "/profile-image")
	public ResponseEntity<?> getProfileImage(
			@RequestParam("path") String path
			, HttpSession sess
	) throws ImageLoadException, IOException
	{
		InputStreamResource imageData = imageService.getProfileImage(path, AuthUserHandler.getUserId(sess));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE) // 이미지 타입에 맞게 설정
                .body(imageData);

	}
	
	@DeleteMapping(value = "/image")
	public ResponseEntity<?> deleteImage(
			@RequestParam("imageId") List<Integer> imageId
			, HttpSession sess
	) throws DataNotDeletedException, DataMissingException 
	{
		Map<String, Object> result = new HashMap<>();
		imageService.deleteImage(imageId, AuthUserHandler.getUserId(sess));
        result.put("success", true);
		return ResponseEntity.ok().body(result);
	}

}
