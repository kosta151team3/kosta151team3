package ncontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	//index.htm 요청 View(index.jsp)
	
	@RequestMapping("index.htm")
	public String index(){
		
		//Tiles 이전
		//return "index.jsp";
		return "home.index";
	}
}
