package validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

	private static boolean firstValidation(String text) {
		boolean verification = false;
		Pattern pattern = Pattern.compile("(?:[012345678]-[0123456789]_)+[012345678]-[0123456789]{1}");
		Matcher matcher  = pattern.matcher(text);
		if (matcher.matches()) {
			verification = true;
		}
		return verification;
	}//method
	
	private static boolean secondValidation(String text) {
		boolean verification = false;
		Pattern pattern = Pattern.compile("(?:[012345678]-[0123456789])");
		Matcher matcher  = pattern.matcher(text);
		if (matcher.matches()) {
			verification = true;
		}
		return verification;
	}//method
	
	public static boolean finalValidation(String text) {
		boolean resultOfValidation = false;
		if(firstValidation(text)) {
			resultOfValidation = true;
		}
		if(secondValidation(text)) {
			resultOfValidation = true;
		}
		return resultOfValidation;	
	}//method
}
