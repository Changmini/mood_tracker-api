package kr.co.moodtracker.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotDeletedException;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.service.CalendarService;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;

@RestController
public class CalendarController extends CommonController {
	
	@Autowired
	CalendarService calendarService;
	
	@GetMapping(value = "/calendar/{date}")
	public ResponseEntity<?> getCalendarInfo(
			HttpSession sess
			, DailySearchVO vo
	) throws SessionNotFoundException, DataMissingException 
	{
		Map<String, Object> res = new HashMap<>();
		setUserInfo(sess, vo);
		List<DailyInfoVO> list = calendarService.getDailyInfoOfTheMonth(vo);
		res.put("dailyInfoList", list);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@GetMapping(value = "/daily")
	public ResponseEntity<?> timeline(
			HttpSession sess
			, DailySearchVO vo
	)throws SessionNotFoundException, DataMissingException 
	{
		Map<String, Object> res = new HashMap<>();
		setUserInfo(sess, vo);
		res.put("dailyInfoList", calendarService.getDailyInfoList(vo));
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping(value = "/daily")
	public ResponseEntity<?> postDailyEntry(
			HttpSession sess
			, DailyInfoVO vo
			, List<MultipartFile> files
	) throws SessionNotFoundException, DataNotInsertedException, IllegalStateException, IOException 
	{
		Map<String, Object> res = new HashMap<>();
		setUserInfo(sess, vo);
		calendarService.postDailyInfo(vo, files);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@PatchMapping(value = "/daily")
	public ResponseEntity<?> patchDailyEntry(
			HttpSession sess, DailyInfoVO vo
			, List<MultipartFile> files
			, @RequestParam("preImageId") List<Integer> preImageId
	) throws SessionNotFoundException, DataNotInsertedException, IllegalStateException, IOException 
	{
		Map<String, Object> res = new HashMap<>();
		setUserInfo(sess, vo);
		calendarService.patchDailyInfo(vo, files, preImageId);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@DeleteMapping(value = "/daily")
	public ResponseEntity<?> deleteDailyEntry(
			HttpSession sess
			, DailyInfoVO vo
	) throws SessionNotFoundException, DataNotDeletedException 
	{
		Map<String, Object> res = new HashMap<>();
		setUserInfo(sess, vo);
		calendarService.deleteDailyInfo(vo);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}

}
