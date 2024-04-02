package model;

public class AutoBid extends BaseEntity{
    private User user;
    private Item item;
    private int maxBid;

    public AutoBid(){}

    public AutoBid(User user, Item item, int maxBid) {
        this.user = user;
        this.item = item;
        this.maxBid = maxBid;
    }

    // Getters
    public User getUser() {
        return user;
    }

    public Item getItem() {
        return item;
    }

    public int getMaxBid() {
        return maxBid;
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setMaxBid(int maxBid) {
        this.maxBid = maxBid;
    }
    
    public String getMaxBidString() {
    	return maxBid + "";
    }
}