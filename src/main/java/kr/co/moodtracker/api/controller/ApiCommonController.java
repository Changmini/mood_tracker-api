package kr.co.moodtracker.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ApiCommonController {
	
	@RequestMapping("/api")
	public ResponseEntity<Map<String, Object>> getMethodName(
			HttpServletRequest req
	) {
		Map<String, Object> res = new HashMap<>();
		List<String> urlList = new ArrayList<>();
		urlList.add(req.getContextPath() + "/api/calendar/{yyyyMMdd}");
		
		res.put("URL", urlList);
		return ResponseEntity.ok().body(res);
	}
}
