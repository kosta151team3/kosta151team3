package ncontroller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dao.MemberDao;
import vo.Member;



@Controller
@RequestMapping("/joinus/")
public class JoinController {
	
	@Autowired
	private MemberDao memberDao;
	
	@RequestMapping(value="join.htm" , method=RequestMethod.GET)
	public String join(){
		System.out.println("회원가입페이지 요청(UI)");
		
		//return "join.jsp";
		  return "joinus.join";
	}
	
	@RequestMapping(value="join.htm" , method=RequestMethod.POST)
	public String join(Member member) throws ClassNotFoundException, SQLException{
		System.out.println("회원가입");
		System.out.println(member.toString());
		
		memberDao.insert(member);
		return "redirect:/index.htm"; //주의 ...
	}
	
	@RequestMapping("login.htm")
	public String login(HttpServletRequest request){
		HttpSession session = request.getSession();
		session.setAttribute("userid", "ture");
		
		return "redirect:/index.htm";
	}
	
	@RequestMapping("logout.htm")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		/*session.setAttribute("userid", "ture");*/
		session.invalidate();
		response.sendRedirect(request.getContextPath()+"/customer/notice.htm");
		//return "customer.notice";
	}
}




