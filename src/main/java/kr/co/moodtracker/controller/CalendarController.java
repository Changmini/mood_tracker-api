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
import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.service.CalendarService;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.UserVO;

@RestController
public class CalendarController extends CommonController {
	
	@Autowired
	CalendarService calendarService;
	
	@GetMapping(value = "/calendar/{date}")
	public ResponseEntity<?> getCalendarInfo(HttpSession sess, DailyInfoVO vo) {
		Map<String, Object> res = new HashMap<>();
		List<DailyInfoVO> list;
		try {
			setUserInfo(sess, vo);
			list = calendarService.getDailyEntryOfTheMonth(vo);
			res.put("dailyInfoList", list);
		} catch (DataMissingException | SessionNotFoundException e) {
			res.put("msg", e.getMessage());
			e.printStackTrace();
		} 
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping(value = "/daily")
	public ResponseEntity<?> postDailyEntry(HttpSession sess, DailyInfoVO vo) {
		Map<String, Object> res = new HashMap<>(); vo.setUserId(1);
		try {
			setUserInfo(sess, vo);
			calendarService.postDailyInfo(vo);
			res.put("success", true);
			return ResponseEntity.ok().body(res);
		} catch (DataNotInsertedException | SessionNotFoundException  e) {
			res.put("msg", e.getMessage());
			e.printStackTrace();
		}
		res.put("success", false);
		return ResponseEntity.ok().body(res);
	}
	
	@PatchMapping(value = "/daily")
	public ResponseEntity<?> patchDailyEntry(HttpSession sess, DailyInfoVO vo) {
		Map<String, Object> res = new HashMap<>(); vo.setUserId(1);
		try {
			setUserInfo(sess, vo);
			calendarService.patchDailyInfo(vo);
			res.put("success", true);
			return ResponseEntity.ok().body(res);
		} catch (DataNotInsertedException | SessionNotFoundException  e) {
			res.put("msg", e.getMessage());
			e.printStackTrace();
		}
		res.put("success", false);
		return ResponseEntity.ok().body(res);
	}
	
	@DeleteMapping(value = "/daily")
	public ResponseEntity<?> deleteDailyEntry(HttpSession sess, DailyInfoVO vo) {
		Map<String, Object> res = new HashMap<>(); vo.setUserId(1);
		try {
			setUserInfo(sess, vo);
			calendarService.deleteDailyInfo(vo);
			res.put("success", true);
			return ResponseEntity.ok().body(res);
		} catch (DataNotInsertedException | SessionNotFoundException  e) {
			res.put("msg", e.getMessage());
			e.printStackTrace();
		}
		res.put("success", false);
		return ResponseEntity.ok().body(res);
	}

}
