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
import org.springframework.web.bind.annotation.PutMapping;
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
		setUserId(sess, vo);
		Map<String, Object> res = new HashMap<>();
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
		setUserId(sess, vo);
		Map<String, Object> res = new HashMap<>();
		res.put("dailyInfoList", calendarService.getDailyInfoList(vo));
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping(value = "/daily")
	public ResponseEntity<?> postDailyEntry(
			HttpSession sess
			, DailyInfoVO vo
			, @RequestParam("files") List<MultipartFile> files
	) throws SessionNotFoundException, DataNotInsertedException, IllegalStateException, IOException 
	{
		setUserId(sess, vo);
		Map<String, Object> res = new HashMap<>();
		calendarService.postDailyInfo(vo, files);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@PutMapping(value = "/daily")
	public ResponseEntity<?> patchDailyEntry(
			HttpSession sess
			, DailyInfoVO vo
			, @RequestParam("files") List<MultipartFile> files
			, @RequestParam("preImageId") List<Integer> preImageId
	) throws SessionNotFoundException, DataNotInsertedException, IllegalStateException, IOException 
	{
		setUserId(sess, vo);
		Map<String, Object> res = new HashMap<>();
		calendarService.putDailyInfo(vo, files, preImageId);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}
	
	@DeleteMapping(value = "/daily")
	public ResponseEntity<?> deleteDailyEntry(
			HttpSession sess
			, DailyInfoVO vo
	) throws SessionNotFoundException, DataNotDeletedException 
	{
		setUserId(sess, vo);
		Map<String, Object> res = new HashMap<>();
		calendarService.deleteDailyInfo(vo);
		res.put("success", true);
		return ResponseEntity.ok().body(res);
	}

}
