package com.rwu.misc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	private static SimpleDateFormat format = new SimpleDateFormat();

	private static SimpleDateFormat formatHourOnly = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat formatDateOnly2 = new SimpleDateFormat("yyyy-MM-dd");

	private static DateFormat formatDateOnly = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);

	/**
	 * Format date (without seconds)
	 */
	public static String formatDateTime(Date date) {
		return format.format(date);
	}

	public static String formatDateOnly(Date date) {
		return formatDateOnly.format(date);
	}

	public static String formatTimeOnly(Date date) {
		return formatHourOnly.format(date);
	}

	/**
	 * Output of hour with seconds
	 * 
	 * 10:02 44s
	 */
	public static String formatDateTimeWithSeconds(Date date) {
		return format.format(date) + " " + date.getSeconds() + "s";
	}

	public static Date withDaysPlus(Date date, int days) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, days);

		return calendar.getTime();
	}

	public static Date parseDateTime(String text) {
		try {
			return format.parse(text);
		} catch (ParseException e) {
			return null;
		}
	}
}
