package beans;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import model.AutoBid;
import model.Bid;
import model.Item;
import model.User;
import viewModel.AutoBidDB;
import viewModel.BidDB;
import viewModel.ItemDB;

@ManagedBean(name="auctionViewBean")
@SessionScoped
public class AuctionViewBean {
	private Item selected;
	private User sold;
	private String backButton;
	private String bid;
	private boolean isIllegal;
	private boolean isIllegalAutoBid;
	private String autoBid;
	private AutoBid current;
	
	public AuctionViewBean() {
		isIllegal = false;
		isIllegalAutoBid = false;
	}
	
	public void setItemActionListener(String ID, String source) {
		ItemDB itemDB = new ItemDB();
		selected = itemDB.SELECTByID(Integer.parseInt(ID));
		backButton = source;
		if(source == "myAuctionsHistory.xhtml") {
			Bid high = selected.getHighestBid();
			
			if(high == null || high.getUser() == null) {
				sold = new User();
				sold.setCellNumber("-");
				sold.setEmail("-");
				sold.setFullName("-");
			}
			else {
				sold = high.getUser();
			}
		}
	}
	
	public Item getSelected() {
		return selected;
	}
	
	public String getBackButton() {
		return backButton;
	}
	
	public User getSold() {
		return sold;
	}
	
	public String getBid() {
		return bid;
	}
	
	public String getAutoBid() {
		return autoBid;
	}
	
	public AutoBid getExistingAutoBid() {
		if(current != null)
			return current;
		if(!doesHaveAutoBid())
			return new AutoBid();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		User user = (User)session.getAttribute("user");
		current = user.getAutoBidByItem(selected);
		return current;
	}
	
	public boolean getIsIllegal() {
		return isIllegal;
	}
	
	public boolean getIsIllegalAutoBid() {
		return isIllegalAutoBid;
	}
	
	public void setBid(String bid) {
		this.bid = bid;
	}
	
	public void setAutoBid(String autoBid) {
		this.autoBid = autoBid;
	}
	
	public String terminateAuction() {
		selected.endAuction();
		return "myOngoingAuctions.xhtml";
	}
	
	public String deleteAuction() {
		selected.deleteAuction();
		return "myOngoingAuctions.xhtml";
	}
	
	public boolean isViewOngoing() {
		return backButton.equals("myOngoingAuctions.xhtml");
	}
	
	public boolean isViewHistory() {
		return backButton.equals("myAuctionsHistory.xhtml");
	}
	
	public boolean isViewPlaceBid() {
		return (backButton.equals("myOngoingBiddings.xhtml") || backButton.equals("ongoingAuctions.xhtml")) && isNotSameUser();
	}
	
	public boolean doesHaveAutoBid() {
		if(selected == null)
			return false;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if(session.getAttribute("user") == null)
			return false;
		return ((User)session.getAttribute("user")).getAutoBidByItem(selected) != null;
	}
	
	public boolean isNotSameUser() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if(session.getAttribute("user") == null)
			return false;
		return !session.getAttribute("user").equals(selected.getUser());
	}
	
	public boolean isBidIllegal() {
		if(!isViewPlaceBid())
			return false;
		if(bid == null || bid.length() == 0)
			return false;
		try {
			if(selected.isBidLegal(Integer.parseInt(bid)))
				return false;
		}catch(Exception e) {
			return true;
		}
		return true;
	}
	
	public String getNextLegalBid() {
		if(!isViewPlaceBid())
			return "";
		return selected.getNextLegalBid() + "";
	}
	
	public String getNextLegalAutoBid() {
		if(doesHaveAutoBid())
			return getCurrentAutoBidValue();
		return getNextLegalBid();
	}
	
	public String getSelectedItemMaxBidString() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return selected.getHighestBidValue() + (selected.getHighestBid().getUser().equals(session.getAttribute("user")) ? " (yours)" : " (not yours)");
	}
	
	public String placeBidActionListener() {
		if(!isBidIllegal()) {
			//insert into DB
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			User user = (User)session.getAttribute("user");
			Date date = new Date();
			BidDB bidDB = new BidDB();
			bidDB.INSERT(new Bid(user, selected, date, Integer.parseInt(bid)));
			//check if auto bids need to be triggered
			if(!selected.checkAutoBids(user)) {
				//check if bid will trigger the waiver price of the auction
				if(selected.getWaiverPrice() != -1 && selected.getWaiverPrice() <= Integer.parseInt(bid))
					selected.endAuction();				
			}
			selected.resetHighestBid();
			isIllegal = false;
		}
		else {
			isIllegal = true;
		}
		return "auctionView.xhtml";
	}
	
	public void terminateAutoBid() {
		current = null;
		AutoBidDB autoBidDB = new AutoBidDB();
		autoBidDB.DELETEAutoBid(getExistingAutoBid());
	}
	
	public String getCurrentAutoBidValue() {
		AutoBid autoBid = getExistingAutoBid();
		if(autoBid == null)
			return "";
		return autoBid.getMaxBidString();
	}
	
	public String placeAutoBid() {
		try {
			Integer.parseInt(autoBid);
		}catch(Exception e) {
			isIllegalAutoBid = true;
			return "autoBidOptions.xhtml";
		}
		
		AutoBidDB autoBidDB = new AutoBidDB();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		User user = (User)session.getAttribute("user");
		//check if an auto bid exists with a higher price
		if(getExistingAutoBid().getMaxBid() > Integer.parseInt(autoBid)) {
			isIllegalAutoBid = true;
			return "autoBidOptions.xhtml";
		}
		
		AutoBid newAutoBid = new AutoBid(user, selected, Integer.parseInt(autoBid));
		autoBidDB.INSERT(newAutoBid);
		//refresh values
		selected.resetHighestBid();
		current = null;
		isIllegalAutoBid = false;
		return "auctionView.xhtml";
	}
}
