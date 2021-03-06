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

import com.dvlcube.cuber.Range;

/**
 * 
 * @since 04/07/2013
 * @author wonka
 */
public class NumberUtils {
	/**
	 * http://stackoverflow.com/questions/345187/math-mapping-numbers
	 * <p>
	 * Linear Transform
	 * <p>
	 * Y = (X-A)/(B-A) * (D-C) + C
	 * 
	 * @param originalRange
	 * @param newRange
	 * @return (X-A)/(B-A) * (D-C) + C
	 * @since 04/07/2013
	 * @author wonka
	 */
	public static double map(double n, Range<Double> originalRange, Range<Double> newRange) {
		double newNumber = (n - originalRange.getStart()) / (originalRange.getEnd() - originalRange.getStart())
				* (newRange.getEnd() - newRange.getStart()) + newRange.getStart();
		return newNumber;
	}
}
