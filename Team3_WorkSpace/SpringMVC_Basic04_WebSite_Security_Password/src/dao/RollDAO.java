package dao;

import java.sql.SQLException;

public interface RollDAO {
	public int insertRoll(String userid) throws ClassNotFoundException, SQLException;
}
