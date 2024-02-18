package kr.co.moodtracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalendarController {
	
	// @Autowired
	// CalendarService calendarService;
	
	// @Autowired
	// BroadService broadService;
	
	@GetMapping(value = "/check")
	public String check() {
		return "{result:success}";
	}
	
	@PostMapping(value = "/calendar")
	public ResponseEntity<?> getCalendarInfo(String date) {
//		LocalDate.parse("1995-05-09");
		/*
		 * 글(Text)만 1달치를 다 뽑아서 전송해주고 
		 * 사진(picture) 상세 조회를 할 경우에만 개별 API를 통해서 
		 * 가져오자.
		 */
		return null;
	}
	
}
