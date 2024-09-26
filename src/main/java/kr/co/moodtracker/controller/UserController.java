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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.DataNotUpdatedException;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.service.UserService;
import kr.co.moodtracker.vo.ProfileVO;
import kr.co.moodtracker.vo.UserVO;


@RestController
public class UserController extends CommonController {
	
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
	
	@GetMapping("/user/profile")
	public ResponseEntity<?> getUserProfile(HttpSession sess, UserVO vo)
			throws SessionNotFoundException {
		Map<String, Object> result = new HashMap<>();
		UserVO user = setUserInfo(sess);
		ProfileVO profile = userService.getUserProfile(user.getUserId());
		result.put("profile", profile);
		result.put("success", profile != null ? true : false);
		return ResponseEntity.ok().body(result);
	}
	
	@PatchMapping("/user/profile")
	public ResponseEntity<?> patchUserProfile(HttpSession sess, UserVO vo) 
			throws SessionNotFoundException {
		Map<String, Object> result = new HashMap<>();
		UserVO user = setUserInfo(sess);
		try {
			userService.patchUserProfile(user.getUserId(), vo);
			result.put("success", true);
		} catch (DataNotUpdatedException e) {
			result.put("msg", e.getMessage());
			result.put("success", false);
		}
		return ResponseEntity.ok().body(result);
	}
	
	@PutMapping("/user/profile/image")
	public ResponseEntity<?> patchUserProfile(HttpSession sess, MultipartFile file, UserVO vo) 
			throws SessionNotFoundException {
		Map<String, Object> result = new HashMap<>();
		UserVO user = setUserInfo(sess);
		try {
			String base64 = userService.putUserProfileImage(file, user.getUserId(), vo);
			result.put("path", base64);
			result.put("success", true);
		} catch (DataNotUpdatedException e) {
			result.put("msg", e.getMessage());
			result.put("success", false);
		} catch (IllegalStateException | IOException e1) {
			e1.printStackTrace();
			result.put("msg", "서버 이상으로 인한 프로필 이미지 등록 실패");
			result.put("success", false);
		}
		return ResponseEntity.ok().body(result);
	}
	
}
