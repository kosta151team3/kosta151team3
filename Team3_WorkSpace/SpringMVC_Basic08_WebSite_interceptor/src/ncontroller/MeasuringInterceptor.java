package ncontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class MeasuringInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
		request.setAttribute("beginTime", System.currentTimeMillis());
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e){
		Long beginTime = (Long)request.getAttribute("beginTime");
		long endTime = System.currentTimeMillis();
		
		System.out.println(request.getRequestURI()+"실행 시간 :"+(endTime-beginTime));
	}

}
