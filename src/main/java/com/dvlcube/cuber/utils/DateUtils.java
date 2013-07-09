package com.dvlcube.cuber.utils;

import java.util.Calendar;
import java.util.Date;

import com.dvlcube.cuber.Range;

/**
 * 
 * @author wonka
 * @since 28/02/2013
 */
public class DateUtils {
	public static Calendar newDateInRange(Calendar cStart, Calendar cEnd) {
		long start = cStart.getTime().getTime();
		long end = cEnd.getTime().getTime();
		return newDateInRange(start, end);
	}

	public static Calendar newDateInRange(Date dataStart, Date dataEnd) {
		Calendar start = Calendar.getInstance();
		start.setTime(dataStart);

		Calendar end = Calendar.getInstance();
		end.setTime(dataEnd);

		return newDateInRange(start, end);
	}

	public static Calendar newDateInRange(long start, long end) {
		long random = start + (long) (Math.random() * (end - start + 1));
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(random));
		return cal;
	}

	public static Calendar newDateInRange(Range<Calendar> range) {
		Calendar cStart = Calendar.getInstance();
		cStart.set(Calendar.HOUR_OF_DAY, range.getStart().get(Calendar.HOUR_OF_DAY));
		cStart.set(Calendar.MINUTE, range.getStart().get(Calendar.MINUTE));
		long start = cStart.getTime().getTime();

		Calendar cEnd = Calendar.getInstance();
		cEnd.set(Calendar.HOUR_OF_DAY, range.getEnd().get(Calendar.HOUR_OF_DAY));
		cEnd.set(Calendar.MINUTE, range.getEnd().get(Calendar.HOUR_OF_DAY));
		long end = cEnd.getTime().getTime();

		return newDateInRange(start, end);
	}
}
