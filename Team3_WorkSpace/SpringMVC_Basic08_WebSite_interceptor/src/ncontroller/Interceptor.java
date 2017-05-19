package ncontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class Interceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler) 
					throws Exception{
		
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("userid");
		
		if(id!=null){
			System.out.println("true");
			return true;
		}else{
			System.out.println("false");
			response.sendRedirect(request.getContextPath()+"/index.htm");
			return false;
		}
	}
}
