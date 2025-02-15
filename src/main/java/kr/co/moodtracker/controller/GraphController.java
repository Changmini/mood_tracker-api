package kr.co.moodtracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.moodtracker.exception.SessionNotFoundException;
import kr.co.moodtracker.handler.AuthUserHandler;
import kr.co.moodtracker.service.GraphService;
import kr.co.moodtracker.vo.SearchDailyInfoVO;

@RestController
@RequestMapping("/view/graph")
public class GraphController {
	
	@Autowired
	GraphService graphService;
	
	@GetMapping("/mood-level-data")
	public ResponseEntity<?> getMoodLevelData(
			HttpSession sess
			, SearchDailyInfoVO vo
	) throws Exception
	{
		AuthUserHandler.setUserId(sess, vo);
		Map<String, Object> result = new HashMap<>();
		Map<?,?> graphData = graphService.getMoodLevelData(vo);
		result.put("graph", graphData);
		result.put("success", true);
		return ResponseEntity.ok().body(result);
	}
}
