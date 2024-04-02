package viewModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.BaseEntity;

public abstract class BaseDB {
	
	private final String url = "jdbc:postgresql://localhost:5432/AuctionHouseDB";
    private final String user = "postgres";
    private final String password = "password123";
	
	public BaseDB() {}
	public abstract BaseEntity createModel(ResultSet rs);
	
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the PostgreSQL server successfully.");
                return conn;
            } else {
                System.out.println("Failed to make connection!");
            }
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}catch(ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public ArrayList<BaseEntity> SELECT(String query) {
		ArrayList<BaseEntity> ret = new ArrayList<BaseEntity>();
		Connection conn = null;
		Statement st = null;
        ResultSet rs = null;
		try {
			conn = getConnection();
	        st = conn.createStatement();
	        rs = st.executeQuery(query);
	        while(rs.next()) {
	        	ret.add(createModel(rs));
	        }
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
				if(conn != null)
					conn.close();
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return ret;
	}
	
	public void EXECUTE(String command) {
		try {
			PreparedStatement statement = getConnection().prepareStatement(command);
			statement.executeUpdate();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
}