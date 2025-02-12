package kr.co.moodtracker.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.service.UserService;

@RestController
@RequestMapping("/api")
public class ApiUserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/user")
	public ResponseEntity<?> getUser(
			HttpSession sess
	)
	{
		return ResponseEntity.ok().body("노출 API");
	}
	
}
