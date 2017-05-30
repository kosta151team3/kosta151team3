package vo;

public class Roll {
	private String userid;
	private String role_name;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	@Override
	public String toString() {
		return "Roll [userid=" + userid + ", role_name=" + role_name + "]";
	}

}
