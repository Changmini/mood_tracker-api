package kr.co.moodtracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.service.UserService;
import kr.co.moodtracker.vo.UserVO;


@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/user")
	public ResponseEntity<?> getUserInfo(HttpSession sess) {
		Map<String, Object> result = new HashMap<>();
		UserVO user = (UserVO) sess.getAttribute("USER");
		if (user != null) {
			result.put("username", user.getUsername());
			result.put("email", user.getEmail());
			result.put("success", true);
		} else {
			result.put("success", false);
		}
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/user")
	public ResponseEntity<?> createAccount(UserVO vo) {
		Map<String, Object> result = new HashMap<>();
		try {
			userService.postUser(vo);
			result.put("success", true);
		} catch(DataNotInsertedException e) {
			result.put("msg", e.getMessage());
			result.put("success", false);
		}
		return ResponseEntity.ok().body(result);
	}
	
	
}
