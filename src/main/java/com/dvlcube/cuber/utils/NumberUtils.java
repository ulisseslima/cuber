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
