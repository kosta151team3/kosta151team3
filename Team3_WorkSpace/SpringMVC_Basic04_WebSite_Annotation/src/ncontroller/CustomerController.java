 package ncontroller;


import java.io.FileOutputStream;
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
@RequestMapping("/customer")
public class CustomerController {
	
	private NoticeDao noticedao;
	@Autowired
    public void setNoticedao(NoticeDao noticedao) {
      this.noticedao = noticedao;
   }

   @RequestMapping("/notice.htm")
   public ModelAndView handleRequest(String pg, String f, String q) throws Exception {
    
      //게시판 검색 설정을 위한 기본값 설정
      int page=1;
      String field="TITLE";
      String query="%%";
      
      //조건에 대한 조합
      if(pg != null && !pg.equals("")){
         page = Integer.parseInt(pg);
      }
      if(f != null && !f.equals("")){
         field = f;
      }
      if(q != null && !q.equals("")){
         query = q;
      }
 
      List<Notice> list = noticedao.getNotices(page, field, query);

      ModelAndView mv = new ModelAndView();
      mv.addObject("list",list);
      mv.setViewName("notice.jsp");
            
      return mv;
   }

    @RequestMapping("/noticeDetail.htm")
	public ModelAndView handleRequest(String seq) throws Exception {
		
    	Notice notice = noticedao.getNotice(seq);
		
		 ModelAndView mv = new ModelAndView();
	     mv.addObject("notice",notice);
	     mv.setViewName("noticeDetail.jsp");
	     
		return mv;
	}
    
 // 글수정하기(화면)
 	@RequestMapping(value="noticeEdit.htm", method=RequestMethod.GET)
 	public String noticeEdit(String seq, org.springframework.ui.Model model) throws Exception {
 		Notice notice = noticedao.getNotice(seq);
 		model.addAttribute("notice", notice);
 		
 		return "noticeEdit.jsp";
 	}
 	
 	// 글수정하기(처리)
 	@RequestMapping(value="noticeEdit.htm", method=RequestMethod.POST)
 	public String noticeEdit(Notice notice, HttpServletRequest request) throws Exception {
 		
 		CommonsMultipartFile imagefile = notice.getFile();
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
 		noticedao.update(notice);
 		return "noticeDetail.htm";
 	}
    
    @RequestMapping("/noticeDel.htm")
    public ModelAndView noticeDel(String seq) throws ClassNotFoundException, SQLException {
       
       int notice = noticedao.delete(seq);
       ModelAndView mv = new ModelAndView();
       mv.addObject("notice", notice);
       
       mv.setViewName("notice.htm");
       return mv;
    }
	
}
