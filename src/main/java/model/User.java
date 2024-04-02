package model;

import java.util.ArrayList;

import viewModel.AutoBidDB;

public class User extends BaseEntity {
    private String email;
    private String fullName;
    private String cellNumber;

    public User() {}

    public User(String email, String fullName, String cellNumber) {
        this.email = email;
        this.fullName = fullName;
        this.cellNumber = cellNumber;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }
    
    @Override
    public boolean equals(Object o) {
    	if(o == null)
    		return false;
    	return ((User)o).cellNumber.equals(this.cellNumber);
    }
    
    public AutoBid getAutoBidByItem(Item item) {
    	AutoBidDB autoBidDB = new AutoBidDB();
    	ArrayList<AutoBid> autoBids = autoBidDB.SELECTByUser(this);
    	if(autoBids.size() == 0)
    		return null;
    	AutoBid max = autoBids.get(0);
    	for(AutoBid current:
    		autoBids) {
    		if(current.getMaxBid() > max.getMaxBid())
    			max = current;
    	}
    	return max;
    }
}