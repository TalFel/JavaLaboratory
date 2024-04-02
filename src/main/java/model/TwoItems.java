package model;

public class TwoItems {
	private Item item1;
	private Item item2;
	
	public TwoItems(Item item1, Item item2) {
		this.item1 = item1;
		this.item2 = item2;
	}

	public Item getItem1() {
		return item1;
	}

	public Item getItem2() {
		return item2;
	}
	
	public void setItem1(Item item1) {
		this.item1 = item1;
	}

	public void setItem2(Item item2) {
		this.item2 = item2;
	}
	
}
