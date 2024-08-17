package kr.co.moodtracker.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.ImageLoadException;
import kr.co.moodtracker.service.ImageService;
import kr.co.moodtracker.vo.UserVO;

@Controller
public class ImageController extends CommonController {
	
	@Autowired
	ImageService imageService;
	
	@GetMapping(value = "/image")
	public ResponseEntity<?> getImage(String path, HttpSession sess) {
		Map<String, Object> result = new HashMap<>();
		try {
//			UserVO user = setUserInfo(sess);
			UserVO user = new UserVO(); user.setUserId(1);
			byte[] imageData = imageService.getImage(path, user);
            ByteArrayResource resource = new ByteArrayResource(imageData);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE) // 이미지 타입에 맞게 설정
                    .body(resource);
		} catch (/*SessionNotFoundException |*/ ImageLoadException | IOException e) {
			e.printStackTrace();
			result.put("msg", e.getMessage());
			result.put("success", false);
		}
		return ResponseEntity.ok().body(result);
	}

}
