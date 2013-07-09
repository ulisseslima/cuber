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

	/**
	 * @param string
	 * @author wonka
	 * @since 28/02/2013
	 */
	public CubeString(final String string) {
		reset(string);
	}

	public CubeString() {

	}

	public CubeString escapeHTML() {
		return new CubeString(StringUtils.escapeHTML(o));
	}

	public boolean isBlank() {
		return StringUtils.isBlank(o);
	}

	public boolean isNotBlank() {
		return !isBlank();
	}

	public CubeString scramble() {
		o = StringUtils.scramble(o);
		return this;
	}

	public final void reset(String string) {
		o = Expand.that(string);
	}

	@Override
	public String toString() {
		return o;
	}
}
