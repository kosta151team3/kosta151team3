package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;



import vo.Notice;

/* 
 
Spring > JdbcTemplate (강제) > CRUD 관련 함수 제공
JDBC API 제공과의 차이 : Select 결과 담을
                         insert, update parameter 값에 설정
 
1. queryForObject : 
   - [레코드 하나(row 1개)]의 값만을 들고 올때 사용 
   - 주의점 : 0~2개 이상이면
   --IncorrectResultSizeDataAccessException을 발생시킴. 
   --select sum(sal) from emp
   --select id, num , title from emp where id=100;
   
2. query : [레코드 여러개]의 값[객체목록]을 가져올때 사용
   --리턴타입  List<T>

3. queryForList :
   --쿼리 실행 결과로 읽어온 컬럼개수가 한개인 경우(데이터 여러건)
   --select name from emp

3. JdbcTemplete 객체 사용시 
   -컬럼명과 VO의 변수명이 같아야한다.

4. ParameterizedBeanPropertyRowMapper와 RowMapper 차이점
   -select 함수 인자 
   - ParameterizedBeanPropertyRowMapper 컬럼명이 같은 경우
   - RowMapper 컬럼명이 다른경우에 set 사용
   - BeanPropertyRowMapper경우는 list와 같은 여러개의 레코드를 받을 때 사용


5. 삽입 / 수정 / 삭제 를 위한 메서드 update()

5. template.update(sql,new PreparedStatementSetter() {
   @Override
   public void setValues(PreparedStatement ps) throws SQLException {
     ps.setString(1, n.getTitle());
    ps.setString(2, n.getContent());
    ps.setString(3, n.getFileSrc());
   }
   });

6. template.update(
    new PreparedStatementCreator() {
    @Override
     public PreparedStatement createPreparedStatement(Connection con)
       throws SQLException {
        String sql = "INSERT INTO NOTICES(SEQ,
         TITLE, CONTENT, WRITER, REGDATE, HIT, FILESRC) VALUES( (SELECT MAX(TO_NUMBER(SEQ))+1 FROM
         NOTICES), ?, ?, 'kglim', SYSDATE, 0, ?)";
       PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, n.getTitle());
        ps.setString(2, n.getContent());
        ps.setString(3, n.getFileSrc());
       return ps;
     }
    } 
    );

  */

public class NewNoticeDao implements NoticeDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int getCount(String field, String query)
			throws ClassNotFoundException, SQLException {

		// 쿼리결과가 단일값 (sum(), max() , min(), count())
		String sql = "SELECT COUNT(*) CNT FROM NOTICES WHERE " + field
				+ " LIKE ?";
		return this.jdbcTemplate.queryForObject(sql,
				                                Integer.class, //return type
				                                "%"+query+ "%");
	}

	@Override
	public List<Notice> getNotices(int page, String field, String query)
			throws ClassNotFoundException, SQLException {

		int srow = 1 + (page - 1) * 5; // 1, 16, 31, 46, 61, ... an = a1 +
										// (n-1)*d
		int erow = 5 + (page - 1) * 5; // 15, 30, 45, 60, 75, ...

		String sql = "SELECT * FROM";
		sql += "(SELECT ROWNUM NUM, N.* FROM (SELECT * FROM NOTICES WHERE "
				+ field + " LIKE ? ORDER BY REGDATE DESC) N)";
		sql += "WHERE NUM BETWEEN ? AND ?";

		// 객체를 (vo) 객체에 담아서 처리하기
		// query 는 배열로 리턴 (목록 출력하기)
		
		//Object[] arr = {};
		
		return this.jdbcTemplate.query(sql, new Object[] { "%" + query + "%",
				srow, erow }, new BeanPropertyRowMapper<Notice>(Notice.class));

	}

	@Override
	public Notice getNotice(String seq) throws ClassNotFoundException,
			SQLException {
			
		//RowMapper<Notice> mapper = new BeanPropertyRowMapper<Notice>();
		//mapper.mapRow(arg0, arg1)
		//select ..컬럼명 == VO(DTO) memberfield 일치


	/*	String sql="select * from usermember where id=?";
		return this.jdbcTemplate.queryForObject(sql, 
				                                new BeanPropertyRowMapper<Notice>(Notice.class),
				                                seq);*/
		
		
		
		
			String sql = "SELECT * FROM NOTICES WHERE SEQ=?";
		
			 return this.jdbcTemplate.queryForObject(sql, new RowMapper<Notice>(){

			@Override
			public Notice mapRow(ResultSet rs, int rownum) throws SQLException {
				Notice n = new Notice();
				//만약 생성자 구현 했다면 Notice n = new Notice(rs.getString(),...)
				n.setSeq(rs.getString("seq")); 
				n.setTitle(rs.getString("title"));
				n.setWriter(rs.getString("writer"));
				n.setRegdate(rs.getDate("regdate")); 
				n.setContent(rs.getString("content"));
				n.setFileSrc(rs.getString("fileSrc")); 
				n.setFileSrc2(rs.getString("fileSrc2")); //추가
				n.setHit(rs.getInt("hit"));
				return n;
			}
			 
		 }, seq);
		
		/*
		//간단하게 자동 매핑 (권장)
		String sql = "SELECT * FROM NOTICES WHERE SEQ=?";
		try{
			return this.jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Notice>(Notice.class),seq);
		}catch(EmptyResultDataAccessException e){
			return null;
		}
		*/
	}
	
	@Override
	public int delete(String seq) throws ClassNotFoundException, SQLException {
		String sql = "DELETE NOTICES WHERE SEQ=?";
		return jdbcTemplate.update(sql, seq);
	}

	@Override
	public int update(Notice notice) throws ClassNotFoundException,
			SQLException {

		String sql = "UPDATE NOTICES SET TITLE=?, CONTENT=?, FILESRC=? , FILESRC2=?  WHERE SEQ=?";
		return this.jdbcTemplate.update(sql,notice.getTitle(), notice.getContent(),
				notice.getFileSrc(), notice.getFileSrc2() ,notice.getSeq());
	}

	@Override
	public int insert(Notice n) throws ClassNotFoundException, SQLException {
		//System.out.println(n.getFileSrc() + "/" + n.getFileSrc2());
		String sql = "INSERT INTO NOTICES" +
				     "(SEQ, TITLE, CONTENT, WRITER, REGDATE, HIT, FILESRC ,FILESRC2 ) " +
				     "VALUES( (SELECT MAX(TO_NUMBER(SEQ))+1 FROM NOTICES), ?, ?, 'kosta', SYSDATE, 0, ?, ?)";
	
		return this.jdbcTemplate.update(sql, n.getTitle(),n.getContent(),n.getFileSrc(),n.getFileSrc2());
		
	}

}
