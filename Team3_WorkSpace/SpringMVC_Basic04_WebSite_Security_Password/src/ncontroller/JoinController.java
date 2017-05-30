package ncontroller;

import java.security.Principal;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dao.MemberDao;
import dao.RollDAO;
import vo.Member;
import vo.Roll;

@Controller
@RequestMapping("/joinus/")
public class JoinController {

	@Autowired
	private SqlSession sqlsession;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	@RequestMapping(value="join.htm" , method=RequestMethod.GET)
	public String join(){
		System.out.println("회원가입페이지 요청(UI)");
		//return "join.jsp";
		return "joinus.join";
	}
	
	@RequestMapping(value="join.htm" , method=RequestMethod.POST)
	public String join(Member member) throws ClassNotFoundException, SQLException{
		int result = 0;
		String viewpage = "";

		System.out.println("회원가입");
		System.out.println(member.toString());

		member.setPwd(this.bCryptPasswordEncoder.encode(member.getPwd()));

		MemberDao memberdao = sqlsession.getMapper(MemberDao.class);
		RollDAO rolldao = sqlsession.getMapper(RollDAO.class);
		result = memberdao.insert(member);
		int rollResult = rolldao.insertRoll(member.getUserid());
		if (result > 0 && rollResult > 0) {
			System.out.println("insert성공");
			viewpage = "redirect:/index.htm";
		} else {
			System.out.println("insert실패");
			viewpage = "join.htm";
		}
		return viewpage;
	}
	
	@RequestMapping(value="login.htm", method=RequestMethod.GET)
	public String login() {
		return "joinus.login";
	}
	
	@RequestMapping(value="memberConfirm.htm",method=RequestMethod.GET)
	public String memberConfirm(){
		return "joinus.memberConfirm";
	}
	
	/*@RequestMapping(value="memberConfirm.htm",method=RequestMethod.POST)
	public String memberConfirm(
			@RequestParam("pwd") String rawPassword, Principal principal) throws ClassNotFoundException, SQLException{
		String viewpage="";
		MemberDao memberdao = sqlsession.getMapper(MemberDao.class);
		Member member = memberdao.getMember(principal.getName());
		String encodedPassword = member.getPwd();

		System.out.println("rawPassword : " + rawPassword );
		System.out.println("encodedPassword : " + encodedPassword);
				
		boolean result = bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
				System.out.println(result);
				if(result){
					viewpage="redirect:memberUpdate.htm";
				}else{
					viewpage="redirect:memberConfirm.htm";
				}
				return viewpage;
	}*/
	
	
	@RequestMapping(value="memberConfirm.htm",method=RequestMethod.POST)
	public @ResponseBody Member memberConfirm(
			@RequestParam("pwd") String rawPassword, Principal principal) throws ClassNotFoundException, SQLException{
		String viewpage="";
		MemberDao memberdao = sqlsession.getMapper(MemberDao.class);
		Member member = memberdao.getMember(principal.getName());
		String encodedPassword = member.getPwd();

		System.out.println("rawPassword : " + rawPassword );
		System.out.println("encodedPassword : " + encodedPassword);
				
		boolean result = bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
				
		System.out.println(result);
		if(result){
			return member;
		}else{
			return null;
		}
		
	}
	
	
	@RequestMapping(value="memberUpdate.htm", method=RequestMethod.GET)
	public String memberUpdate(Model model, Principal principal) throws ClassNotFoundException, SQLException{
		MemberDao memberdao = sqlsession.getMapper(MemberDao.class);
		Member member = memberdao.getMember(principal.getName());
		model.addAttribute("member", member);
		return "joinus.memberUpdate";
	}
	
	@RequestMapping(value="memberUpdate.htm", method=RequestMethod.POST)
	public String memberUpdate(Model model, Member member) throws ClassNotFoundException, SQLException{
		int result = 0;
		String viewpage = "";
		MemberDao memberdao = sqlsession.getMapper(MemberDao.class);
		
		member.setName(member.getName());
		member.setCPhone(member.getCPhone());
		member.setEmail(member.getEmail());
		member.setPwd(bCryptPasswordEncoder.encode(member.getPwd()));
		
		result = memberdao.update(member);
		if (result > 0) {
			System.out.println("update성공");
			viewpage = "redirect:/index.htm";
		} else {
			System.out.println("update실패");
			viewpage = "memberUpdate.htm";
		}
		return viewpage;
	}
}