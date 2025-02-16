package kr.co.moodtracker.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ApiCommonController {
	
	/**
	 * description
	 * @param req
	 * @return
	 */
	@GetMapping("/api/v1")
	public ResponseEntity<Map<String, Object>> getMethodName(
			HttpServletRequest req
	) {
		String url = req.getRequestURL().toString();
		int idx = url.indexOf("/api");
		String contextPath = url.substring(0, idx);
		
		Map<String, Object> res = new HashMap<>();
		Map<String, Object> links = new LinkedHashMap<>();
		Map<String, Object> details = null;
		List<String> type = null;
		List<String> reqBody = null;
		
		details = new LinkedHashMap<>();
		details.put("href", contextPath+"/api/v1");
		links.put("self", details);
		
		details = new LinkedHashMap<>();
		details.put("href", contextPath+"/api/v1/calendar/{*yyyy-MM}");
		type = new ArrayList<>();
		type.add("list");
		details.put("type", type);
		reqBody = new ArrayList<>();
		reqBody.add("*key");
		details.put("request-payload", reqBody);
		links.put("calendar-date", details);
		
		details = new LinkedHashMap<>();
		details.put("href", contextPath+"/api/v1/daily/{*yyyy-MM-dd}");
		type = new ArrayList<>();
		type.add("select");
		type.add("insert");
		type.add("update");
		details.put("type", type);
		reqBody = new ArrayList<>();
		reqBody.add("*key");
		reqBody.add("*moodLevel");
		reqBody.add("noteTitle");
		reqBody.add("noteContent");
		details.put("request-payload", reqBody);
		links.put("daily-date", details);
		
		res.put("_links", links);
		return ResponseEntity.ok().body(res);
	}
	
	
}
