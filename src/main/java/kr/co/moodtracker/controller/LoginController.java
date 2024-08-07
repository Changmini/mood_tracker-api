package kr.co.moodtracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.service.UserService;
import kr.co.moodtracker.vo.UserVO;


@RestController
public class LoginController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(HttpSession sess, @RequestParam Map<String,String> vo) {
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
	
	@GetMapping("/login/status")
	public ResponseEntity<?> loginStatus(HttpSession sess) {
		Map<String, Object> result = new HashMap<>();
		UserVO user = (UserVO) sess.getAttribute("USER");
		if (user != null)
			result.put("success", true);
		else 
			result.put("success", false);
		return ResponseEntity.ok().body(result);
	}
}
