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
