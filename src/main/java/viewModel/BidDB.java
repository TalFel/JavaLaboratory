package viewModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.BaseEntity;
import model.Bid;
import model.User;
import model.Item;

public class BidDB extends BaseDB{
	private final int SECOND_USER_START = 19;
	
	@Override
	public BaseEntity createModel(ResultSet rs) {
		Bid ret = new Bid();
		try {
			ret.setDate(rs.getDate("bidDate"));
			ret.setPrice(rs.getInt("bidValue"));
			
			UserDB userDB = new UserDB();
			ret.setUser((User)userDB.createModel(rs));
			
			ItemDB itemDB = new ItemDB();
			ret.setItem((Item)itemDB.createModel(rs));
			
			//the query result needs to get the second user of the result set
			User user = new User();
			user.setCellNumber(rs.getString(SECOND_USER_START));
			user.setEmail(rs.getString(SECOND_USER_START + 1));
			user.setFullName(rs.getString(SECOND_USER_START + 2));
			ret.getItem().setUser(user);
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}
	
	public ArrayList<Bid> SELECTByItem(Item item){
		String query = "SELECT * FROM ((\"Bids\" INNER JOIN \"Users\" ON \"Bids\".\"userCellphone\"=\"Users\".\"userCellphone\") INNER JOIN \"Items\" ON \"Items\".\"itemID\"=\"Bids\".\"itemID\")" +
				"INNER JOIN \"Users\" us ON \"Items\".\"userCellphone\"=us.\"userCellphone\" WHERE \"Items\".\"itemID\"=" + item.getId() + " ORDER BY \"Items\".\"itemEndingDate\" DESC";
		ArrayList<BaseEntity> res = SELECT(query);
		if(res == null || res.size() == 0)
			return new ArrayList<Bid>();
		ArrayList<Bid> ret = new ArrayList<Bid>();
		for(BaseEntity entity: 
			res) {
			ret.add((Bid)entity);
		}
		return ret;
	}
	
	public ArrayList<Bid> SELECTByUserAll(User user){
		String query = "SELECT * FROM ((\"Bids\" INNER JOIN \"Users\" ON \"Bids\".\"userCellphone\"=\"Users\".\"userCellphone\") INNER JOIN \"Items\" ON \"Items\".\"itemID\"=\"Bids\".\"itemID\")" +
				"INNER JOIN \"Users\" us ON \"Items\".\"userCellphone\"=us.\"userCellphone\" WHERE \"Bids\".\"userCellphone\"='" + user.getCellNumber() + "' ORDER BY \"Items\".\"itemEndingDate\" DESC";
		ArrayList<BaseEntity> res = SELECT(query);
		if(res == null || res.size() == 0)
			return new ArrayList<Bid>();
		ArrayList<Bid> ret = new ArrayList<Bid>();
		for(BaseEntity entity: 
			res) {
			ret.add((Bid)entity);
		}
		return ret;
	}
	
	public ArrayList<Bid> SELECTByUserOngoing(User user){
		String dateString = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
		String query = "SELECT * FROM ((\"Bids\" INNER JOIN \"Users\" ON \"Bids\".\"userCellphone\"=\"Users\".\"userCellphone\") INNER JOIN \"Items\" ON \"Items\".\"itemID\"=\"Bids\".\"itemID\")" +
				"INNER JOIN \"Users\" us ON \"Items\".\"userCellphone\"=us.\"userCellphone\" WHERE \"Bids\".\"userCellphone\"='" + user.getCellNumber() + "' AND (\"Items\".\"itemEndingDate\">='" + 
				dateString + "' AND \"Items\".\"itemEnded\"=false)  ORDER BY \"Items\".\"itemEndingDate\" DESC";
		ArrayList<BaseEntity> res = SELECT(query);
		if(res == null || res.size() == 0)
			return new ArrayList<Bid>();
		ArrayList<Bid> ret = new ArrayList<Bid>();
		for(BaseEntity entity: 
			res) {
			ret.add((Bid)entity);
		}
		return ret;
	}
	
	public ArrayList<Bid> SELECTByUserWins(User user){
		String query = "SELECT * FROM ((\"Bids\" INNER JOIN \"Users\" ON \"Bids\".\"userCellphone\"=\"Users\".\"userCellphone\") INNER JOIN \"Items\" ON \"Items\".\"itemID\"=\"Bids\".\"itemID\")" +
				"INNER JOIN \"Users\" us ON \"Items\".\"userCellphone\"=us.\"userCellphone\" WHERE \"Bids\".\"userCellphone\"='" + user.getCellNumber() + "' AND \"Items\".\"itemEnded\"=true"
						+ " ORDER BY \"Items\".\"itemEndingDate\" DESC";
		ArrayList<BaseEntity> res = SELECT(query);
		if(res == null || res.size() == 0)
			return new ArrayList<Bid>();
		ArrayList<Bid> ret = new ArrayList<Bid>();
		for(BaseEntity entity: 
			res) {
			if(((Bid)entity).equals(((Bid)entity).getItem().getHighestBid()))
				ret.add((Bid)entity);
		}
		return ret;
	}
	
	public void INSERT(Bid bid) {
		String dateString = (new SimpleDateFormat("yyyy-MM-dd")).format(bid.getDate());
		String command = "INSERT INTO \"Bids\"(\"itemID\", \"userCellphone\", \"bidDate\", \"bidValue\") VALUES("
				+ bid.getItem().getId() + ", '" + bid.getUser().getCellNumber() + "', '" + dateString + "', " + bid.getPrice() + ")";
		EXECUTE(command);
	}
}
