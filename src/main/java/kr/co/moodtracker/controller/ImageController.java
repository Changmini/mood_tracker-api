package kr.co.moodtracker.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.ImageLoadException;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.service.ImageService;
import kr.co.moodtracker.vo.UserVO;

@RequestMapping("/image")
public class ImageController extends CommonController {
	
	@Autowired
	ImageService imageService;
	
	@GetMapping("{base64}")
	public ResponseEntity<?> getImage(@PathVariable String base64, HttpSession sess) {
		Map<String, Object> result = new HashMap<>();
		try {
			UserVO user = setUserInfo(sess);
			imageService.getImage(base64, user);
			result.put("success", true);
		} catch (SessionNotFoundException | ImageLoadException | IOException e) {
			e.printStackTrace();
			result.put("msg", e.getMessage());
			result.put("success", false);
		}
		return ResponseEntity.ok().body(result);
	}

}
