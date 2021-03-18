package com.example.ably.controllers;

import com.example.ably.AblyConfig;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.rest.Auth;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.Capability;
import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AblyAuthController
{
	@Autowired
	AblyConfig ablyConfig;

	private AblyRest ablyRest;

//	@RequestMapping("/auth")
//	public String auth(HttpServletRequest request, HttpServletResponse response) throws AblyException
//	{
//		String username = null;
//		Cookie[] cookies = request.getCookies();
//		for (Cookie cookie : cookies) {
//			if (cookie.getName().equalsIgnoreCase("username")) {
//				username = cookie.getValue();
//				break;
//			}
//		}
//		Auth.TokenParams tokenParams = getTokenParams(username);
//		return createTokenRequest(tokenParams, response);
//	}
//
//	@RequestMapping(value = "/login", method = RequestMethod.GET)
//	public String login(@RequestParam(name = "username", defaultValue = "anonymous")
//							String username, HttpServletResponse response) throws IOException
//	{
//		response.addCookie(new Cookie("username", username));
//		response.sendRedirect("/");
//		return "redirect:/";
//	}
//
//	@RequestMapping(value = "/logout", method = RequestMethod.GET)
//	public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		for(Cookie cookie : request.getCookies()) {
//			if(cookie.getName().equalsIgnoreCase("username")) {
//				cookie.setValue(null);
//				cookie.setMaxAge(0);
//				cookie.setPath(request.getContextPath());
//				response.addCookie(cookie);
//			}
//		}
//		response.sendRedirect("/");
//		return "redirect:/";
//	}

	public Auth.TokenParams getTokenParams(String username) throws AblyException
	{
		Auth.TokenParams tokenParams = new Auth.TokenParams();
		tokenParams.capability = Capability.c14n("{ '*': ['subscribe'] }");
		if (username != null) {
			tokenParams.clientId = username;
		}
		return tokenParams;
	}

	public String createTokenRequest(Auth.TokenParams tokenParams, HttpServletResponse response) {
		Auth.TokenRequest tokenRequest;
		try {
			tokenRequest = getAblyRest().auth.createTokenRequest(tokenParams, null);
			response.setHeader("Content-Type", "application/json");
			return tokenRequest.asJson();
		} catch (AblyException e) {
			response.setStatus(500);
			return "Error requesting token: " + e.getMessage();
		}
	}

	private AblyRest getAblyRest() throws AblyException {
		if (ablyRest == null) {
			ablyRest = ablyConfig.ablyRest();
		}
		return ablyRest;
	}
}
