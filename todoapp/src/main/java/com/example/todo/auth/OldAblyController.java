//package com.example.todo.auth;
//
//import java.io.IOException;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import io.ably.lib.rest.AblyRest;
//import io.ably.lib.rest.Auth;
//import io.ably.lib.types.AblyException;
//import io.ably.lib.types.Capability;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//public class AuthController
//{
//    private AblyRest ablyRest;
//    @Value( "${ABLY_API_KEY}" )
//    private void setAblyRest(String apiKey) throws AblyException {
//        ablyRest = new AblyRest(apiKey);
//    }
//
//    @RequestMapping("/auth")
//    public String auth(HttpServletRequest request, HttpServletResponse response) throws AblyException
//    {
//        String oldToken = null;
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equalsIgnoreCase("token")) {
//                oldToken = cookie.getValue();
//                break;
//            }
//        }
//        if (oldToken == null)
//        {
//            return null;
//        }
//
//        Auth.TokenDetails tokenDetails = Auth.TokenDetails.fromJson(oldToken);
//        String username = tokenDetails.clientId;
//
//        Auth.TokenParams tokenParams = getTokenParams(username);
//        return createTokenRequest(tokenParams, response);
//    }
//
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public String login(@RequestParam(name = "username", defaultValue = "anonymous") String username, HttpServletResponse response) throws IOException, AblyException {
//        /* Login the user without credentials. This is an over simplified authentication system to keep this tutorial simple */
//        Auth.TokenParams tokenParams = getTokenParams(username);
//        String ablyToken = createTokenRequest(tokenParams, response);
//        System.out.println(ablyToken);
//        response.addCookie(new Cookie("token", ablyToken));
//        response.sendRedirect("/");
//        return "redirect:/";
//    }
//
//    /* Clear the cookie when the user logs outs */
//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        for (Cookie cookie : request.getCookies()) {
//            if (cookie.getName().equalsIgnoreCase("token")) {
//                cookie.setValue(null);
//                cookie.setMaxAge(0);
//                cookie.setPath(request.getContextPath());
//                response.addCookie(cookie);
//            }
//        }
//        response.sendRedirect("/");
//        return "redirect:/";
//    }
//
//    public Auth.TokenParams getTokenParams(String username) throws AblyException
//    {
//        Auth.TokenParams tokenParams = new Auth.TokenParams();
//        tokenParams.capability = Capability.c14n("{ '*': ['subscribe'] }");
//        if (username != null) {
//            tokenParams.clientId = username;
//        }
//        return tokenParams;
//    }
//
//    public String createTokenRequest(Auth.TokenParams tokenParams, HttpServletResponse response) {
//        Auth.TokenDetails tokenRequest;
//        try {
//            tokenRequest = ablyRest.auth.requestToken(tokenParams, null);
//            response.setHeader("Content-Type", "application/json");
//            return tokenRequest.token;
//        } catch (AblyException e) {
//            response.setStatus(500);
//            return "Error requesting token: " + e.getMessage();
//        }
//    }
//}
