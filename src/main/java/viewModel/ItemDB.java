package viewModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.BaseEntity;
import model.Item;
import model.User;
import java.util.Calendar;

public class ItemDB extends BaseDB{

	@Override
	public BaseEntity createModel(ResultSet rs) {
		Item ret = new Item();
		try{
			ret.setId(rs.getInt("itemID"));
			ret.setCategory(rs.getString("categoryName"));
			
			UserDB userDB = new UserDB();
			ret.setUser((User)userDB.createModel(rs));
			
			ret.setName(rs.getString("itemName"));
			ret.setDescription(rs.getString("itemDescription"));
			ret.setPictureURL(rs.getString("itemImageURL"));
			ret.setStartPrice(rs.getInt("itemStartingPrice"));
			ret.setStartDate(rs.getDate("itemStartingDate"));
			ret.setEndDate(rs.getDate("itemEndingDate"));
			ret.setWaiverPrice(rs.getInt("itemWaiverPrice"));
			ret.setHasEnded(rs.getBoolean("itemEnded"));
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}
	
	public ArrayList<Item> SELECTAll() {
		String nowDateString = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
		String query = "SELECT * FROM \"Items\" INNER JOIN \"Users\" ON \"Items\".\"userCellphone\"=\"Users\".\"userCellphone\" WHERE \"itemEnded\"=false AND \"itemEndingDate\">='"
				+ nowDateString + "' ORDER BY \"itemEndingDate\" DESC";
		ArrayList<BaseEntity> res = SELECT(query);
		if(res.size() == 0)
			return new ArrayList<Item>();
		ArrayList<Item> ret = new ArrayList<Item>();
		for(BaseEntity entity: 
			res) {
			ret.add((Item)entity);
		}
		return ret;
	}
	
	public Item SELECTByID(int ID) {
		String query = "SELECT * FROM \"Items\" INNER JOIN \"Users\" ON \"Items\".\"userCellphone\"=\"Users\".\"userCellphone\" WHERE \"itemID\"=" + ID;
		ArrayList<BaseEntity> res = SELECT(query);
		if(res == null || res.size() == 0)
			return new Item();
		return (Item)res.get(0);
	}
	
	public ArrayList<Item> SELECTByUserOngoing(User user) {
		String query = "SELECT * FROM \"Items\" INNER JOIN \"Users\" ON \"Items\".\"userCellphone\"=\"Users\".\"userCellphone\" WHERE \"Items\".\"userCellphone\"='" + user.getCellNumber() + "'"
				+ " AND \"itemEnded\"=false ORDER BY \"itemEndingDate\" DESC";
		ArrayList<BaseEntity> res = SELECT(query);
		if(res == null || res.size() == 0)
			return new ArrayList<Item>();
		ArrayList<Item> ret = new ArrayList<Item>();
		for(BaseEntity entity: 
			res) {
			ret.add((Item)entity);
		}
		return ret;
	}
	
	public ArrayList<Item> SELECTByUserHistory(User user){
		String query = "SELECT * FROM \"Items\" INNER JOIN \"Users\" ON \"Items\".\"userCellphone\"=\"Users\".\"userCellphone\" WHERE \"Items\".\"userCellphone\"='" + user.getCellNumber() + "'"
				+ " AND \"itemEnded\"=true ORDER BY \"itemEndingDate\" DESC";
		ArrayList<BaseEntity> res = SELECT(query);
		if(res == null || res.size() == 0)
			return new ArrayList<Item>();
		ArrayList<Item> ret = new ArrayList<Item>();
		for(BaseEntity entity: 
			res) {
			ret.add((Item)entity);
		}
		return ret;
	}
	
	public ArrayList<Item> SELECTEndedScheduler(){
		String nowDateString = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
		
		Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        Date twoDaysAgo = cal.getTime();
		
		String query = "SELECT * FROM \"Items\" INNER JOIN \"Users\" ON \"Items\".\"userCellphone\"=\"Users\".\"userCellphone\" WHERE \"itemEndingDate\"<'" + nowDateString + "' AND \"itemEndingDate\">'" +
				twoDaysAgo + "' AND \"itemEnded\"=false";
		ArrayList<BaseEntity> res = SELECT(query);
		if(res == null || res.size() == 0)
			return new ArrayList<Item>();
		ArrayList<Item> ret = new ArrayList<Item>();
		for(BaseEntity entity: 
			res) {
			ret.add((Item)entity);
		}
		return ret;
	}
	
	public void UPDATEEnded(Item item) {
		String command = "UPDATE \"Items\" SET \"itemEnded\"=true WHERE \"itemID\"=" + item.getId();
		EXECUTE(command);
	}
	
	public void INSERT(Item item) {
		String startDateString = (new SimpleDateFormat("yyyy-MM-dd")).format(item.getStartDate());
		String endDateString = (new SimpleDateFormat("yyyy-MM-dd")).format(item.getEndDate());
		String command = "INSERT INTO \"Items\"(\"userCellphone\", \"categoryName\", \"itemName\", \"itemDescription\", \"itemImageURL\", \"itemStartingPrice\", \"itemStartingDate\", \"itemEndingDate\", \"itemWaiverPrice\", \"itemEnded\")"
				+ " VALUES ('" + item.getUser().getCellNumber() + "', '" + item.getCategory() +"', '" + item.getName() + "', '" + item.getDescription() + "', '" + item.getPictureURL()
				+ "', " + item.getStartPrice() + ", '" + startDateString + "', '" + endDateString +"', " + item.getWaiverPrice() + ", false)";
		EXECUTE(command);
	}
}
