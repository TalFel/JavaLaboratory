package beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import model.Item;
import model.TwoItems;
import model.User;
import viewModel.ItemDB;


@ManagedBean(name="ongoinAuctionsBean")
@SessionScoped
public class OngoingAuctionsBean {
	private List<TwoItems> dataAuctions;
	private String search;
	private int page;
	private final int pageSize = 4;
	
	public OngoingAuctionsBean() {
		dataAuctions = getDataForTable();
		search = "";
		page = 0;
	}
	
	public List<TwoItems> getDataAuctions() {
		return dataAuctions;
	}
	
	public String getSearch() {
		return search;
	}

	public void setDataAuctions(List<TwoItems> dataAuctions) {
		this.dataAuctions = dataAuctions;
	}
	
	public void setSearch(String search) {
		this.search = search;
	}
	
	
	public boolean isEmpty() {
		return dataAuctions.size() == 0;
	}
	
	public boolean isNotEmpty() {
		return dataAuctions.size() != 0;
	}
	
	public List<Item> loadAllAuctions(){
		ItemDB itemDB = new ItemDB();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if(session == null)
			return new ArrayList<Item>();
		List<Item> lst = itemDB.SELECTAll();
		if(lst == null || lst.size() == 0)
			return new ArrayList<Item>();
		List<Item> ret = new ArrayList<Item>();
		for(int i = 0; i < pageSize && i + page * pageSize < lst.size(); i++){
			Item item = lst.get(i + page*pageSize);
			if(search != null && search != "") {
				if(item.getCategory().contains(search) || item.getName().contains(search))
					ret.add(item);
			}
			else
				ret.add(item);
		}
		return ret;
	}
	
	public List<TwoItems> getDataForTable(){
		List<Item> auctions = loadAllAuctions();
		double length = auctions.size();
		List<TwoItems> ret = new ArrayList<TwoItems>();
		if(auctions.size() == 0)
			return new ArrayList<TwoItems>();
		if(auctions.size() == 1) {
			ret.add(new TwoItems(auctions.get(0), new Item()));
			return ret;
		}
    	for(int i = 0; i <= Math.ceil(length/2); i+=2) {
    		Item item1 = new Item();
    		if(i < auctions.size())
    			item1 = auctions.get(i);
    		Item item2 = new Item();
    		if(i + 1 < auctions.size())
    			item2 = auctions.get(i + 1);
    		ret.add(new TwoItems(item1, item2));
    	}
    	return ret;
    }
	
	public void searchActionListener() {
		dataAuctions = getDataForTable();
	}
	
	public void clearActionListener() {
		setSearch("");
		dataAuctions = getDataForTable();
	}
	
	public void nextPage() {
		ItemDB itemDB = new ItemDB();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if(session == null)
			return;
		User user = (User)session.getAttribute("user");
		List<Item> lst = itemDB.SELECTAll();
		int DBSize = 0;
		for(Item item: lst) {
			if(!item.getUser().equals(user))
				DBSize++;
		}
		if((page + 1) * pageSize < DBSize)
			page++;
		dataAuctions = getDataForTable();
	}
	
	public void prevPage() {
		if(page > 0)
			page--;
		dataAuctions = getDataForTable();
	}
	
	public void reloadOngoing() {
		dataAuctions = getDataForTable();
	}
}
