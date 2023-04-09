//package com.steelman.iot.platform.config;
//
//import com.alibaba.fastjson.JSONObject;
//import com.steelman.iot.platform.entity.User;
//import com.steelman.iot.platform.service.UserService;
//import com.steelman.iot.platform.utils.GlobalMap;
//import com.steelman.iot.platform.utils.Result;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.PrintWriter;
//
//
//@Component
//public class AppInterceptor implements HandlerInterceptor {
//
//	@Resource
//	private UserService userService;
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		boolean result = true;
//		String token = request.getParameter("token");
//			if (token != null && !"".equals(token)) {
//				User user = GlobalMap.userMap.get(token);
//				if (user == null) {
//					user = userService.findByUserToken(token);
//					if (user == null) {
//						response.setCharacterEncoding("UTF-8");
//						response.setContentType("application/json; charset=utf-8");
//						PrintWriter out = null;
//						try {
//							JSONObject jsonStu = (JSONObject) JSONObject.toJSON(Result.MissingToken());
//							out = response.getWriter();
//							out.append(jsonStu.toString());
//							return false;
//						} catch (Exception e) {
//							e.printStackTrace();
//							response.sendError(500);
//							return false;
//						}
//					}
//					GlobalMap.userMap.put(token,user);
//				}
//
//			} else {
//				response.setCharacterEncoding("UTF-8");
//				response.setContentType("application/json; charset=utf-8");
//				PrintWriter out = null;
//				try {
//					JSONObject jsonStu = (JSONObject) JSONObject.toJSON(Result.MissingToken());
//					out = response.getWriter();
//					out.append(jsonStu.toString());
//					return false;
//				} catch (Exception e) {
//					e.printStackTrace();
//					response.sendError(500);
//					return false;
//				}
//			}
//		return result;
//	}
//
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//			ModelAndView modelAndView) throws Exception {
//		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//	}
//
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//			throws Exception {
//		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//	}
//
//}
