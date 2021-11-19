package database;

public enum Products {
	MILK(2, false),
	PORK(25, true),
	EGG(3, false),
	SHRIMP(10, true),
	LOBSTER(40, true),
	KAMUT(12, false),
	WALNUTS(12, false),
	ALMONDS(15, false),
	GREEN(7, false);
	
	private int price;
	private boolean isStock;
	
	private Products(int price, boolean isStock) {
		this.price = price;
		this.isStock = isStock;
	}

	public int getPrice() {
		return price;
	}

	public boolean isStock() {
		return isStock;
	}
	
	public static Products returnProductbyId (String i) {
		Products[] arrayOfProducts = Products.values();
		return arrayOfProducts[Integer.valueOf(i)];
	}
	
}