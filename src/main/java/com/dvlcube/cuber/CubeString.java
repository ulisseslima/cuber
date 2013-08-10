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
