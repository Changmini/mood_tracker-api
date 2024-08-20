package kr.co.moodtracker.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotDeletedException;
import kr.co.moodtracker.exception.ImageLoadException;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.service.ImageService;
import kr.co.moodtracker.vo.UserVO;

@Controller
public class ImageController extends CommonController {
	
	@Autowired
	ImageService imageService;
	
	@GetMapping(value = "/image")
	public ResponseEntity<?> getImage(String path, HttpSession sess) 
			throws SessionNotFoundException, ImageLoadException {
		Map<String, Object> result = new HashMap<>();
		try {
			UserVO user = setUserInfo(sess);
			byte[] imageData = imageService.getImage(path, user.getUserId());
            ByteArrayResource resource = new ByteArrayResource(imageData);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE) // 이미지 타입에 맞게 설정
                    .body(resource);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			result.put("msg", ioe.getMessage());
			result.put("success", false);
		}
		return ResponseEntity.ok().body(result);
	}
	
	@DeleteMapping(value = "/image")
	public ResponseEntity<?> deleteImage(
			@RequestParam("imageId") List<Integer> imageId, HttpSession sess) 
					throws SessionNotFoundException, DataNotDeletedException, DataMissingException {
		Map<String, Object> result = new HashMap<>();
		UserVO user = setUserInfo(sess);
		imageService.deleteImage(imageId, user.getUserId());
        result.put("success", true);
		return ResponseEntity.ok().body(result);
	}

}
