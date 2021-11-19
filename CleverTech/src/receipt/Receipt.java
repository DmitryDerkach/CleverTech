package receipt;

import database.Products;

public class Receipt {
	
	private Products [] listofProducts = new Products[Products.values().length];
	private int [] quantityOfProducts = new int[Products.values().length];
	private int [] priceOfProducts = new int[Products.values().length];
	private int [] totalPriceOfProducts = new int[Products.values().length];
	
	
	public Receipt() {

	}
	
	public void setProducts(String cellOfArray, Products products) {
		listofProducts[Integer.valueOf(cellOfArray)] = products;
	}

	public void setQuantity(String cellOfArray, String value) {
		quantityOfProducts[Integer.valueOf(cellOfArray)] += Integer.valueOf(value);
	}
	
	public void setPrice (String cellOfArray) {
		priceOfProducts[Integer.valueOf(cellOfArray)] = Products.returnProductbyId(cellOfArray).getPrice();
	}
	
	public void setTotalPrice(String cellOfArray) {
		int checkQuantityNumber = getQuantity(Integer.valueOf(cellOfArray));
		/*10% скидка*/
		if (checkQuantityNumber > 5) {
			totalPriceOfProducts[Integer.valueOf(cellOfArray)] = getPrice(Integer.valueOf(cellOfArray)) * getQuantity(Integer.valueOf(cellOfArray)) - 
			(totalPriceOfProducts[Integer.valueOf(cellOfArray)] = getPrice(Integer.valueOf(cellOfArray)) * getQuantity(Integer.valueOf(cellOfArray))/10);
		} else {
			totalPriceOfProducts[Integer.valueOf(cellOfArray)] = getPrice(Integer.valueOf(cellOfArray)) * getQuantity(Integer.valueOf(cellOfArray));
		}
		
	}
	
	
	////////////////
	

	public int getQuantity (int cellOfArray) {
		return quantityOfProducts[cellOfArray];
	}
	
	public String getDescription (int cellOfArray) {
		return listofProducts[cellOfArray].toString();
	}
	
	public int getPrice (int cellOfArray) {
		return priceOfProducts[cellOfArray];
	}
	
	public int getTotalPrice(int cellOfArray) {
		return totalPriceOfProducts[cellOfArray];
	}
	

}
