package kr.co.moodtracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.service.UserService;
import kr.co.moodtracker.vo.UserVO;


@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/login")
	public ResponseEntity<?> requestMethodName(HttpSession sess, Map<String,String> vo) {
		Map<String, Object> result = new HashMap<>();
		UserVO user = userService.getUser(vo);
		if (user != null) {
			sess.setAttribute("USER", user);
			result.put("success", true);
		} else {
			result.put("success", false);
		}
		return ResponseEntity.ok().body(result);
	}
	
	
}
