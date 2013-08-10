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

/**
 * 
 * @author wonka
 * @since 16/09/2012
 */
public class Range<T> {
	/**
	 * Shortcut.
	 * 
	 * @param start
	 * @param end
	 * @return range from start to end;
	 * @author wonka
	 * @since 13/07/2013
	 */
	public static <T> Range<T> $range(final T start, final T end) {
		return new Range<T>(start, end);
	}

	private final T end;

	private final T start;

	/**
	 * @param start
	 * @param end
	 * @author wonka
	 * @since 16/09/2012
	 */
	public Range(final T start, final T end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * @return the end
	 */
	public T getEnd() {
		return end;
	}

	/**
	 * @return the start
	 */
	public T getStart() {
		return start;
	}
}
