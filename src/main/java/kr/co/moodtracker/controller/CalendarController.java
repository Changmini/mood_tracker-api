package kr.co.moodtracker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.service.CalendarService;
import kr.co.moodtracker.service.TestService;
import kr.co.moodtracker.vo.DailyEntryVO;

@RestController
public class CalendarController {
	
	@Autowired
	TestService testService;
	
	@Autowired
	CalendarService calendarService;
	
	@GetMapping(value = "/check")
	public String check() {
		return "{result:success}";
	}
	
	@GetMapping(value = "/calendar")
	public ResponseEntity<?> getCalendarInfo(DailyEntryVO vo) {
		Map<String, Object> res = new HashMap<>();
		/*
		 * 글(Text)만 1달치를 다 뽑아서 전송해주고 
		 * 사진(picture) 상세 조회를 할 경우에만 개별 API를 통해서 
		 * 가져오자.
		 */
		List<DailyEntryVO> list;
		try {
			list = calendarService.getDailyEntryOfTheMonth(vo);
			res.put("dailyEntryList", list);
		} catch (DataMissingException e) {
			res.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping(value = "/daily")
	public ResponseEntity<?> postDailyEntry(DailyEntryVO vo) {
		Map<String, Object> res = new HashMap<>();
		try {
			calendarService.postDailyEntry(vo);
			res.put("success", true);
			return ResponseEntity.ok().body(res);
		} catch (DataNotInsertedException  e) {
			res.put("msg", e.getMessage());
			e.printStackTrace();
		}
		res.put("success", false);
		return ResponseEntity.ok().body(res);
	}
	
	@RequestMapping("/test")
	public ResponseEntity<?> test() {
		testService.getUser();
		return ResponseEntity.ok().body("ok");
	}
}
