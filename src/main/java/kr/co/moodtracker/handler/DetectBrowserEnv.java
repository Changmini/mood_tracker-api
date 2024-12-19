package kr.co.moodtracker.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.moodtracker.controller.ExceptionController;

public class DetectBrowserEnv implements HandlerInterceptor {
	
	private static Logger logger = LoggerFactory.getLogger(ExceptionController.class);
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler
			) throws Exception 
	{
		logger.info(
				getRequestInfo(req)
			);
		return true;
	}
	
    public static String getRequestInfo(HttpServletRequest request) {
        // 요청 URL 정보
        String requestURL = request.getRequestURL().toString();

        // 요청자의 IP 주소
        String ipAddress = getClientIpAddress(request);

        // User-Agent 정보
        String userAgent = request.getHeader("User-Agent");

        String browser = "Unknown";
        String os = "Unknown";

        // 브라우저 정보 감지
        if (userAgent.contains("Chrome")) {
            browser = "Chrome";
        } else if (userAgent.contains("Firefox")) {
            browser = "Firefox";
        } else if (userAgent.contains("Safari")) {
            browser = "Safari";
        } else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            browser = "Internet Explorer";
        }

        // 운영체제 정보 감지
        if (userAgent.contains("Windows")) {
            os = "Windows";
        } else if (userAgent.contains("Mac")) {
            os = "MacOS";
        } else if (userAgent.contains("Linux")) {
            os = "Linux";
        } else if (userAgent.contains("Android")) {
            os = "Android";
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            os = "iOS";
        }

        // 요청자 IP 주소, URL, 브라우저, 운영체제 정보 반환
        return String.format("IP: %s, Request URL: %s, Browser: %s, OS: %s", ipAddress, requestURL, browser, os);
    }
	
    // 클라이언트 IP 주소를 추출하는 메서드
    private static String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }
	
}
