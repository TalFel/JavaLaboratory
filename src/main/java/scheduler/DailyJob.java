package scheduler;

import java.util.List;
import java.util.TimerTask;

import model.Item;
import viewModel.ItemDB;

public class DailyJob extends TimerTask {
	@Override
	public void run() {
	    System.out.println("Job trigged by scheduler");
	    
	    ItemDB itemDB = new ItemDB();
	    List<Item> auctions = itemDB.SELECTEndedScheduler();
	    
	    if(auctions == null || auctions.size() == 0)
	    	return;
	    
        for(Item item: 
	    	auctions) {
	    	item.endAuction();
	    }
	}
}