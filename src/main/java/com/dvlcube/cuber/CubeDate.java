package com.dvlcube.cuber;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.dvlcube.cuber.utils.DateUtils;

/**
 * 
 * @author wonka
 * @since 28/02/2013
 */
public class CubeDate {
	public Calendar o;
	public String pattern = "yyyy-MM-dd_HH-mm-ss.SSS";
	private final SimpleDateFormat _ = new SimpleDateFormat(pattern);

	public CubeDate() {
		o = Calendar.getInstance();
	}

	public CubeDate(String pattern) {
		this();
		this.pattern = pattern;
		_.applyPattern(pattern);
	}

	public CubeDate newDateInRange(Calendar cStart, Calendar cEnd) {
		o = DateUtils.newDateInRange(cStart, cEnd);
		return this;
	}

	public CubeDate newDateInRange(Date dateStart, Date dateEnd) {
		o = DateUtils.newDateInRange(dateStart, dateEnd);
		return this;
	}

	public CubeDate newDateInRange(long start, long end) {
		o = DateUtils.newDateInRange(start, end);
		return this;
	}

	public CubeDate newDateInRange(Range<Calendar> range) {
		o = DateUtils.newDateInRange(range);
		return this;
	}

	public CubeDate setPattern(String pattern) {
		this.pattern = pattern;
		_.applyPattern(pattern);
		return this;
	}

	/**
	 * @return the year.
	 * @since 09/07/2013
	 * @author wonka
	 */
	public int year() {
		return o.get(Calendar.YEAR);
	}

	/**
	 * @return year as tring.
	 * @since 09/07/2013
	 * @author wonka
	 */
	public String sYear() {
		return String.valueOf(o.get(Calendar.YEAR));
	}

	@Override
	public String toString() {
		return _.format(o.getTime());
	}
}
