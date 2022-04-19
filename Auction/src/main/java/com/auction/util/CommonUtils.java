package com.auction.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.text.WordUtils;

/**
 * This class contains common utility methods
 * 
 */
public class CommonUtils {

	private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

	static {
		map.put(1000, "M");
		map.put(900, "CM");
		map.put(500, "D");
		map.put(400, "CD");
		map.put(100, "C");
		map.put(90, "XC");
		map.put(50, "L");
		map.put(40, "XL");
		map.put(10, "X");
		map.put(9, "IX");
		map.put(5, "V");
		map.put(4, "IV");
		map.put(1, "I");
	}

	/**
	 * A method to check if one of the <code>elements</code> matches the String
	 * to <code>check</code>
	 * 
	 * @param check
	 * @param elements
	 * @return <code>true</code>, if
	 */
	public static boolean isOneOf(String check, String... elements) {
		for (String element : elements) {
			if (element == null && check == null) {
				return true;
			} else if (element.equalsIgnoreCase(check)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method checks to see if the given Collection is null or contains no
	 * object.
	 * 
	 * @param val
	 *            passed Collection object
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(final Collection val) {
		return (val == null || (val.size() == 0));
	}

	/**
	 * This method checks to see if the given Map is null or contains no object.
	 * 
	 * @param val
	 *            passed Map object
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(final Map val) {
		return (val == null || (val.size() == 0));
	}

	/**
	 * This method checks to see if the given Object is null or not.
	 * 
	 * @param obj
	 *            object to be checked
	 * @return boolean
	 */
	public static boolean isEmpty(final Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof String) {
			return isEmpty((String) obj);
		}
		return false;
	}

	/**
	 * This method checks to see if the given String is null or blank.
	 * 
	 * @param val
	 *            passed String object
	 * @return boolean
	 */
	public static boolean isEmpty(final String val) {
		return (val == null || val.trim().equals(""));
	}

	/**
	 * This method checks whether the given String is purely numeric.
	 * 
	 * @param str
	 *            A non-null string
	 * @return boolean If str is alphabetic
	 */
	public static boolean isNumeric(String str) {
		if (isEmpty(str)) {
			return false;
		}
		return str.matches("[0-9]*");
	}

	/**
	 * This method checks whether the given String is numeric with decimal.
	 * 
	 * @param str
	 *            A non-null string
	 * @return boolean If str is alphabetic
	 */
	public static boolean isNumericAndDecimal(String str) {
		try {
			Float.parseFloat(str);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * This method is used to get the complete stack trace
	 * 
	 * @param throwable
	 * @return String
	 */
	public static String getStackTrace(Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		return writer.toString();
	}

	public static Date stringToDate(String str) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		formatter.setLenient(false);
		return formatter.parse(str);
	}

	public static Date convertDate(String str) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		formatter.setLenient(false);
		return formatter.parse(str);
	}

	public static String dateToString(Date strDate, String strFormat) {
		String strRetDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		try {
			strRetDate = sdf.format(strDate);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return strRetDate;
	}

	public static String convertListToString(List<String> list) {
		if (!isEmpty(list)) {
			return list.toString().replace("[", "").replace("]", "");
		}
		return null;
	}

	public static String convertFloatListToString(List<Float> list) {
		if (!isEmpty(list)) {
			return list.toString().replace("[", "").replace("]", "");
		}
		return null;
	}

	public static String convertFloatListofListToString(List<ArrayList<Float>> list) {
		String convertedString = "";
		if (!isEmpty(list)) {
			for (ArrayList<Float> insideList : list) {
				int i = 0;
				for (Float f : insideList) {
					if (i == 0) {
						convertedString = convertedString + f;
					} else {
						convertedString = convertedString + "," + f;
					}
					i++;
				}
				convertedString = convertedString + ";";
			}
		}
		return convertedString;
	}

	public static String formatMessage(String message, Object... arguments) {
		return MessageFormat.format(message, (Object[]) arguments);
	}

	public static Map<String, String> sortMapByValue(Map<String, String> allCategories) {

		MapComparator comp = new CommonUtils().new MapComparator(allCategories);
		Map<String, String> finalMap = new TreeMap<String, String>(comp);
		finalMap.putAll(allCategories);
		return finalMap;
	}

	private class MapComparator implements Comparator<String> {

		private Map<String, String> tempMap = null;

		MapComparator(Map<String, String> map) {
			tempMap = map;
		}

		public int compare(String o1, String o2) {
			return (tempMap.get(o1).compareTo(tempMap.get(o2)));
		}

	}

	/**
	 * Counter part for isEmpty.
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNotEmpty(final Collection<?> val) {
		return !CommonUtils.isEmpty(val);
	}

	/**
	 * Counter part for isEmpty.
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNotEmpty(final Map<?, ?> val) {
		return !CommonUtils.isEmpty(val);
	}

	/**
	 * Counter part for isEmpty.
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNotEmpty(final Object val) {
		return !CommonUtils.isEmpty(val);
	}

	/**
	 * Counter part for isEmpty.
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNotEmpty(final Object[] val) {
		return !CommonUtils.isEmpty(val);
	}

	/**
	 * Counter part for isEmpty.
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNotEmpty(final String val) {
		return !CommonUtils.isEmpty(val);
	}

	/**
	 * Counter part for isEmpty.
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNotEmpty(final String[] val) {
		return !CommonUtils.isEmpty(val);
	}

	/**
	 * Counter part for isEmpty.
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(final Integer[] val) {
		boolean status = false;
		if (val == null) {
			status = true;
		} else if (val.length < 1) {
			status = true;
		}
		return status;
	}

	/**
	 * Counter part for isEmpty.
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNotEmpty(final Integer[] val) {
		return !isEmpty(val);
	}

	
	@SuppressWarnings("deprecation")
	public static float roundUp(double d, int precision) {
		return (new BigDecimal(d).setScale(precision, BigDecimal.ROUND_HALF_UP).floatValue());
	}

	@SuppressWarnings("deprecation")
	public static float roundOff(double d, int precision, String roundingDir, String isRoundOff) {
		if ("Y".equalsIgnoreCase(isRoundOff)) {
			if ("Higher".equalsIgnoreCase(roundingDir)) {
				return (new BigDecimal(d).setScale(precision, BigDecimal.ROUND_UP).floatValue());
			} else if ("Lower".equalsIgnoreCase(roundingDir)) {
				return (new BigDecimal(d).setScale(precision, BigDecimal.ROUND_DOWN).floatValue());
			} else {
				return (new BigDecimal(d).setScale(precision, BigDecimal.ROUND_HALF_UP).floatValue());
			}
		} else {
			return new BigDecimal(d).floatValue();
		}

	}

	@SuppressWarnings("deprecation")
	public static int roundUpToInt(float value, int precision) {
		return (new BigDecimal(value).setScale(precision, BigDecimal.ROUND_HALF_UP).intValue());
	}

	@SuppressWarnings("deprecation")
	public static int roundOff(float value, int precision) {
		return (new BigDecimal(value).setScale(precision, BigDecimal.ROUND_HALF_UP).intValue());
	}

	public static float roundUpToNearest10(float value) {
		return (float) (roundOff(((value) / 10), 0) * 10);
	}

	/**
	 * This method finds out the employee category based on his age and gender
	 * 
	 * @param Date
	 *            dob
	 * @param String
	 *            gender
	 * 
	 */

	/**
	 * Generate MD5 value from input value
	 * 
	 * @param input
	 * @return
	 */
	public static String getMd5(final String input) {
		String md5 = null;
		if (null == input)
			return null;
		try {
			// Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");
			// Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());
			// Converts message digest value in base 16 (hex)
			md5 = new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5;
	}

	public static boolean isCommaSep(String input) {
		boolean valid = false;
		input = input.replaceAll("\\s+", "");
		if (input.matches("^([a-zA-Z0-9]+(,[a-zA-Z0-9]+)*)+$")) {
			valid = true;
		}
		return valid;
	}

	/**
	 * This method is used to get drop down value of month.
	 * 
	 * @return
	 */
	public static Map<Integer, String> getMonthMap() {
		final Map<Integer, String> monthMap = new LinkedHashMap<Integer, String>();
		monthMap.put(10, "January");
		monthMap.put(11, "February");
		monthMap.put(12, "March");
		monthMap.put(1, "April");
		monthMap.put(2, "May");
		monthMap.put(3, "June");
		monthMap.put(4, "July");
		monthMap.put(5, "August");
		monthMap.put(6, "September");
		monthMap.put(7, "October");
		monthMap.put(8, "November");
		monthMap.put(9, "December");
		return monthMap;
	}

	public static Map<Integer, String> getCalMonthMap() {
		final Map<Integer, String> monthMap = new LinkedHashMap<Integer, String>();
		monthMap.put(1, "January");
		monthMap.put(2, "February");
		monthMap.put(3, "March");
		monthMap.put(4, "April");
		monthMap.put(5, "May");
		monthMap.put(6, "June");
		monthMap.put(7, "July");
		monthMap.put(8, "August");
		monthMap.put(9, "September");
		monthMap.put(10, "October");
		monthMap.put(11, "November");
		monthMap.put(12, "December");
		return monthMap;
	}

	public static Map<String, String> getMonthtoIntMap() {
		final Map<String, String> monthMap = new LinkedHashMap<>();
		monthMap.put("1", "10");
		monthMap.put("2", "11");
		monthMap.put("3", "12");
		monthMap.put("4", "1");
		monthMap.put("5", "2");
		monthMap.put("6", "3");
		monthMap.put("7", "4");
		monthMap.put("8", "5");
		monthMap.put("9", "6");
		monthMap.put("10", "7");
		monthMap.put("11", "8");
		monthMap.put("12", "9");
		return monthMap;
	}

	public static Map<String, String> getMonthFromIntMap() {
		final Map<String, String> monthMap = new LinkedHashMap<>();
		monthMap.put("10", "1");
		monthMap.put("11", "2");
		monthMap.put("12", "3");
		monthMap.put("1", "4");
		monthMap.put("2", "5");
		monthMap.put("3", "6");
		monthMap.put("4", "7");
		monthMap.put("5", "8");
		monthMap.put("6", "9");
		monthMap.put("7", "10");
		monthMap.put("8", "11");
		monthMap.put("9", "12");
		return monthMap;
	}

	public static Map<Integer, String> getQuarterMap() {
		final Map<Integer, String> monthMap = new LinkedHashMap<Integer, String>();
		monthMap.put(6, "Q1 (April - June)");
		monthMap.put(9, "Q2 (July - September)");
		monthMap.put(12, "Q3 (October - December)");
		monthMap.put(3, "Q4 (January - March)");
		return monthMap;
	}

	public static Map<String, String> getMonthMapKeyString() {
		final Map<String, String> monthMap = new LinkedHashMap<String, String>();
		monthMap.put("10", "January");
		monthMap.put("11", "February");
		monthMap.put("12", "March");
		monthMap.put("1", "April");
		monthMap.put("2", "May");
		monthMap.put("3", "June");
		monthMap.put("4", "July");
		monthMap.put("5", "August");
		monthMap.put("6", "September");
		monthMap.put("7", "October");
		monthMap.put("8", "November");
		monthMap.put("9", "December");
		return monthMap;
	}
	public static String getClientIpAddr(HttpServletRequest request) {  
	    String ip = request.getHeader("X-Forwarded-For");  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("Proxy-Client-IP");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_X_FORWARDED");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_CLIENT_IP");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_FORWARDED_FOR");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_FORWARDED");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("HTTP_VIA");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getHeader("REMOTE_ADDR");  
	    }  
	    if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	}

	public static int getNumberDaysInMonth(int monthNumber, Date forYear){
		monthNumber = monthNumber + 3;
		if (monthNumber > 12) {
			monthNumber = monthNumber - 12;
		}
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(forYear);
		int year = calendar.get(Calendar.YEAR);
		int lwdMonth = calendar.get(Calendar.MONTH) +1;
		Calendar mycal = new GregorianCalendar(year, monthNumber-1, 1);
		if(monthNumber == lwdMonth){			
			int lastDayOfMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
			mycal.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
			
			Date maxDate = mycal.getTime();
			Date toBeConsidered = maxDate.compareTo(forYear) > 0 ? forYear : maxDate;
			
			Calendar calendarToBeConsidered = new GregorianCalendar();
			calendarToBeConsidered.setTime(toBeConsidered);
			
			return calendarToBeConsidered.get(Calendar.DAY_OF_MONTH);			
		}else{
			return mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		
	}
	

	public static HashMap<Integer, Object> convertListToMap(HashMap<Integer, Object> mapToBePopulated,
			List<?> listToBeConverted, String keyGetMethodName) {
		try {
			for (Object obj : listToBeConverted) {
				Method method = obj.getClass().getMethod(keyGetMethodName);
				mapToBePopulated.put((Integer) method.invoke(obj), obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapToBePopulated;
	}

	public static String convertKeySetToString(HashMap<?, ?> mapToBeConverted) {
		String keyValue = "";
		int i = 0;
		Iterator<?> mapIterator = mapToBeConverted.entrySet().iterator();
		while (mapIterator.hasNext()) {
			Map.Entry<?, ?> entry = (Entry<?, ?>) mapIterator.next();
			if (i == 0) {
				keyValue = keyValue + entry.getKey();
			} else {
				keyValue = keyValue + "," + entry.getKey();
			}
			i++;
		}
		return keyValue;
	}

	public static String convertValueToString(HashMap<?, ?> mapToBeConverted) {
		String keyValue = "";
		int i = 0;
		Iterator<?> mapIterator = mapToBeConverted.entrySet().iterator();
		while (mapIterator.hasNext()) {
			Map.Entry<?, ?> entry = (Entry<?, ?>) mapIterator.next();
			if (i == 0) {
				keyValue = keyValue + entry.getValue();
			} else {
				keyValue = keyValue + "," + entry.getValue();
			}
			i++;
		}
		return keyValue;
	}

	public static String[] cleanArrayOfBlankNullValues(String[] allElements) {
		int n = 0;
		for (int i = 0; i < allElements.length; i++)
			if (allElements[i] != null && allElements[i].trim() != "")
				n++;

		String[] _localAllElements = new String[n];

		int j = 0;
		for (int i = 0; i < allElements.length; i++)
			if (allElements[i] != null && allElements[i].trim() != "")
				_localAllElements[j++] = allElements[i];

		return _localAllElements;
	}

	public static double calculateTotalOfArray(String[] toBeAddedArray) {
		double total = 0;
		for (String toBeAdded : toBeAddedArray) {
			total = total + Double.parseDouble(toBeAdded);
		}
		return total;
	}

	/**
	 * @param startyear
	 * @param endyear
	 * @param totalMonths
	 * @return
	 */
	public static String[] returnTotalShortMonths(String startyear, String endyear, int totalMonths) {
		String[] monthNames = new String[12];
		String[] shortMonths = new DateFormatSymbols().getShortMonths();
		int i = 0;
		int totalCount = 0;
		for (String shortMonth : shortMonths) {
			if (i > 2 && totalCount < totalMonths && i < 12) {
				monthNames[totalCount] = shortMonth + "'" + startyear;
				totalCount++;
			}
			i++;
		}
		i = 0;
		for (String shortMonth : shortMonths) {
			if (i < 3 && totalCount < totalMonths) {
				monthNames[totalCount] = shortMonth + "'" + endyear;
				totalCount++;
			} else {
				break;
			}
			i++;
		}
		return monthNames;
	}

	public static String[] numOfMonthDaysArray(String[] monthNames) {
		String[] shortMonths = new DateFormatSymbols().getShortMonths();
		List<String> shortMonthsList = Arrays.asList(shortMonths);
		String[] monthDays = new String[12];
		int i = 0;
		for (String monthName : monthNames) {
			String shortMonthName = monthName.substring(0, monthName.indexOf("'"));
			int month = shortMonthsList.indexOf(shortMonthName);
			int year = Integer.parseInt("20" + monthName.substring(monthName.indexOf("'") + 1));
			Calendar mycal = new GregorianCalendar(year, month, 1);
			monthDays[i] = String.valueOf(mycal.getActualMaximum(Calendar.DAY_OF_MONTH));
			i++;
		}
		return monthDays;
	}

	public static String formatNumber(float num) {
		DecimalFormat df = new DecimalFormat("#.00");
		df.setMaximumFractionDigits(2);
		return df.format(num);
	}

	/**
	 * Parses the string argument as a boolean. The {@code boolean} returned
	 * represents the value {@code true} if the string argument is not
	 * {@code null} and is equal, ignoring case, to the string {@code "true"}.
	 * <p>
	 * Example: {@code Boolean.parseBoolean("True")} returns {@code true}.<br>
	 * Example: {@code Boolean.parseBoolean("yes")} returns {@code false}.
	 *
	 * @param s
	 *            the {@code String} containing the boolean representation to be
	 *            parsed
	 * @return the boolean represented by the string argument
	 * @since 1.5
	 */
	public static boolean parseBoolean(String s) {
		return toBoolean(s);
	}

	private static boolean toBoolean(String name) {
		return ((name != null) && name.equalsIgnoreCase("true"));
	}

	public static boolean validateUploadFileName(String strFileName) {
		boolean flgStatus = false;
		if (strFileName != null && !"".equals(strFileName)) {
			String srs = "([a-zA-Z0-9]{1,150}.[a-zA-Z0-9]{1,10})";
			flgStatus = strFileName.matches(srs);
		}
		return flgStatus;
	}

	public final static String toRoman(int number) {
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1000, "M");
		map.put(900, "CM");
		map.put(500, "D");
		map.put(400, "CD");
		map.put(100, "C");
		map.put(90, "XC");
		map.put(50, "L");
		map.put(40, "XL");
		map.put(10, "X");
		map.put(9, "IX");
		map.put(5, "V");
		map.put(4, "IV");
		map.put(1, "I");

		int l = map.floorKey(number);
		if (number == l) {
			return map.get(number);
		}
		return map.get(l) + toRoman(number - l);
	}

	public final static String toRomanSmall(int number) {
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1000, "m");
		map.put(900, "cm");
		map.put(500, "d");
		map.put(400, "cd");
		map.put(100, "c");
		map.put(90, "xc");
		map.put(50, "l");
		map.put(40, "xl");
		map.put(10, "x");
		map.put(9, "ix");
		map.put(5, "v");
		map.put(4, "iv");
		map.put(1, "i");

		int l = map.floorKey(number);
		if (number == l) {
			return map.get(number);
		}
		return map.get(l) + toRomanSmall(number - l);
	}

	public static String getCharForNumber(int i) {
		return i > 0 && i < 27 ? String.valueOf((char) (i + 96)) : null;
	}

	public static class Words {
		long num;

		private Words() {
			num = 0;
		}

		private Words(long num) {
			this.num = num;
		}

		public void setNumber(long num) {
			this.num = num;
		}

		public long getNumber() {
			return num;
		}

		public static Words getInstance(long num) {
			return new Words(num);
		}

		public static String leftChars(String str, int n) {
			if (str.length() <= n)
				return str;
			else
				return str.substring(0, n);
		}

		public static String rightChars(String str, int n) {
			if (str.length() <= n)
				return str;
			else
				return str.substring(str.length() - n, str.length());
		}

		@SuppressWarnings("removal")
		public long leftChars(int n) {
			return new Long(leftChars(new Long(num).toString(), n)).longValue();
		}

		@SuppressWarnings("removal")
		public long rightChars(int n) {
			return new Long(rightChars(new Long(num).toString(), n)).longValue();
		}

		@SuppressWarnings("removal")
		public long leftChars(long num, int n) {
			return new Long(leftChars(new Long(num).toString(), n)).longValue();
		}

		@SuppressWarnings("removal")
		public long rightChars(long num, int n) {
			return new Long(rightChars(new Long(num).toString(), n)).longValue();
		}

		@SuppressWarnings("removal")
		public int length(long num) {
			return new Long(num).toString().length();
		}

		private String belowTen(long x) {
			switch ((int) x) {
			case 1:
				return "One ";
			case 2:
				return "Two ";
			case 3:
				return "Three ";
			case 4:
				return "Four ";
			case 5:
				return "Five ";
			case 6:
				return "Six ";
			case 7:
				return "Seven ";
			case 8:
				return "Eight ";
			case 9:
				return "Nine ";
			}
			return "";
		}

		private String belowTwenty(long x) {
			if (x < 10)
				return belowTen(x);
			switch ((int) x) {
			case 10:
				return "Ten ";
			case 11:
				return "Eleven ";
			case 12:
				return "Twelve ";
			case 13:
				return "Thirteen ";
			case 14:
				return "Fourteen ";
			case 15:
				return "Fifteen ";
			case 16:
				return "Sixteen ";
			case 17:
				return "Seventeen ";
			case 18:
				return "Eighteen ";
			case 19:
				return "Nineteen ";
			}
			return "";
		}

		private String belowHundred(long x) {
			if (x < 10)
				return belowTen(x);
			else if (x < 20)
				return belowTwenty(x);
			switch ((int) leftChars(x, 1)) {
			case 2:
				return "Twenty " + belowTen(rightChars(x, 1));
			case 3:
				return "Thirty " + belowTen(rightChars(x, 1));
			case 4:
				return "Fourty " + belowTen(rightChars(x, 1));
			case 5:
				return "Fifty " + belowTen(rightChars(x, 1));
			case 6:
				return "Sixty " + belowTen(rightChars(x, 1));
			case 7:
				return "Seventy " + belowTen(rightChars(x, 1));
			case 8:
				return "Eighty " + belowTen(rightChars(x, 1));
			case 9:
				return "Ninety " + belowTen(rightChars(x, 1));
			}
			return "";
		}

		private String belowThousand(long x) {
			if (x < 10)
				return belowTen(x);
			else if (x < 20)
				return belowTwenty(x);
			else if (x < 100)
				return belowHundred(x);
			return belowTen(leftChars(x, 1)) + "Hundred " + belowHundred(rightChars(x, 2));
		}

		private String belowLakh(long x) {
			if (x < 10)
				return belowTen(x);
			else if (x < 20)
				return belowTwenty(x);
			else if (x < 100)
				return belowHundred(x);
			else if (x < 1000)
				return belowThousand(x);
			if (length(x) == 4)
				return belowTen(leftChars(x, 1)) + "Thousand " + belowThousand(rightChars(x, 3));
			else
				return belowHundred(leftChars(x, 2)) + "Thousand " + belowThousand(rightChars(x, 3));
		}

		public String belowCrore(long x) {
			if (x < 10)
				return belowTen(x);
			else if (x < 20)
				return belowTwenty(x);
			else if (x < 100)
				return belowHundred(x);
			else if (x < 1000)
				return belowThousand(x);
			else if (x < 100000)
				return belowLakh(x);
			if (length(x) == 6)
				return belowTen(leftChars(x, 1)) + "Lakhs " + belowLakh(rightChars(x, 5));
			else
				return belowHundred(leftChars(x, 2)) + "Lakhs " + belowLakh(rightChars(x, 5));
		}

		public String belowBilion(long x) {
			if (x < 10)
				return belowTen(x);
			else if (x < 20)
				return belowTwenty(x);
			else if (x < 100)
				return belowHundred(x);
			else if (x < 1000)
				return belowThousand(x);
			else if (x < 100000)
				return belowLakh(x);
			else if (x < 100000000)
				return belowCrore(x);

			if (length(x) == 8)
				return belowTen(leftChars(x, 1)) + "Bilion " + belowCrore(rightChars(x, 7));
			else
				return belowHundred(leftChars(x, 2)) + "Bilion " + belowCrore(rightChars(x, 7));
		}

		public String getNumberInWords() {
			if (num < 10)
				return belowTen(num);
			else if (num < 20)
				return belowTwenty(num);
			else if (num < 100)
				return belowHundred(num);
			else if (num < 1000)
				return belowThousand(num);
			else if (num < 100000)
				return belowLakh(num);
			else if (num < 10000000)
				return belowCrore(num);
			else if (num < 1000000000)
				return belowBilion(num);
			return "";
		}

		public static void main(String[] args) throws Exception {
			System.out.println("Enter one number:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			Words w = Words.getInstance(Integer.parseInt(br.readLine()));
			System.out.println(w.getNumberInWords());
		}
	}

	public static List<Date> monthsBetween(Date d1, Date d2) {
		List<Date> ret = new ArrayList<Date>();
		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		ret.add(c.getTime());
		while (c.getTimeInMillis() < d2.getTime()) {
			c.add(Calendar.MONTH, 1);
			if (c.getTimeInMillis() < d2.getTime()) {
				ret.add(c.getTime());
			}
		}
		return ret;
	}

	public static int stopThreads(String batchName) {
		Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();
		int noThreads = 0;
		// Iterate over set to find yours
		for (Thread thread : setOfThread) {
			if (thread.getName().startsWith(batchName)) {
				noThreads++;
				thread.interrupt();
			}
		}
		return noThreads;
	}

	public static boolean isAlphabet(String toBeChecked) {
		if (isEmpty(toBeChecked)) {
			return false;
		} else {
			String regex = "^[A-z]+$";
			Pattern pattern = Pattern.compile(regex);
			return pattern.matcher(toBeChecked).matches();
		}
	}

	public static boolean isAplpabetAndSpace(String toBeChecked) {
		if (isEmpty(toBeChecked)) {
			return false;
		} else {
			String regex = "^[ A-z]+$";
			Pattern pattern = Pattern.compile(regex);
			return pattern.matcher(toBeChecked).matches();
		}
	}

	public static boolean isAlphaNumeric(String toBeChecked) {
		if (isEmpty(toBeChecked)) {
			return false;
		} else {
			String regex = "^[A-Za-z0-9]+$";
			Pattern pattern = Pattern.compile(regex);
			return pattern.matcher(toBeChecked).matches();
		}
	}

	public static boolean isAlphaNumericAndSpace(String toBeChecked) {
		if (isEmpty(toBeChecked)) {
			return false;
		} else {
			String regex = "^[ A-Za-z0-9]+$";
			Pattern pattern = Pattern.compile(regex);
			return pattern.matcher(toBeChecked).matches();
		}
	}

	public static Date getFirstDayOfQuarter(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	public static boolean checkDouble(String str) {
		boolean flgValid = false;
		try {
			flgValid = true;
		} catch (Exception ex) {
			flgValid = false;
		}
		return flgValid;
	}

	@SuppressWarnings("deprecation")
	public static String convertInRequiredCase(String toBeConverted, String displayCase) {
		if (isNotEmpty(toBeConverted)) {
			if (displayCase.equalsIgnoreCase("Upper")) {
				return toBeConverted.toUpperCase();
			} else if (displayCase.equalsIgnoreCase("Proper")) {
				return WordUtils.capitalizeFully(toBeConverted);
			} else if (displayCase.equalsIgnoreCase("Uploaded Value")) {
				return toBeConverted;
			} else {
				return toBeConverted;
			}
		} else {
			return "";
		}
	}
	
	public static char[] generateOtp() {
		String numbers="0123456789";
		
		Random rndm_method = new Random(); 
		Integer len=6;
		  
        char[] otp = new char[len]; 
  
        for (int i = 0; i < len; i++) 
        { 
            otp[i] = 
             numbers.charAt(rndm_method.nextInt(numbers.length())); 
        } 
        return otp; 
	}
	
	
	
	public static boolean isValidEmail(String email) {
	      String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	      return email.matches(regex);
	   }

}
