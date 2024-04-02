package model;

import java.util.Date;

public class Bid extends BaseEntity{
    private User user;
    private Item item;
    private Date date;
    private int price;

    public Bid(){}

    public Bid(User user, Item item, Date date, int price) {
        this.user = user;
        this.item = item;
        this.date = date;
        this.price = price;
    }

    /*public static Bid getNewBidByAutoBid(AutoBid autoBid){
        return new Bid(autoBid.getUser(), autoBid.getItem(), new Date(), autoBid.getNextBid());
    }*/

    // Getters
    public User getUser() {
        return user;
    }

    public Item getItem() {
        return item;
    }

    public Date getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if(!(obj instanceof Bid))
    		return false;
    	if(((Bid)obj).getItem().equals(this.getItem()) && ((Bid)obj).getPrice() == this.getPrice())
    		return true;
    	return false;
    }
}