package bonuscards;

public enum BonusCards {
	
	CARDNUMBER1(111, 10),
	CARDNUMBER2(222, 20),
	CARDNUMBER3(333, 30);
	
	private int uniqueNum;
	private int discountValue;
	
	private BonusCards(int uniqueNum, int discountValue) {
		this.uniqueNum = uniqueNum;
		this.discountValue = discountValue;
	}

	public int getUniqueNum() {
		return uniqueNum;
	}

	public static int getDiscountValue(String value) {
		int discount = 0;
		BonusCards [] array = BonusCards.values();
		for (int i = 0; i < array.length; i++) {
			if (Integer.valueOf(value) == array[i].getUniqueNum()) {
				discount =  array[i].discountValue;
			}
		}
		return discount;
	}

}
