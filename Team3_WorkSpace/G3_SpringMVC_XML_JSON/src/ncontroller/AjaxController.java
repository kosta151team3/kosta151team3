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

import dao.MemberDao;
import dao.RollDAO;
import vo.Member;
import vo.Roll;

@Controller
public class AjaxController {

	@Autowired
	private SqlSession sqlsession;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	
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
	public Member memberConfirm(
			@RequestParam("pwd") String rawPassword, Principal principal) throws ClassNotFoundException, SQLException{
		String viewpage="";
		MemberDao memberdao = sqlsession.getMapper(MemberDao.class);
		Member member = memberdao.getMember(principal.getName());
		String encodedPassword = member.getPwd();

		System.out.println("~~~~");
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
	
}