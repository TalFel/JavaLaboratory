package model;

import java.util.ArrayList;
import java.util.Date;

import viewModel.AutoBidDB;
import viewModel.BidDB;
import viewModel.ItemDB;

public class Item extends BaseEntity{
    private int id;
    private User user;
    private String category;
    private String name;
    private String description;
    private String pictureURL;
    private int startPrice;
    private Date startDate;
    private Date endDate;
    private int waiverPrice;
    private boolean hasEnded;
    
    private Bid highestBid;
    
    private final int priceMargin = 5;
    
    public Item(){}

    public Item(int id, User user, String category, String name, String description,
                String pictureURL, int startPrice, Date startDate, Date endDate, int waiverPrice, boolean hasEnded) {
        this.id = id;
        this.user = user;
        this.category = category;
        this.name = name;
        this.description = description;
        this.pictureURL = pictureURL;
        this.startPrice = startPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.waiverPrice = waiverPrice;
        this.hasEnded = hasEnded;
    }
    
    public boolean isNotNull() {
    	if(user != null)
    		return true;
    	return false;
    }

    // Getters
    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getWaiverPrice() {
        return waiverPrice;
    }
    
    public String getWaiverPriceString() {
    	if(waiverPrice == -1)
    		return "none";
    	else
    		return waiverPrice + "";
    }
    
    public boolean getHasEnded() {
    	return hasEnded;
    }
    
    public String getStringId() {
    	return id + "";
    }
    
    public int getPriceMargin() {
		return priceMargin;
	}
    
    public String getImagePath() {
    	return "/" + General.getResourcesImagesPath() + "/" + pictureURL;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }

	public void setUser(User user) {
        this.user = user;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public void setStartPrice(int startPrice) {
        this.startPrice = startPrice;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setWaiverPrice(int waiverPrice) {
        this.waiverPrice = waiverPrice;
    }
    
    public void setHasEnded(boolean hasEnded) {
    	this.hasEnded = hasEnded;
    }
    
    public void resetHighestBid() {
    	highestBid = null;
    }

    @Override
    public boolean equals(Object obj) {
    	if(!(obj instanceof Item))
    		return false;
        if(this.getId() == ((Item)obj).getId())
        	return true;
    	return false;
    }

    public Bid getHighestBid() {
    	if(highestBid == null) {
	    	BidDB bidDB = new BidDB();
	        ArrayList<Bid> bids = bidDB.SELECTByItem(this);
	        if(bids.size() == 0)
	        	return new Bid(this.user, this, this.startDate, this.startPrice);
	        Bid max = bids.get(0);
	        for (Bid bid:
	                bids) {
	            if(bid.getPrice() > max.getPrice())
	                max = bid;
	        }
	        highestBid = max;
    	}
        return highestBid;
    }
    
    public String getHighestBidValue(){
        return getHighestBid().getPrice() + "";
    }
    
    public int getNextLegalBid() {
    	int offer = Integer.parseInt(getHighestBidValue());
    	int size = (int)Math.log10(offer);
    	int ret = (int)Math.pow(priceMargin, size);
    	return offer + ret;
    }
    
    public int getNumberOfBids() {
    	BidDB bidDB = new BidDB();
    	return bidDB.SELECTByItem(this).size();
    }
    
    public String getNumberOfBidsString() {
    	return getNumberOfBids() + "";
    }
    
    public boolean isBidLegal(int bid) {
    	if(getNextLegalBid() > bid)
    		return false;
    	return true;
    }
    
    public void endAuction() {
    	//get the highest bid
    	Bid highest = getHighestBid();
    	//update the item DB
    	ItemDB itemDB = new ItemDB();
    	itemDB.UPDATEEnded(this);
    	//send mails to users
    	if(highest.getUser().equals(this.getUser())) {
    		//no one won the auction.. ):
    		General.sendMail("Status of Your Auction", "It seems that no one was interested in buying your item, you can always try again!", highest.getUser().getEmail());
    	}
    	else {
    		//someone won the auction
    		String message = "You have won an auction! The auction of- " + highest.getItem().getName() + " has been concluded with the final price of " + highest.getPrice() + "$."
    				+ "\nThe owner of this item was notified and will contact you shortly.";
    		General.sendMail("You Have Won an Auction!", message, highest.getUser().getEmail());
    		message = "The auction of item " + this.getName() + " has ended with the final price of- " + highest.getPrice() +"$!\n the winning user is- " + highest.getUser().getFullName() + ", here are some ways to contact him/her:\n "
    				+ highest.getUser().getEmail() + " / " + highest.getUser().getCellNumber();
    		General.sendMail("An Auction Has Ended!", message, this.getUser().getEmail());
    	}
    }
    
    public void deleteAuction() {
    	ItemDB itemDB = new ItemDB();
    	itemDB.UPDATEEnded(this);
    	//send mail to user
    	General.sendMail("Status of Your Auction", "It seems that you have terminated your auction. If you wish to re-add it, please notify our support @tal.feldman22@gmail.com.", user.getEmail());
    }
    
    public boolean checkAutoBids(User current) {
    	AutoBidDB autoBidDB = new AutoBidDB();
    	ArrayList<AutoBid> autoBids = autoBidDB.SELECTByItem(this);
    	ArrayList<AutoBid> autoBidsNotUser = new ArrayList<AutoBid>();
    	for(AutoBid bid:
    		autoBids) {
    		if(!bid.getUser().equals(current))
    			autoBidsNotUser.add(bid);
    	}
    	if(autoBidsNotUser.size() == 0)
    		return false;
    	ArrayList<AutoBid> higher = new ArrayList<AutoBid>();
    	for(AutoBid bid:
    		autoBidsNotUser) {
    		if(bid.getMaxBid() > getNextLegalBid()) {
    			higher.add(bid);
    		}
    	}
    	if(higher.size() == 0)
    		return false;
    	if(higher.size() == 1){
    		Bid bid = new Bid(higher.get(0).getUser(), this, new Date(), getNextLegalBid());
    		BidDB bidDB = new BidDB();
    		bidDB.INSERT(bid);
    		if(getWaiverPrice() != -1 && getWaiverPrice() <= bid.getPrice()) {
    			endAuction();
    			return true;
    		}
    		return false;
    	}
    	AutoBid first = higher.get(0);
    	AutoBid second = higher.get(0);
    	for(int i = 0; i < higher.size(); i++) {
    		if(first.getMaxBid() < higher.get(i).getMaxBid()) {
    			second = first;
    			first = higher.get(i);
    		}
    	}
    	//check if the auto bids will cycle leading one to win with higher margin than needed
    	int offer = second.getMaxBid();
    	int size = (int)Math.log10(offer);
    	int ret = (int)Math.pow(priceMargin, size);
    	Bid bid;
    	if(first.getMaxBid() > offer + ret) {
    		bid = new Bid(first.getUser(), this, new Date(), offer + ret);
    	}
    	//if not the new max bid will be the highest
    	else {
    		bid = new Bid(first.getUser(), this, new Date(), first.getMaxBid());
    	}
    	BidDB bidDB = new BidDB();
		bidDB.INSERT(bid);
		if(getWaiverPrice() != -1 && getWaiverPrice() <= bid.getPrice())
			endAuction();
    	return true;
    }
}