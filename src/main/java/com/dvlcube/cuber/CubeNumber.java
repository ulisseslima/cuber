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
		o = n;
	}

	public CubeNumber(double n, double range0, double range1) {
		o = n;
		range = new Range<>(range0, range1);
	}

	public CubeNumber(double n, Range<Double> range) {
		o = n;
		this.range = range;
	}

	public float f() {
		return o.floatValue();
	}

	public int i() {
		return o.intValue();
	}

	public long l() {
		return o.longValue();
	}

	public CubeNumber limit(double l1, double l2) {
		if (o < l1) {
			o = l1;
		}

		if (o > l2) {
			o = l2;
		}

		return this;
	}

	public CubeNumber map(double range0, double range1) {
		map(range, new Range<>(range0, range1));
		return this;
	}

	public CubeNumber map(Range<Double> newRange) {
		map(range, newRange);
		return this;
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

	@Override
	public String toString() {
		return o.toString();
	}
}
