package com.dvlcube.cuber;

import com.dvlcube.cuber.utils.NumberUtils;

/**
 * 
 * @author wonka
 * @since 28/02/2013
 */
public class CubeNumber {
	public Double o;
	public Range<Double> range = new Range<>(Double.MIN_VALUE, Double.MAX_VALUE);

	public CubeNumber(double n) {
		this.o = n;
	}

	public CubeNumber(double n, Range<Double> range) {
		this.o = n;
		this.range = range;
	}

	public CubeNumber(double n, double range0, double range1) {
		this.o = n;
		this.range = new Range<>(range0, range1);
	}

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
	public CubeNumber map(Range<Double> originalRange, Range<Double> newRange) {
		o = NumberUtils.map(o, originalRange, newRange);
		return this;
	}

	public CubeNumber map(Range<Double> newRange) {
		map(range, newRange);
		return this;
	}

	public CubeNumber map(double range0, double range1) {
		map(range, new Range<>(range0, range1));
		return this;
	}

	public CubeNumber limit(double l1, double l2) {
		if (o < l1)
			o = l1;

		if (o > l2)
			o = l2;

		return this;
	}

	public int i() {
		return o.intValue();
	}

	public long l() {
		return o.longValue();
	}

	public float f() {
		return o.floatValue();
	}

	@Override
	public String toString() {
		return o.toString();
	}
}
