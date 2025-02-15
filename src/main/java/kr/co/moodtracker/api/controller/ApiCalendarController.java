package kr.co.moodtracker.api.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import kr.co.moodtracker.handler.DateHandler;
import kr.co.moodtracker.service.AuthenticationService;
import kr.co.moodtracker.service.CalendarService;
import kr.co.moodtracker.vo.ReturnDailyInfoVO;
import kr.co.moodtracker.vo.SearchDailyInfoVO;

@RestController
@RequestMapping("/api/v1")
public class ApiCalendarController {
	
	@Autowired
	CalendarService calendarService;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@GetMapping("/calendar/{date}")
	public ResponseEntity<?> getCalendar(
			HttpServletRequest req
			, SearchDailyInfoVO vo
	) throws InvalidApiKeyException, DataMissingException
	{
		authenticationService.validateApiKey(vo.getKey(), vo);		
		List<ReturnDailyInfoVO> list = calendarService.getDailyInfoOfTheMonth(vo);
		
		Map<String, Object> res = new HashMap<>();
		res.put("dailyInfoList", list);
		res.put("status", "success");
		res.put("message", "OK");
		res.put("links", this.allLinks(req, MethodType.SELECT_LIST, vo.getDate()));
		return ResponseEntity.ok().body(res);
	}
	
	@GetMapping("/daily/{date}")
	public ResponseEntity<?> getDailyInfo(
			HttpServletRequest req
			, SearchDailyInfoVO vo
	) throws InvalidApiKeyException, DataMissingException
	{
		authenticationService.validateApiKey(vo.getKey(), vo);		
		ReturnDailyInfoVO daily = calendarService.getDailyInfo(vo);
		
		Map<String, Object> res = new HashMap<>();
		res.put("dailyInfo", daily);
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
			, SearchDailyInfoVO vo
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
			, SearchDailyInfoVO vo
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
			, SearchDailyInfoVO vo
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
		String _date = null;
		if (date.length() > 7) {
			_date = date.substring(0, 10);
		} else  {
			_date = date.substring(0, 7) + String.format("-%02d", LocalDate.now().getDayOfMonth());

		}
			
		
		List<Map<?,?>> links = new ArrayList<>();
		List<String> payload = null;
		Map<String, Object> link = null;
		
		link = new LinkedHashMap<>();
		link.put("ref", pos == MethodType.SELECT_LIST ? "self" : "select");
		link.put("href", contextPath+"/api/v1/calendar/"+_date.substring(0, 7)+"?key=");
		payload = new ArrayList<>();
		payload.add("*key");
		link.put("payload", payload);
		links.add(link);
		
		link = new LinkedHashMap<>();
		link.put("ref", pos == MethodType.SELECT ? "self" : "select");
		link.put("href", contextPath+"/api/v1/daily/"+_date+"?key=");
		payload = new ArrayList<>();
		payload.add("*key");
		link.put("payload", payload);
		links.add(link);
		
		link = new LinkedHashMap<>();
		link.put("ref", pos == MethodType.INSERT ? "self" : "insert");
		link.put("href", contextPath+"/api/v1/daily/"+_date+"?key=&moodLevel=50&noteTitle=&noteContent=");
		payload = new ArrayList<>();
		payload.add("*key");
		payload.add("*moodLevel");
		payload.add("noteTitle");
		payload.add("noteContent");
		link.put("payload", payload);
		links.add(link);
		
		link = new LinkedHashMap<>();
		link.put("ref", pos == MethodType.UPDATE ? "self" : "update");
		link.put("href", contextPath+"/api/v1/daily/"+_date+"?key=&moodLevel=50&noteTitle=&noteContent=");
		payload = new ArrayList<>();
		payload.add("*key");
		payload.add("*moodLevel");
		payload.add("noteTitle");
		payload.add("noteContent");
		link.put("payload", payload);
		links.add(link);
		
		return links;
	}
	
}
