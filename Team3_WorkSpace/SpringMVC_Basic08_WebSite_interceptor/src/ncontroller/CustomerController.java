package ncontroller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import vo.Notice;
import dao.NoticeDao;

//폴더 경로가 긴 경우
///customer/notice.htm => notice.htm (함수 mapping)
///customer/noticeDetail.html  =>noticeDetail.html
@Controller
@RequestMapping("/customer/")
public class CustomerController {

	@Autowired
	private NoticeDao noticeDao;

	//글목록보기
	@RequestMapping("notice.htm")
	public String notices(String pg, String f , String q ,Model model ) throws ClassNotFoundException, SQLException{
		System.out.println("notice.htm");
		//1단계 (전통적인 방법)
		//notices(HttpServletRequest request){ request.getParamter("pg")}
		
		//2단계 (옵션 제약사항)
		//notices(@RequestParam(value="pg" required="false" defaultValue="10") String pg){}
		
		//3단계 (심플하게)
		//jsp?pg=100&f=10&q=1
		//notices(String pg , String f , String q)
		//파라메터 이름과 함수의 파라메터 이름 동일
		//자동으로 값 매핑
		
		//4단계 (객체 (DTO))
		//notices(boardDto board) 일치 
		
		
		int page = 1;
		String field = "TITLE";
		String query ="%%";
		
		//위 두 조건 조합
		if(pg != null && !pg.equals("")){
			page = Integer.parseInt(pg);
		}
		if(f != null && !f.equals("")){
			field = f;
		}
		if(q != null && !q.equals("")){
			query = q;
		}
		
		List<Notice> list = noticeDao.getNotices(page, field, query);
		
		//Model 객체 사용해서
		model.addAttribute("list", list); //자동 forward 
		
		//return "notice.jsp";
		  return "customer.notice";
	}

	//글 상세 보기
	@RequestMapping("noticeDetail.htm")
	public String noticeDetail(String seq,Model model) throws ClassNotFoundException, SQLException{
		
		Notice notice = noticeDao.getNotice(seq);
		model.addAttribute("notice",notice );
		
		//return "noticeDetail.jsp";
		  return "customer.noticeDetail";
	}

	//글쓰기(화면)
	//@RequestMapping("/customer/")
	@RequestMapping(value="noticeReg.htm" , method=RequestMethod.GET)
	public String noticeReg(){
			//return "noticeReg.jsp";
			return "customer.noticeReg";
	}
		
		
	//글쓰기(처리)
	@RequestMapping(value="noticeReg.htm" , method=RequestMethod.POST)
	public String noticeReg(Notice n,HttpServletRequest request) throws ClassNotFoundException, SQLException, IOException{
		 //다중 파일 업로드 처리
		 //view 단
		 //<input type="file" name="files[0]"
		 //<input type="file" name="files[1]"
		 //files[0] , files[1] 처리
		 //DTO > private List<CommonsMultipartFile> files;
		 //DTO 의 변수명과 input 태그 name 동일 자동화
		System.out.println("다중 파일 글 등록 처리");
		System.out.println("n :" + n.getTitle());
		System.out.println("n :" + n.getContent());
		System.out.println("n :" + n.getFiles().get(0).getName());
		System.out.println("n :" + n.getFiles().get(1).getName());
			
		List<CommonsMultipartFile> files = n.getFiles();	
		List<String> filenames = new ArrayList<String>();//파일명만 추출	
		
		if(files != null && files.size() > 0){
			//업로드한 파일이 하나라도 있다면
			for(CommonsMultipartFile multifile : files){
				String filename = multifile.getOriginalFilename();
				String path = request.getServletContext().getRealPath("/customer/upload");
				String fpath = path + "\\" + filename;
				System.out.println(filename + "/" + fpath);
				if(!filename.equals("")){
					//서버에 파일 쓰기 작업
					FileOutputStream fs = new FileOutputStream(fpath);
					fs.write(multifile.getBytes());
					fs.close();
				}
				filenames.add(filename);// 실제 DB insert 할 파일명
			}
		}
		
		//DB작업
		n.setFileSrc(filenames.get(0));
		n.setFileSrc2(filenames.get(1));
		
		noticeDao.insert(n);
		return "redirect:notice.htm";	
			
			
			/*  단일 파일 유효성 검증 없이 처리
			String filename = n.getFile().getOriginalFilename();
			String path = request.getServletContext().getRealPath("/customer/upload");
			
			String fpath = path + "\\" + filename;
			FileOutputStream fs = new FileOutputStream(fpath);
			fs.write(n.getFile().getBytes());
			fs.close();
			
			//파일명 추가
			n.setFileSrc(filename);
			
			noticeDao.insert(n);
			return "redirect:notice.htm";
			*/
		    
			//주소창 주소 확인하기
			//return "notice.htm" > 요청 주소인경우 
			//결국 : @RequestMapping("notice.htm")
					//       public String notices{ }
					//       위 코드를 한번 실행해서 forward
			//http://localhost:8090/SpringMVC_Basic05_WebSite_Annotation/customer/noticeReg.htm
		
			//return "notice.jsp" > 인경우 
			//페이지만 만들어서 전송
			
			 
			//return "redirect:notice.htm";
			//http://localhost:8090/SpringMVC_Basic05_WebSite_Annotation/customer/notice.htm
			//F5 , 새로고침 처리 목록페이지 보여주도록 설정
			
			//location.href
			//response.sendredirect
			
			// POINT-1***********************************************
			// public String noticeReg(Notice n , HttpServletRequest request)
			// Parameter > Notice n 객체 타입
			// 글쓰기 화면 입력 > 함수(입력한 값)
			// 전제 : <input 태그 name="" 값하고 Notice 객체가 가지는 memberField명 같은 경우
			 
			// POINT-2***********************************************
			// Spring 에서 파일 업로드
			// 웹 서버 upload 폴더 : 파일올리기 (IO)
			// DB : 파일명만 가지면 된다
			
			//<Form 태그에 속성으로 : enctype="multipart/form-data">
			// 1.의존 lib 추가 (fileupload , io)
		    // 2. VO , DTO 쪽에Spring 제공하는
			// CommonsMultipartFile 타입을 갖는 멤버 변수 추가하기 (setter, getter)
			 
			// 3. 전송페이지에서 <form ... enctype="multipart/form-data" 설정
			// -<input type="file" name="file" 파일명 VO 객체 이름 동일 강제사항
			 
			// 4. xml container 에
			// ***CommonsMultipartFile 반드시> id="multipartResolver"***
			//<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver" >
			//   <property name="maxUploadSize" value="10485760"/>
			//</bean>

		}

	//글삭제하기
	@RequestMapping("noticeDel.htm")
	public String noticeDel(String seq) throws ClassNotFoundException, SQLException{
		
			noticeDao.delete(seq);
			return "redirect:notice.htm"; //location.href 동일
		}
		
	//글수정하기(두가지 처리 : 화면 (select) , 처리(update)
	//글수정하기(수정하기 화면 , 수정처리)
	//주소같고 (GET , POST)
	@RequestMapping(value="noticeEdit.htm" , method=RequestMethod.GET)
	public String noticeEdit(String seq, Model model) throws ClassNotFoundException, SQLException{
			//코드 구현
			//수정페이지는 기존 데이터 출력
			Notice notice = noticeDao.getNotice(seq);
			model.addAttribute("notice", notice);
			//request.setAttribute("notice",notice)
			//forward
			//return "noticeEdit.jsp";
			return "customer.noticeEdit";
	}
	@RequestMapping(value="noticeEdit.htm" , method=RequestMethod.POST)
	public String noticeEdit(Notice n,HttpServletRequest request) throws IOException, ClassNotFoundException, SQLException{
			
			/*
			단일 파일 수정
			String filename = n.getFile().getOriginalFilename();
			String path = request.getServletContext().getRealPath("/customer/upload");
			
			String fpath = path + "\\" + filename;
			FileOutputStream fs = new FileOutputStream(fpath);
			fs.write(n.getFile().getBytes());
			fs.close();
			
			//파일명 추가
			n.setFileSrc(filename);
			*/
		List<CommonsMultipartFile> files = n.getFiles();	
		List<String> filenames = new ArrayList<String>();//파일명만 추출	
		
		if(files != null && files.size() > 0){
			//업로드한 파일이 하나라도 있다면
			for(CommonsMultipartFile multifile : files){
				String filename = multifile.getOriginalFilename();
				String path = request.getServletContext().getRealPath("/customer/upload");
				String fpath = path + "\\" + filename;
				System.out.println(filename + "/" + fpath);
				if(!filename.equals("")){
					//서버에 파일 쓰기 작업
					FileOutputStream fs = new FileOutputStream(fpath);
					fs.write(multifile.getBytes());
					fs.close();
				}
				filenames.add(filename);// 실제 DB insert 할 파일명
			}
		}
		//DB작업
		n.setFileSrc(filenames.get(0));
		n.setFileSrc2(filenames.get(1));
				
			noticeDao.update(n);
			return "redirect:noticeDetail.htm?seq=" + n.getSeq();
		}
	
	
	
	 //DAO 단에서 JDBCTemplate 변환 파일 이미지 가지고 온 다음 확인 합시다 	 
	 @RequestMapping("download.htm")
	 public void download(String p, String f, HttpServletRequest request,
	   HttpServletResponse response) throws IOException {

	  /*
	   * //한글 처리 형식 지정 String sEncoding = new
	   * String(filename.getBytes("euc-kr"),"8859_1");
	   * response.setHeader("Content-Disposition","attachment;filename= " +
	   * sEncoding);
	   * //response.setHeader("Content-Disposition","attachment;filename= " +
	   * filename +";");
	   */
	  // 한글 파일명 처리 (Filtter 처리 확인) -> 경우 ...
	  // 한글 파일 깨짐 현상 해결하기
	  // String fname = new String(f.getBytes("ISO8859_1"),"UTF-8");
	  String fname = new String(f.getBytes("euc-kr"), "8859_1");
	  System.out.println(fname);
	  // 다운로드 기본 설정 (브라우져가 read 하지 않고 ... 다운 )
	  // 요청 - 응답 간에 헤더정보에 설정을 강제 다운로드
	  // response.setHeader("Content-Disposition", "attachment;filename=" +
	  // new String(fname.getBytes(),"ISO8859_1"));
	  response.setHeader("Content-Disposition", "attachment;filename="
	    + fname + ";");
	  // 파일명 전송
	  // 파일 내용전송
	  String fullpath = request.getServletContext().getRealPath(
	    "/customer/" + p + "/" + f);
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
