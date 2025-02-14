package kr.co.moodtracker.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotDeletedException;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.DuplicateDataException;
import kr.co.moodtracker.exception.InvalidApiKeyException;
import kr.co.moodtracker.service.AuthenticationService;
import kr.co.moodtracker.service.CalendarService;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;


@RestController
@RequestMapping("/api")
public class ApiCalendarController {
	
	@Autowired
	CalendarService calendarService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@GetMapping("/calendar/{date}")
	public ResponseEntity<?> getUser(
			DailySearchVO vo
	) throws InvalidApiKeyException, DataMissingException
	{
		authenticationService.validateApiKey(vo.getKey(), vo);
		List<DailyInfoVO> list = calendarService.getDailyInfoOfTheMonth(vo);
		Map<String, Object> res = new HashMap<>();
		res.put("dailyInfoList", list);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping(value = "/daily")
	public ResponseEntity<?> postDailyEntry(
			DailyInfoVO vo
	) throws InvalidApiKeyException, DataNotInsertedException, IllegalStateException
			, IOException, DuplicateDataException
	{
		authenticationService.validateApiKey(vo.getKey(), vo);
		calendarService.postDailyInfo(vo, null);
		Map<String, Object> res = new HashMap<>();
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@PutMapping(value = "/daily")
	public ResponseEntity<?> patchDailyEntry(
			DailyInfoVO vo
	) throws InvalidApiKeyException, DataNotInsertedException, IllegalStateException, IOException
	{
		authenticationService.validateApiKey(vo.getKey(), vo);
		calendarService.putDailyInfo(vo, null, null);
		Map<String, Object> res = new HashMap<>();
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@DeleteMapping(value = "/daily")
	public ResponseEntity<?> deleteDailyEntry(
			DailyInfoVO vo
	) throws InvalidApiKeyException, DataNotDeletedException 
	{
		authenticationService.validateApiKey(vo.getKey(), vo);
		calendarService.deleteDailyInfo(vo);
		Map<String, Object> res = new HashMap<>();
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
}
