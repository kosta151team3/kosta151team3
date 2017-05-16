package ncontroller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;


import vo.Notice;
import dao.NoticeDao;

@Controller
@RequestMapping("/customer/noticeReg.htm")
public class CustomerWriteController {

	private NoticeDao noticedao;

	@Autowired
	public void setNoticedao(NoticeDao noticedao) {
		this.noticedao = noticedao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String writeBoard() {// 화면

		return "noticeReg.jsp";

	}

	@RequestMapping(method = RequestMethod.POST)
	public String submit(Notice notice, HttpServletRequest request) throws IOException, ClassNotFoundException, SQLException {

		CommonsMultipartFile imagefile = notice.getFile();

		// 파일 업로드 작업
		String filename = imagefile.getOriginalFilename();
		String path = request.getServletContext().getRealPath("/upload");
		String fpath = path + "\\" + filename;

		if(filename==null || filename==""){
		
		}else{
			FileOutputStream fs = new FileOutputStream(fpath);
			fs.write(imagefile.getBytes());
			fs.close();

			notice.setFileSrc(fpath);
		}
		
		int rs = noticedao.insert(notice);
		
		if(rs>0){
			return "notice.htm";
		}else{
			return "noticeReg.htm";
		}
		
		
	}
}
