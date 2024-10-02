package kr.co.moodtracker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.service.CalendarService;
import kr.co.moodtracker.service.NeighborService;
import kr.co.moodtracker.vo.NeighborVO;
import kr.co.moodtracker.vo.SearchNeighborVO;
import kr.co.moodtracker.vo.UserVO;


@RestController
public class NeighborController extends CommonController {
	
	@Autowired
	NeighborService neighborService;
	
	@Autowired
	CalendarService calendarService;
	
	@GetMapping("/neighbors")
	public ResponseEntity<?> getNeighbors(HttpSession sess) 
			throws SessionNotFoundException {
		Map<String, Object> result = new HashMap<>();
		UserVO user = setUserInfo(sess);
		List<NeighborVO> neighbors =  neighborService.getNeighbors(user.getUserId());
		result.put("neighbors", neighbors);
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/neighbor/{nickname}")
	public ResponseEntity<?> postNeighbor(HttpSession sess, SearchNeighborVO vo) 
			throws SessionNotFoundException {
		Map<String, Object> result = new HashMap<>();
		UserVO user = setUserInfo(sess);
		try {
			neighborService.postNeighbor(user.getUserId(), vo);
			result.put("success", true);
		} catch (DataNotInsertedException e) {
			result.put("msg", e.getMessage());
			result.put("success", false);
		}
		return ResponseEntity.ok().body(result);
	}
	
	@PatchMapping("/neighbor")
	public ResponseEntity<?> patchNeighbor(HttpSession sess, SearchNeighborVO vo) 
			throws SessionNotFoundException {
		Map<String, Object> result = new HashMap<>();
		UserVO user = setUserInfo(sess);
		neighborService.patchNeighbor(user.getUserId(), vo);
		result.put("success", true);
		return ResponseEntity.ok().body(result);
	}
	
	@PatchMapping("/neighbor/synchronize")
	public ResponseEntity<?> setSynchronize(HttpSession sess, SearchNeighborVO vo) 
			throws SessionNotFoundException {
		Map<String, Object> result = new HashMap<>();
		UserVO user = setUserInfo(sess);
		neighborService.synchronize(user.getUserId(), vo);
		result.put("success", true);
		return ResponseEntity.ok().body(result);
	}
	
	@DeleteMapping("/neighbor")
	public ResponseEntity<?> deleteNeighbor(HttpSession sess, SearchNeighborVO vo) 
			throws SessionNotFoundException {
		Map<String, Object> result = new HashMap<>();
		UserVO user = setUserInfo(sess);
		neighborService.deleteNeighbor(user.getUserId(), vo);
		result.put("success", true);
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/neighbor/{neighborId}/calendar")
	public ResponseEntity<?> getNeighborCalendar(HttpSession sess, SearchNeighborVO vo) 
			throws SessionNotFoundException {
		Map<String, Object> result = new HashMap<>();
		UserVO user = setUserInfo(sess);
		
		result.put("success", true);
		return ResponseEntity.ok().body(result);
	}
}
