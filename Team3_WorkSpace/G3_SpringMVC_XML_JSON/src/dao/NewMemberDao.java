package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import vo.Member;

public class NewMemberDao  implements MemberDao{
	
	private JdbcTemplate template;
	@Autowired
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
	
	@Override
	public Member getMember(String userid) throws ClassNotFoundException,
			SQLException {
    //코드 완성
    //new RowMapper 구현하는 코드로
		List<Member> results = template.query(
				"SELECT * FROM MEMBER WHERE USERID=?",
				new RowMapper<Member>() {
					@Override
					public Member mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Member member = new Member( //생성자
								 rs.getString("USERID"),
								 rs.getString("PWD"),
								 rs.getString("NAME"),
								 rs.getString("GENDER"),
								 rs.getString("BIRTH"),
								 rs.getString("IS_LUNAR"),
								 rs.getString("CPHONE"),
								 rs.getString("EMAIL"),
								 rs.getString("HABIT")
								);
						return member;
					}
				},
				userid);

		return results.isEmpty() ? null : results.get(0);
	}

	public int insert(final Member member) throws ClassNotFoundException, SQLException
	{
	 return	template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String sql = "INSERT INTO MEMBER(USERID, PWD, NAME, GENDER, BIRTH, IS_LUNAR, CPHONE, EMAIL, HABIT, REGDATE) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, member.getUserid());
				pstmt.setString(2, member.getPwd());
				pstmt.setString(3, member.getName());
				pstmt.setString(4, member.getGender());
				pstmt.setString(5, member.getBirth());
				pstmt.setString(6, member.getIsLunar());
				pstmt.setString(7, member.getCPhone());
				pstmt.setString(8, member.getEmail());
				pstmt.setString(9, member.getHabit());
				return pstmt;
			}
		});
		
	}

	@Override
	public int update(final Member member) {
	 return	template.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String sql = "update MEMBER set PWD = ?, NAME = ?, CPHONE = ?, EMAIL = ?, HABIT = ?, REGDATE=SYSDATE";
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, member.getPwd());
				pstmt.setString(2, member.getName());
				pstmt.setString(3, member.getCPhone());
				pstmt.setString(4, member.getEmail());
				pstmt.setString(5, member.getHabit());
				return pstmt;
			}
		});
		
	}
}
