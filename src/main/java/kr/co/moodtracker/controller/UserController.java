package kr.co.moodtracker.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.DataNotUpdatedException;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.handler.AuthUserHandler;
import kr.co.moodtracker.service.UserService;
import kr.co.moodtracker.vo.ProfileVO;
import kr.co.moodtracker.vo.UserVO;

@RestController
@RequestMapping("/view")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/user")
	public ResponseEntity<?> getUser(
			HttpSession sess
	)
	{
		Map<String, Object> result = new HashMap<>();
		UserVO user = (UserVO) sess.getAttribute("USER");
		if (user != null) {
			result.put("username", user.getUsername());
			result.put("email", user.getEmail());
			result.put("nickname", user.getNickname());
			result.put("success", true);
		} else {
			result.put("success", false);
		}
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/user")
	public ResponseEntity<?> createAccount(
			UserVO vo
	) throws DataNotInsertedException 
	{
		Map<String, Object> result = new HashMap<>();
		userService.postUser(vo);
		result.put("success", true);
		return ResponseEntity.ok().body(result);
	}
	
	@PatchMapping("/user")
	public ResponseEntity<?> patchUser(
			UserVO vo
	) throws DataNotUpdatedException 
	{
		Map<String, Object> result = new HashMap<>();
		userService.patchUser(vo);
		result.put("success", true);
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/user/profile")
	public ResponseEntity<?> getUserProfile(
			HttpSession sess, UserVO vo
	) throws SessionNotFoundException 
	{
		UserVO user = AuthUserHandler.getUserInfo(sess);
		Map<String, Object> result = new HashMap<>();
		ProfileVO profile = userService.getUserProfile(user.getUserId());
		result.put("profile", profile);
		result.put("success", profile != null ? true : false);
		return ResponseEntity.ok().body(result);
	}
	
	@PatchMapping("/user/profile")
	public ResponseEntity<?> patchUserProfile(
			HttpSession sess
			, UserVO vo
	) throws SessionNotFoundException, DataNotUpdatedException 
	{
		UserVO user = AuthUserHandler.getUserInfo(sess);
		Map<String, Object> result = new HashMap<>();
		userService.patchUserProfile(user.getUserId(), vo);
		result.put("success", true);
		return ResponseEntity.ok().body(result);
	}
	
	@PutMapping("/user/profile/image")
	public ResponseEntity<?> patchUserProfile(
			HttpSession sess
			, @RequestParam("file") MultipartFile file
			, UserVO vo
	) throws SessionNotFoundException, DataNotUpdatedException, IllegalStateException, IOException 
	{
		UserVO user = AuthUserHandler.getUserInfo(sess);
		Map<String, Object> result = new HashMap<>();
		String base64 = userService.putUserProfileImage(file, user.getUserId(), vo);
		result.put("imagePath", base64);
		result.put("success", true);
		return ResponseEntity.ok().body(result);
	}
	
}
