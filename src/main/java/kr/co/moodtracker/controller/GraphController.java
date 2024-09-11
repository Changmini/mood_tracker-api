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
import kr.co.moodtracker.service.GraphService;
import kr.co.moodtracker.vo.DailySearchVO;
import kr.co.moodtracker.vo.UserVO;

@RequestMapping("/graph")
@RestController
public class GraphController extends CommonController {
	
	@Autowired
	GraphService graphService;
	
	@GetMapping("/mood-level-data")
	public ResponseEntity<?> getMoodLevelData(HttpSession sess, DailySearchVO vo)
			throws SessionNotFoundException {
		Map<String, Object> result = new HashMap<>();
		try {
			UserVO user = setUserInfo(sess);
			Map<?,?> graphData = graphService.getMoodLevelData(user.getUserId(), vo);
			result.put("graph", graphData);
			result.put("success", true);
            return ResponseEntity.ok().body(result);
		} catch(Exception e) { e.printStackTrace(); }
		result.put("success", false);
		return ResponseEntity.ok().body(result);
	}
}
