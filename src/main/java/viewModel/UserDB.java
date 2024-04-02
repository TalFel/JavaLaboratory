package viewModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.BaseEntity;
import model.User;

public class UserDB extends BaseDB{

	@Override
	public BaseEntity createModel(ResultSet rs) {
		User ret = new User();
		try {
			ret.setCellNumber(rs.getString("userCellphone"));
			ret.setFullName(rs.getString("userFullName"));
			ret.setEmail(rs.getString("userEmail"));
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}
	
	public User login(String cellphone, String password) {
		String query = "SELECT * FROM \"Users\" WHERE \"userCellphone\"='" + cellphone + "' AND \"userPassword\"='" + password + "'";
		ArrayList<BaseEntity> res = SELECT(query);
		if(res.size() == 0)
			return null;
		return (User)res.get(0);
	}
	public boolean exists(User user) {
		String query = "SELECT * FROM \"Users\" WHERE \"userCellphone\"='" + user.getCellNumber() + "' OR \"userEmail\"='" + user.getEmail() + "'";
		ArrayList<BaseEntity> res = SELECT(query);
		if(res.size() == 0)
			return false;
		return true;
	}
	public void INSERT(User user, String password) {
		String command = "INSERT INTO \"Users\"(\"userCellphone\", \"userEmail\", \"userFullName\", \"userPassword\") VALUES ('"
				+ user.getCellNumber() + "', '" + user.getEmail() + "', '" + user.getFullName() + "', '" + password + "')";
		EXECUTE(command);
	}
}
