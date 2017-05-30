package ncontroller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import vo.Notice;
import dao.NoticeDao;

@Controller
@RequestMapping("/customer/")
public class CustomerController {

	@Autowired
	private SqlSession sqlsession;

	// 글목록보기
	@RequestMapping("notice.htm")
	public String notices(String pg, String f, String q, Model model) throws ClassNotFoundException, SQLException {

		System.out.println("notice.htm");
		int page = 1;
		String field = "TITLE";
		String query = "%%";

		// 위 두 조건 조합
		if (pg != null && !pg.equals("")) {
			page = Integer.parseInt(pg);
		}
		if (f != null && !f.equals("")) {
			field = f;
		}
		if (q != null && !q.equals("")) {
			query = q;
		}
		
		NoticeDao noticeDao = sqlsession.getMapper(NoticeDao.class);
		List<Notice> list = noticeDao.getNotices(page, field, query);

		// Model 객체 사용해서
		model.addAttribute("list", list); // 자동 forward

		// return "notice.jsp";
		return "customer.notice";
	}

	/* 글 상세 보기 */
	@RequestMapping("noticeDetail.htm")
	public String noticeDetail(String seq, Model model) throws ClassNotFoundException, SQLException {
		NoticeDao noticeDao = sqlsession.getMapper(NoticeDao.class);
		Notice notice = noticeDao.getNotice(seq);
		model.addAttribute("notice", notice);
		return "customer.noticeDetail";
	}

	/* 글쓰기(화면) */
	// @RequestMapping("/customer/")
	@RequestMapping(value = "noticeReg.htm", method = RequestMethod.GET)
	public String noticeReg() {
		return "customer.noticeReg";
	}

	/* 글쓰기(처리) */
	@RequestMapping(value = "noticeReg.htm", method = RequestMethod.POST)
	public String noticeReg(Notice n, HttpServletRequest request, Principal principal)
			throws ClassNotFoundException, SQLException, IOException {

		List<CommonsMultipartFile> files = n.getFiles();
		List<String> filenames = new ArrayList<String>();// 파일명만 추출

		if (files != null && files.size() > 0) {
			// 업로드한 파일이 하나라도 있다면
			for (CommonsMultipartFile multifile : files) {
				String filename = multifile.getOriginalFilename();
				String path = request.getServletContext().getRealPath("/customer/upload");
				String fpath = path + "\\" + filename;
				System.out.println(filename + "/" + fpath);
				if (!filename.equals("")) {
					// 서버에 파일 쓰기 작업
					FileOutputStream fs = new FileOutputStream(fpath);
					fs.write(multifile.getBytes());
					fs.close();
				}
				filenames.add(filename);// 실제 DB insert 할 파일명
			}
		}

		// DB작업
		n.setFileSrc(filenames.get(0));
		n.setFileSrc2(filenames.get(1));
		
		/*******************************************/
/*		
			문제점 : 글쓰기 때 접속한 회원의 ID값을 사용
			> 회원가입되어있고 인증받는 사용자의 ID값
			> 로그인 폼 : (name="username", name="password")
			> 로그인 성공 : (username, password) Spring 관리 (기존 : session)
*/		
/*
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		UserDetails userinfo = (UserDetails)auth.getPrincipal();
		System.out.println(userinfo.getPassword());
		System.out.println(userinfo.getUsername());
		
		n.setWriter(userinfo.getUsername());
*/
		// 위보다 더 간단한 방법!
		// String noticeReg(Notice n, HttpServletRequest request, Principal principal)
		// 생성된 Principal 구현하는 객체의 주소
		n.setWriter(principal.getName());
		/*******************************************/		
		NoticeDao noticeDao = sqlsession.getMapper(NoticeDao.class);
		noticeDao.insert(n);
		return "redirect:notice.htm";

	}

	/* 글삭제하기 */
	@RequestMapping("noticeDel.htm")
	public String noticeDel(String seq) throws ClassNotFoundException, SQLException {
		NoticeDao noticeDao = sqlsession.getMapper(NoticeDao.class);
		noticeDao.delete(seq);
		return "redirect:notice.htm"; // location.href 동일
	}

	@RequestMapping(value = "noticeEdit.htm", method = RequestMethod.GET)
	public String noticeEdit(String seq, Model model) throws ClassNotFoundException, SQLException {
		NoticeDao noticeDao = sqlsession.getMapper(NoticeDao.class);
		Notice notice = noticeDao.getNotice(seq);
		model.addAttribute("notice", notice);
		return "customer.noticeEdit";
	}

	@RequestMapping(value = "noticeEdit.htm", method = RequestMethod.POST)
	public String noticeEdit(Notice n, HttpServletRequest request)
			throws IOException, ClassNotFoundException, SQLException {
		NoticeDao noticeDao = sqlsession.getMapper(NoticeDao.class);
		List<CommonsMultipartFile> files = n.getFiles();
		List<String> filenames = new ArrayList<String>();// 파일명만 추출

		if (files != null && files.size() > 0) {
			// 업로드한 파일이 하나라도 있다면
			for (CommonsMultipartFile multifile : files) {
				String filename = multifile.getOriginalFilename();
				String path = request.getServletContext().getRealPath("/customer/upload");
				String fpath = path + "\\" + filename;
				System.out.println(filename + "/" + fpath);
				if (!filename.equals("")) {
					// 서버에 파일 쓰기 작업
					FileOutputStream fs = new FileOutputStream(fpath);
					fs.write(multifile.getBytes());
					fs.close();
				}
				filenames.add(filename);// 실제 DB insert 할 파일명
			}
		}
		// DB작업
		n.setFileSrc(filenames.get(0));
		n.setFileSrc2(filenames.get(1));

		noticeDao.update(n);
		return "redirect:noticeDetail.htm?seq=" + n.getSeq();
	}

	// DAO 단에서 JDBCTemplate 변환 파일 이미지 가지고 온 다음 확인 합시다
	@RequestMapping("download.htm")
	public void download(String p, String f, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String fname = new String(f.getBytes("euc-kr"), "8859_1");
		System.out.println(fname);
		response.setHeader("Content-Disposition", "attachment;filename=" + fname + ";");
		String fullpath = request.getServletContext().getRealPath("/customer/" + p + "/" + f);
		System.out.println(fullpath);
		FileInputStream fin = new FileInputStream(fullpath);
		// 출력 도구 얻기 :response.getOutputStream()
		ServletOutputStream sout = response.getOutputStream();
		byte[] buf = new byte[1024]; // 전체를 다읽지 않고 1204byte씩 읽어서
		int size = 0;
		while ((size = fin.read(buf, 0, buf.length)) != -1) // buffer 에 1024byte
		// 담고
		{ // 마지막 남아있는 byte 담고 그다음 없으면 탈출
			sout.write(buf, 0, size); // 1kbyte씩 출력
		}
		fin.close();
		sout.close();
	}

}
