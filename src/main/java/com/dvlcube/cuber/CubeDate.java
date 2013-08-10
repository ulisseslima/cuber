/*
  This file is part of Cuber.

    Cuber is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Cuber is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Cuber.  If not, see <http://www.gnu.org/licenses/>.
 */
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
	public String pattern = "yyyy-MM-dd_HH-mm-ss.SSS";
	private final SimpleDateFormat patternFormat = new SimpleDateFormat(pattern);
	public Calendar o;

	public CubeDate() {
		o = Calendar.getInstance();
	}

	public CubeDate(String pattern) {
		this();
		this.pattern = pattern;
		patternFormat.applyPattern(pattern);
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
		patternFormat.applyPattern(pattern);
		return this;
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
		return patternFormat.format(o.getTime());
	}

	/**
	 * @return the year.
	 * @since 09/07/2013
	 * @author wonka
	 */
	public int year() {
		return o.get(Calendar.YEAR);
	}
}
