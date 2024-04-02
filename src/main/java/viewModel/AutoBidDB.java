package viewModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.BaseEntity;
import model.AutoBid;
import model.User;
import model.Item;

public class AutoBidDB extends BaseDB{

	@Override
	public BaseEntity createModel(ResultSet rs) {
		AutoBid ret = new AutoBid();
		try {
			ret.setMaxBid(rs.getInt("maxBid"));
			
			UserDB userDB = new UserDB();
			ret.setUser((User)userDB.createModel(rs));
			
			ItemDB itemDB = new ItemDB();
			ret.setItem((Item)itemDB.createModel(rs));
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}
	
	public ArrayList<AutoBid> SELECTByItemAndUser(Item item, User user){
		String query = "SELECT * FROM ((\"AutoBids\" INNER JOIN \"Users\" ON \"AutoBids\".\"userCellphone\"=\"Users\".\"userCellphone\") INNER JOIN \"Items\" ON \"Items\".\"itemID\"=\"AutoBids\".\"itemID\")" +
				"INNER JOIN \"Users\" us ON \"Items\".\"userCellphone\"=us.\"userCellphone\" WHERE \"AutoBids\".\"userCellphone\"='" + user.getCellNumber() + "' AND \"Items\".\"itemID\"=" + item.getId();
		ArrayList<BaseEntity> res = SELECT(query);
		if(res == null || res.size() == 0)
			return new ArrayList<AutoBid>();
		ArrayList<AutoBid> ret = new ArrayList<AutoBid>();
		for(BaseEntity entity: 
			res) {
			ret.add((AutoBid)entity);
		}
		return ret;
	}
	
	public ArrayList<AutoBid> SELECTByUser(User user){
		String query = "SELECT * FROM ((\"AutoBids\" INNER JOIN \"Users\" ON \"AutoBids\".\"userCellphone\"=\"Users\".\"userCellphone\") INNER JOIN \"Items\" ON \"Items\".\"itemID\"=\"AutoBids\".\"itemID\")" +
				"INNER JOIN \"Users\" us ON \"Items\".\"userCellphone\"=us.\"userCellphone\" WHERE \"AutoBids\".\"userCellphone\"='" + user.getCellNumber() + "'";
		ArrayList<BaseEntity> res = SELECT(query);
		if(res == null || res.size() == 0)
			return new ArrayList<AutoBid>();
		ArrayList<AutoBid> ret = new ArrayList<AutoBid>();
		for(BaseEntity entity: 
			res) {
			ret.add((AutoBid)entity);
		}
		return ret;
	}
	
	public void DELETEAutoBid(AutoBid autoBid) {
		String command = "DELETE FROM \"AutoBids\" WHERE \"itemID\"=" + autoBid.getItem().getId() + " AND \"userCellphone\"='" + autoBid.getUser().getCellNumber() + "' AND \"maxBid\"=" + autoBid.getMaxBid();
		EXECUTE(command);
	}
	
	public boolean EXISTS(AutoBid autoBid) {
		if(SELECTByItemAndUser(autoBid.getItem(), autoBid.getUser()).size() > 0)
			return true;
		return false;
	}
	
	public void INSERT(AutoBid autoBid) {
		String command;
		if(!EXISTS(autoBid))
			command = "INSERT INTO \"AutoBids\" (\"userCellphone\", \"itemID\", \"maxBid\") VALUES ('" + autoBid.getUser().getCellNumber() + "', " + autoBid.getItem().getId() + ", " + autoBid.getMaxBid() + ")";
		else
			command = "UPDATE \"AutoBids\" SET \"maxBid\" = " + autoBid.getMaxBid() + " WHERE \"userCellphone\" = '" + autoBid.getUser().getCellNumber() + "' AND \"itemID\" = " + autoBid.getItem().getId();
		EXECUTE(command);
	}
	
	public ArrayList<AutoBid> SELECTByItem(Item item){
		String query = "SELECT * FROM ((\"AutoBids\" INNER JOIN \"Users\" ON \"AutoBids\".\"userCellphone\"=\"Users\".\"userCellphone\") INNER JOIN \"Items\" ON \"Items\".\"itemID\"=\"AutoBids\".\"itemID\")" +
				"INNER JOIN \"Users\" us ON \"Items\".\"userCellphone\"=us.\"userCellphone\" WHERE \"Items\".\"itemID\"=" + item.getId();
		ArrayList<BaseEntity> res = SELECT(query);
		if(res == null || res.size() == 0)
			return new ArrayList<AutoBid>();
		ArrayList<AutoBid> ret = new ArrayList<AutoBid>();
		for(BaseEntity entity: 
			res) {
			ret.add((AutoBid)entity);
		}
		return ret;
	}
}
