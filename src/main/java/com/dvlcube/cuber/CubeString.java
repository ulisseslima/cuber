package com.dvlcube.cuber;

import com.dvlcube.cuber.utils.StringUtils;
import com.dvlcube.cuber.utils.StringUtils.Expand;

/**
 * 
 * @author wonka
 * @since 28/02/2013
 */
public class CubeString {
	public String o = "";

	public CubeString() {

	}

	/**
	 * @param string
	 * @param indexes
	 * @author wonka
	 * @since 13/07/2013
	 */
	public CubeString(char[] string, int... indexes) {
		if (string != null) {
			StringBuilder builder = new StringBuilder();
			for (int i : indexes) {
				if (i < string.length) {
					builder.append(string[i]);
				}
			}
			reset(builder.toString());
		} else {
			reset("");
		}
	}

	/**
	 * @param string
	 * @author wonka
	 * @since 28/02/2013
	 */
	public CubeString(final String string) {
		reset(string);
	}

	/**
	 * @param string
	 *            string.
	 * @param times
	 *            times to repeat.
	 * @author wonka
	 * @since 13/07/2013
	 */
	public CubeString(String string, int times) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < times; i++) {
			builder.append(string);
		}
		reset(builder.toString());
	}

	public CubeString escapeHTML() {
		return new CubeString(StringUtils.escapeHTML(o));
	}

	public Integer i() {
		try {
			return Integer.parseInt(o);
		} catch (Exception e) {
			return 0;
		}
	}

	public boolean isBlank() {
		return StringUtils.isBlank(o);
	}

	public boolean isNotBlank() {
		return !isBlank();
	}

	public final void reset(String string) {
		o = Expand.that(string);
	}

	public CubeString scramble() {
		o = StringUtils.scramble(o);
		return this;
	}

	@Override
	public String toString() {
		return o;
	}
}
