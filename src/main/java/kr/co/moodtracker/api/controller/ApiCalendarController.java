package kr.co.moodtracker.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.moodtracker.enums.MethodType;
import kr.co.moodtracker.exception.DataMissingException;
import kr.co.moodtracker.exception.DataNotInsertedException;
import kr.co.moodtracker.exception.DuplicateDataException;
import kr.co.moodtracker.exception.InvalidApiKeyException;
import kr.co.moodtracker.service.AuthenticationService;
import kr.co.moodtracker.service.CalendarService;
import kr.co.moodtracker.vo.DailyInfoVO;
import kr.co.moodtracker.vo.DailySearchVO;


@RestController
@RequestMapping("/api/v1")
public class ApiCalendarController {
	
	@Autowired
	CalendarService calendarService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@GetMapping("/calendar/{date}")
	public ResponseEntity<?> getUser(
			HttpServletRequest req
			, DailySearchVO vo
	) throws InvalidApiKeyException, DataMissingException
	{
		authenticationService.validateApiKey(vo.getKey(), vo);		
		List<DailyInfoVO> list = calendarService.getDailyInfoOfTheMonth(vo);
		
		Map<String, Object> res = new HashMap<>();
		res.put("dailyInfoList", list);
		res.put("status", "success");
		res.put("message", "OK");
		res.put("links", this.allLinks(req, MethodType.SELECT, vo.getDate()));
		return ResponseEntity.ok().body(res);
	}
	
	/**
	 * RequestBody : noteTitle, noteContent, moodLevel
	 * @param req
	 * @param vo
	 * @return
	 * @throws InvalidApiKeyException
	 * @throws DataNotInsertedException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws DuplicateDataException
	 */
	@PostMapping(value = "/daily/{date}")
	public ResponseEntity<?> postDailyEntry(
			HttpServletRequest req
			, DailyInfoVO vo
	) throws InvalidApiKeyException, DataNotInsertedException, IllegalStateException
			, IOException, DuplicateDataException
	{
		authenticationService.validateApiKey(vo.getKey(), vo);
		calendarService.postDailyInfo(vo, null);
		
		Map<String, Object> res = new HashMap<>();
		res.put("status", "success");
		res.put("message", "Item created successfully");
		res.put("links", this.allLinks(req, MethodType.INSERT, vo.getDate()));
		return ResponseEntity.ok().body(res);
	}
	
	/**
	 * RequestBody : noteTitle, noteContent, moodLevel
	 * @param req
	 * @param vo
	 * @return
	 * @throws InvalidApiKeyException
	 * @throws DataNotInsertedException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@PutMapping(value = "/daily/{date}")
	public ResponseEntity<?> putDailyEntry(
			HttpServletRequest req
			, DailyInfoVO vo
			) throws InvalidApiKeyException, DataNotInsertedException, IllegalStateException, IOException
	{
		authenticationService.validateApiKey(vo.getKey(), vo);
		calendarService.putDailyInfoWithImageJobExclusion(vo);
		
		Map<String, Object> res = new HashMap<>();
		res.put("status", "success");
		res.put("message", "Item updated successfully");
		res.put("links", this.allLinks(req, MethodType.UPDATE, vo.getDate()));
		return ResponseEntity.ok().body(res);
	}
	
	/**
	 * RequestBody : noteTitle, noteContent, moodLevel
	 * @param req
	 * @param vo
	 * @return
	 * @throws InvalidApiKeyException
	 * @throws DataNotInsertedException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@PatchMapping(value = "/daily/{date}")
	public ResponseEntity<?> patchDailyEntry(
			HttpServletRequest req
			, DailyInfoVO vo
	) throws InvalidApiKeyException, DataNotInsertedException, IllegalStateException, IOException
	{
		authenticationService.validateApiKey(vo.getKey(), vo);
		calendarService.patchDailyInfo(vo, null, null);
		
		Map<String, Object> res = new HashMap<>();
		res.put("status", "success");
		res.put("message", "Item updated successfully");
		res.put("links", this.allLinks(req, MethodType.UPDATE, vo.getDate()));
		return ResponseEntity.ok().body(res);
	}
	
	private List<?> allLinks(
			HttpServletRequest req
			, MethodType pos
			, String date
	) {
		String url = req.getRequestURL().toString();
		int idx = url.indexOf("/api");
		String contextPath = url.substring(0, idx);
		
		List<Map<?,?>> links = new ArrayList<>();
		
		Map<String, Object> link = new HashMap<>();
		link.put("ref", pos == MethodType.SELECT ? "self" : "select");
		link.put("href", contextPath+"/api/v1/calendar/"+date);
		links.add(link);
		
		link = new HashMap<>();
		link.put("ref", pos == MethodType.INSERT ? "self" : "insert");
		link.put("href", contextPath+"/api/v1/daily/"+date);
		links.add(link);
		
		link = new HashMap<>();
		link.put("ref", pos == MethodType.UPDATE ? "self" : "update");
		link.put("href", contextPath+"/api/v1/daily/"+date);
		links.add(link);
		
		link = new HashMap<>();
		link.put("ref", pos == MethodType.DELETE ? "self" : "delete");
		link.put("href", contextPath+"/api/v1/daily/"+date);
		links.add(link);
		
		return links;
	}
	
}
