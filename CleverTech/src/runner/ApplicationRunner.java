package runner;

import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import validator.*;
import bonuscards.BonusCards;
import database.Products;
import receipt.Receipt;

public class ApplicationRunner {
	static Scanner scanner = new Scanner(System.in);
	static String[] argsValueContainer = null;
	public static void main(String[] args) {
		argsValueContainer = args;
		System.out.println("Hello! Welcome to the self-service checkout. Please select one of the avaible numbers below:");
			do {
				showStoreAvaibleOptions();
				String input = scanner.nextLine();
				switch (input) {
					case "1" : {
						showListOfProducts();
						showUserAvaibleOptions();
						break;
					}//case 1
					case "2" : {
						Receipt generatedReceipt = generateReceipt();
						String formedReceipt = outputReceipt(generatedReceipt);
						saveOrNotChoise(formedReceipt);
						break;
					}//case 2
					case "3" : {
						if (args.length == 0) {
							System.out.println("User predifine information wasn't given!");
							showUserAvaibleOptions();
							break;
						} else {
							try {
								Receipt generatedReceipt = generateReceipt(argsValueContainer[0]);
								String formedReceipt = outputReceipt(generatedReceipt);
								saveOrNotChoise(formedReceipt);
							} catch (NullPointerException e) {
								System.out.println("No predifine information was given!");
							} finally {
								showUserAvaibleOptions();
							}
						}
						break;
					}//case3
					case "4" : {
						endOfShoping();
					}//case4
					default : {
						System.out.println("You entered invalid number. Please, try again");
					}//default
				}
			} while (true);	
	}//main
		
	private static void saveReceipt(String generatedReceipt) {
        try(PrintStream printStream = new PrintStream("Receipt.txt", "UTF-8")) {
            printStream.println(generatedReceipt);
            System.out.println("Receipt was succassfully saved!");
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }  
	}//method

	private static void showListOfProducts() {
		System.out.println("List of avaible products:");
		for (Products itterator: Products.values()) {
				if (itterator.isStock()) {
					System.out.println(itterator.ordinal() + ". " + itterator.toString() + " - " + itterator.getPrice() + "$" + "  " + "Stock item!");
				} else {
					System.out.println(itterator.ordinal() + ". " + itterator.toString() + " - " + itterator.getPrice() + "$");
				}
		}
	}//method
	
	private static void endOfShoping() {
		System.out.println("Come shopping again!");
		System.exit(0);
	}//method
	
	private static Receipt generateReceipt() {
		String customerInput = userInputForReceiptValidation();
		String [] separatedParts = customerInput.split("_");
		Receipt receipt = new Receipt();
		for (int i = 0; i < separatedParts.length; i++) {
			receipt.setProducts(separatedParts[i].charAt(0) + "", Products.returnProductbyId(separatedParts[i].charAt(0) + ""));
			receipt.setQuantity(separatedParts[i].charAt(0) + "", separatedParts[i].substring(2, separatedParts[i].length()));
			receipt.setPrice(separatedParts[i].charAt(0) + "");
			receipt.setTotalPrice(separatedParts[i].charAt(0) + "");
		}
		return receipt;
	}//method
	
	private static Receipt generateReceipt(String commandLineData) {
		if (!Validator.finalValidation(commandLineData)) {
			System.out.println("User predifine information was given incorect!");
			showUserAvaibleOptions();
			ApplicationRunner.main(null);
		}
		String [] separatedParts = commandLineData.split("_");
		Receipt receipt = new Receipt();
		for (int i = 0; i < separatedParts.length; i++) {
			receipt.setProducts(separatedParts[i].charAt(0) + "", Products.returnProductbyId(separatedParts[i].charAt(0) + ""));
			receipt.setQuantity(separatedParts[i].charAt(0) + "", separatedParts[i].substring(2, separatedParts[i].length()));
			receipt.setPrice(separatedParts[i].charAt(0) + "");
			receipt.setTotalPrice(separatedParts[i].charAt(0) + "");
		}
		return receipt;
	}//method
	
	private static String outputReceipt(Receipt generatedReceipt) {
		String receipt = null;
		
		String separator = "-------------------------------";
		
		String header = String.format("      CASH RECEIPT\n"
				+ "    Supermarket 123\n"
				+ "12, Milkyway galaxy/ Earth\n"
				+ "Tel: 123-456-7890\n"
				+ "Cashier: #1520  Date:%tF\n"
				+ "		Time:%tR", LocalDate.now(), LocalTime.now());
		
		String description = "QTY  DESCRIPTION  PRICE  TOTAL";
		
		StringBuffer body = new StringBuffer();
		String bodypart = null;
			for (int i = 0; i < Products.values().length; i++) {
				if (generatedReceipt.getQuantity(i) == 0) {
					continue;
				} else {
					bodypart = String.format("%2d %8s %9d %6d ",
					generatedReceipt.getQuantity(i),
					generatedReceipt.getDescription(i),
					generatedReceipt.getPrice(i),
					generatedReceipt.getTotalPrice(i));
					body.append(bodypart + "\n");
				}
			}
			
		String 	footer =  bonusCardInfluence(generatedReceipt);
			
		receipt = separator + "\n" + header + "\n" + separator + "\n" + 
			      description + "\n" + body + separator + "\n" + footer;

		System.out.println(receipt);

		return receipt;
	}//method
	
	private static String bonusCardInfluence(Receipt receipt) {
		String footer = null;
		int sumOfAllProducts = 0;
		for (int i = 0; i < Products.values().length; i++) {
			if (receipt.getQuantity(i) == 0) {
				continue;
			} else {
				sumOfAllProducts += receipt.getTotalPrice(i);
			}
		}
		/*Блок для обработки командной строки*/
		if (argsValueContainer.length == 2) {
			if(!bonusCardValidation(argsValueContainer[1])) {
				System.out.println("Predifined information about card was given inccor");
			}
			int DiscountValue = BonusCards.getDiscountValue(argsValueContainer[1]);
			footer = String.format("Intermediate amount:      $%d\n"
        			 			 + "Discount:            	  %d%%\n"
        			 			 + "TOTAL:            	  $%d", sumOfAllProducts, DiscountValue, sumOfAllProducts -
        			 (DiscountValue * sumOfAllProducts)/100);
		} else { 
			if (argsValueContainer.length == 1) {
				footer = String.format("Intermediate amount:      $%d\n"
			 						 + "Discount:            	  NO\n"
			 			             + "TOTAL:            	  $%d", sumOfAllProducts, sumOfAllProducts);
			} else {
				System.out.println("Do you have bonus card? Y/N");
				String input = scanner.nextLine();
				switch (input) {
					case "Y": {
						StringBuilder customerInput = new StringBuilder();
						do {
							System.out.println("Please, insert ID of your bonus card");
							customerInput.append (scanner.nextLine());
							if (!bonusCardValidation(customerInput.toString())) {
								System.out.println("This card does not exist!");
								showUserAvaibleOptions();
								customerInput.delete(0, customerInput.length());
							} else {
								break;
							}
						} while(true);
						int DiscountValue = BonusCards.getDiscountValue(customerInput.toString());
						footer = String.format("Intermediate amount:      $%d\n"
					             			 + "Discount:            	  %d%%\n"
					             			 + "TOTAL:            	  $%d", sumOfAllProducts, DiscountValue, sumOfAllProducts -
					             			 (DiscountValue * sumOfAllProducts)/100);
						break;	
					}
					case "N": {
						footer = String.format("Intermediate amount:      $%d\n"
			        			 			+ "Discount:            	  NO\n"
			        			 			+ "TOTAL:            	  $%d", sumOfAllProducts, sumOfAllProducts);
						break;
					}
					default: {
						System.out.println("Wrong input. Please, try again");
					}
				}//switch
			}//inner else
		}//outer else
		return footer;
	}//method
	
	private static void showUserAvaibleOptions() {
		do {
			System.out.println("Do you want to continue? Y/N");
			String input = scanner.nextLine();
			switch (input) {
				case "Y": {
					return;
				}
				case "N": {
					endOfShoping();
				}
				default: {
					System.out.println("Wrong input. Please, try again");
				}
			}
		} while (true);
	}//method
	
	private static void showStoreAvaibleOptions() {
		System.out.println("1. Print list of all products\n"
				+ "2. Generate purchase receipt\n"
				+ "3. Generate  purchase receipt from userpredefined input\n"
				+ "4. Exit");
	}//method
	
	private static void saveOrNotChoise(String generatedReceipt) {
		System.out.println("Do you want to save Receipt? Y/N");
		String input = scanner.nextLine();
		switch (input) {
			case "Y": {
				saveReceipt(generatedReceipt);
				showUserAvaibleOptions();
				break;
			}
			case "N": {
				endOfShoping();
			}
			default: {
				System.out.println("Wrong input. Please, try again");
			}
		}
	}//method
	
	private static void generateReceiptDescription() {
		System.out.println("Please, fill out the form according to the rule below:\n"
				+ "in-quantity_in-quantity_etc\n"
				+ "id - identification number according to list of avaible products\n"
				+ "quantity - number of particular product that you want to buy\n"
				+ "_ - separator which allow program to processing your request\n\n"
				+ "Please, form your Receipt:");
	}//method
	
	private static String userInputForReceiptValidation() {
		StringBuilder customerInput = new StringBuilder();
		do {
			generateReceiptDescription();
			customerInput.append (scanner.nextLine());
			if (!Validator.finalValidation(customerInput.toString())) {
				System.out.println("Wrong input!");
				showUserAvaibleOptions();
				customerInput.delete(0, customerInput.length());
			} else {
				break;
			}
		} while (true);
		return customerInput.toString();
	}//method
	
	private static boolean bonusCardValidation(String cardNumber) {
		boolean cardValidation = false;
		BonusCards [] arrayOfCards = BonusCards.values();
		for (int i = 0; i < BonusCards.values().length; i++) {
			try {
				if (Integer.valueOf(cardNumber) == arrayOfCards[i].getUniqueNum()) {
					cardValidation = true;
					break;
				}
			} catch (Exception e) {
				break;
			}
		}//for
		return cardValidation;	
	}//method
	
}//class
