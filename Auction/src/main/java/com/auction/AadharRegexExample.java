package com.auction;

//Java program to check valid
//Aadhaar number using regex.

import java.util.regex.*;
class AadharRegexExample {

	// Function to validate Aadhaar number.
	public static boolean
	isValidAadhaarNumber(String str)
	{
		// Regex to check valid Aadhaar number.
		String regex
			= "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$";

		// Compile the ReGex
		Pattern p = Pattern.compile(regex);

		// If the string is empty
		// return false
		if (str == null) {
			return false;
		}

		// Pattern class contains matcher() method
		// to find matching between given string
		// and regular expression.
		Matcher m = p.matcher(str);

		// Return if the string
		// matched the ReGex
		return m.matches();
	}
	
	// Function to validate the PAN Card number.
    public static boolean isValidPanCardNo(String panCardNo)
    {
 
        // Regex to check valid PAN Card number.
        String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
 
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
 
        // If the PAN Card number
        // is empty return false
        if (panCardNo == null)
        {
            return false;
        }
 
        // Pattern class contains matcher() method
        // to find matching between given
        // PAN Card number using regular expression.
        Matcher m = p.matcher(panCardNo);
 
        // Return if the PAN Card number
        // matched the ReGex
        return m.matches();
    }

	// Driver Code.
	public static void main(String args[])
	{

		// Test Case 1:
//		String str1 = "3675 9834 6015";
//		System.out.println(isValidAadhaarNumber(str1));
//
//		// Test Case 2:
//		String str2 = "4675 9834 6012 8";
//		System.out.println(isValidAadhaarNumber(str2));
//
//		// Test Case 3:
//		String str3 = "0175 9834 6012";
//		System.out.println(isValidAadhaarNumber(str3));
//
//		// Test Case 4:
//		String str4 = "3675 98AF 60#2";
//		System.out.println(isValidAadhaarNumber(str4));
//
//		// Test Case 5:
//		String str5 = "417598346012";
//		System.out.println(isValidAadhaarNumber(str5));
//		
//		
		
		// Test Case 1:
        String str1 = "BNZAA2318J";
        System.out.println(isValidPanCardNo(str1));
 
        // Test Case 2:
        String str2 = "23ZAABN18J";
        System.out.println(isValidPanCardNo(str2));
 
        // Test Case 3:
        String str3 = "BNZAA2318JM";
        System.out.println(isValidPanCardNo(str3));
 
        // Test Case 4:
        String str4 = "BNZAA23184";
        System.out.println(isValidPanCardNo(str4));
 
        // Test Case 5:
        String str5 = "BNZAA 23184";
        System.out.println(isValidPanCardNo(str5));
	}
}

