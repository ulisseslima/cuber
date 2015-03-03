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
