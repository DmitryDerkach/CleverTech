package runner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.sound.midi.Soundbank;

import bonuscards.BonusCards;
import database.Products;
import receipt.Receipt;

public class ApplicationRunner {
	static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("Hello! Welcome to the self-service checkout. Please select one of the avaible numbers below:");
			do {
				outer: {
					showOptions();
					//Scanner scanner = new Scanner(System.in);
					String input = scanner.nextLine();
					switch (input) {
						case "1" : {
							showListOfProducts();
							System.out.println("Do you want to continue? Y/N");
							input = scanner.nextLine();
							switch (input) {
								case "Y": {
									break outer;
								}
								case "N": {
									endOfShoping();
								}
								default: {
									System.out.println("Wrong input. Please, try again");
								}
							}
							break;
						}
						case "2" : {
							Receipt generatedReceipt = generateReceipt();
							outputReceipt(generatedReceipt);
							System.out.println("Do you want to save Receipt? Y/N");
							input = scanner.nextLine();
							switch (input) {
								case "Y": {
									saveReceipt(outputReceipt(generatedReceipt));
								}
								case "N": {
									endOfShoping();
								}
								default: {
									System.out.println("Wrong input. Please, try again");
								}
								break;
							}//inner switch
						}//case 2
						case "3" : {
							endOfShoping();
						}
						default : {
							System.out.println("You entered invalid number. Please, try again");
						}
					}
				}//outer
			} while (true);
	}//main
	
	private static void saveReceipt(String generatedReceipt) {
        try(FileOutputStream fos=new FileOutputStream("FormedReceipt//Receipt.txt");
            PrintStream printStream = new PrintStream(fos)) {
            printStream.println(generatedReceipt);
            System.out.println("Receipt was succassfully saved in -FormedReceipt- folder");
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }  
	}//method

	private static void showListOfProducts() {
		System.out.println("List of avaible products:\n");
		for (Products itterator: Products.values()) {
				if (itterator.isStock()) {
					System.out.println(itterator.ordinal() + ". " + itterator.toString() + " - " + itterator.getPrice() + "$" + "  " + "Stock item!");
				} else {
					System.out.println(itterator.ordinal() + ". " + itterator.toString() + " - " + itterator.getPrice() + "$");
				}
		}
	}//method
	
	private static void showOptions() {
		System.out.println("1. Print list of all products\n"
				+ "2. Generate purchase receipt\n"
				+ "3. Exit");
	}//method
	
	private static void endOfShoping() {
		System.out.println("Come shopping again!");
		System.exit(0);
	}//method
	
	private static Receipt generateReceipt() {
		System.out.println("Please, fill out the form according to the rule below:\n"
				+ "in-quantity_in-quantity_etc\n"
				+ "id - identification number according to list of avaible products\n"
				+ "quantity - number of particular product that you want to buy\n"
				+ "_ - separator which allow program to processing your request\n\n"
				+ "Please, form your Receipt:");
		String customerInput = scanner.nextLine();
		String [] separatedParts = customerInput.split("_");
		Receipt receipt = new Receipt();
		for (int i = 0; i < separatedParts.length; i++) {
			receipt.setProducts(separatedParts[i].charAt(0) + "", Products.returnProductbyId(separatedParts[i].charAt(0) + ""));
			receipt.setQuantity(separatedParts[i].charAt(0) + "", separatedParts[i].charAt(separatedParts[i].length() - 1) + "");
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
		System.out.println("Do you have bonus card? Y/N");
		String input = scanner.nextLine();
		switch (input) {
			case "Y": {
				System.out.println("Please, insert ID of your bonus card");
				String inutV2 = scanner.nextLine();
				int DiscountValue = BonusCards.getDiscountValue(inutV2);
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
		return footer;
	}//method
}//class
