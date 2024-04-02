package beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import model.Bid;
import model.Item;
import model.User;
import viewModel.BidDB;
import viewModel.ItemDB;


@ManagedBean(name="generalBean")
@SessionScoped
public class GeneralBean {
	private User user;
	private List<Item> userOngoingAuctions;
	private List<Item> userAuctionsHistory;
	private List<Bid> userOngoingBiddings;
	private List<Bid> userBiddingsHistory;
	private List<Bid> userWinsHistory;
	
	public GeneralBean() {
		user = getUserFromSession();
	}
	
	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "signIn.xhtml";
	}
	
	public boolean isConnected() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return session != null && session.getAttribute("user") != null;
	}
	
	public User getUserFromSession() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if(session == null)
			return null;
		if(session.getAttribute("user") == null)
			return null;
		return (User)session.getAttribute("user");
	}
	
	public User getUser() {
		if(user == null || user.getCellNumber() == null) {
			user = getUserFromSession();
		}
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getOngoingAuctionsSource() {
		return "myOngoingAuctions.xhtml";
	}
	
	public String getAuctionsHistorySource() {
		return "myAuctionsHistory.xhtml";
	}
	
	public String getBiddingsHistorySource() {
		return "myBiddingsHistory.xhtml";
	}
	
	public String getOngoingBiddingsSource() {
		return "myOngoingBiddings.xhtml";
	}
	
	public String getWinningBidsSource() {
		return "myWinningBids.xhtml";
	}
	
	public String getOnoingAuctionsSource() {
		return "ongoingAuctions.xhtml";
	}
	
	public List<Item> getUserOngoingAuctions() {
		if(userOngoingAuctions == null) {
			ItemDB itemDB = new ItemDB();
			userOngoingAuctions = itemDB.SELECTByUserOngoing(getUser());
		}
		return userOngoingAuctions;
	}
	
	public List<Item> getUserAuctionsHistory() {
		if(userAuctionsHistory == null) {
			ItemDB itemDB = new ItemDB();
			userAuctionsHistory = itemDB.SELECTByUserHistory(getUser());
		}
		return userAuctionsHistory;
	}
	
	public List<Bid> getUserBiddingsHistory() {
		if(userBiddingsHistory == null) {
			BidDB bidDB = new BidDB();
			userBiddingsHistory = bidDB.SELECTByUserAll(getUser());
		}
		return userBiddingsHistory;
	}
	
	public List<Bid> getUserOngoingBiddings(){
		if(userOngoingBiddings == null) {
			BidDB bidDB = new BidDB();
			userOngoingBiddings = bidDB.SELECTByUserOngoing(getUser());
		}
		return userOngoingBiddings;
	}
	
	public List<Bid> getUserWinsHistory(){
		if(userWinsHistory == null) {
			BidDB bidDB = new BidDB();
			userWinsHistory = bidDB.SELECTByUserWins(getUser());
		}
		return userWinsHistory;
	}
}
