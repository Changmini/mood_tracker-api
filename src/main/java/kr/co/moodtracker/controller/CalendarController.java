package kr.co.moodtracker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ko.co.moodtracker.vo.DailyEntryVO;
import ko.co.moodtracker.vo.SearchVO;
import kr.co.moodtracker.service.CalendarService;

@RestController
public class CalendarController {
	
	 @Autowired
	 CalendarService calendarService;
	
	// @Autowired
	// BroadService broadService;
	
	@GetMapping(value = "/check")
	public String check() {
		return "{result:success}";
	}
	
	@RequestMapping(value = "/calendar")
	public ResponseEntity<?> getCalendarInfo(SearchVO vo) {
		Map<String, Object> res = new HashMap<>();
//		LocalDate.parse("1995-05-09");
		/*
		 * 글(Text)만 1달치를 다 뽑아서 전송해주고 
		 * 사진(picture) 상세 조회를 할 경우에만 개별 API를 통해서 
		 * 가져오자.
		 */
		List<DailyEntryVO> list = calendarService.getDailyboxOfTheMonth(vo);
		res.put("dailyEntryList", list);
		return ResponseEntity.ok().body(res);
	}
	
}
