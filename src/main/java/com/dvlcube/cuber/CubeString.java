package com.dvlcube.cuber;

/**
 * 
 * @author wonka
 * @since 28/02/2013
 */
public class CubeString {
	public String o;

	/**
	 * @param string
	 * @author wonka
	 * @since 28/02/2013
	 */
	public CubeString(final String string) {
		o = string;
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

	@Override
	public String toString() {
		return o;
	}
}
