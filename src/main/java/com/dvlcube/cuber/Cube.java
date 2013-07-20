package com.dvlcube.cuber;

import com.dvlcube.cuber.utils.ObjectUtils;
import com.dvlcube.cuber.utils.StringUtils;

/**
 * 
 * @author wonka
 * @since 14/04/2013
 */
public class Cube {
	public String[] arr;
	public Object o;

	/**
	 * @param object2
	 * @author wonka
	 * @param objects
	 * @since 14/04/2013
	 */
	public Cube(Object object, String... objects) {
		o = object;
		arr = objects;
	}

	/**
	 * @param objects
	 *            objects to compare.
	 * @return true if this object is equal to any of the other objects.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public boolean in(Object... objects) {
		return ObjectUtils.in(o, objects);
	}

	/**
	 * @return a string representing the junction between the objects, separated by commas.
	 * @author wonka
	 * @since 20/07/2013
	 */
	public String join() {
		return join(",");
	}

	/**
	 * @param separator
	 * @return a string representing the junction between the objects, using the provided separator.
	 * @author wonka
	 * @since 20/07/2013
	 */
	public String join(String separator) {
		StringBuilder builder = new StringBuilder(separator);
		builder.append(o.toString());
		for (String string : arr) {
			builder.append(separator).append(string);
		}
		return builder.toString().replaceFirst(separator, "");
	}

	/**
	 * Merges this object's attributes with another Object, ignoring null values.
	 * 
	 * @param on
	 *            This object will have its properties updated, according to this object's values.
	 * @author wonka
	 * @since 14/04/2013
	 */
	public void merge(Object on) {
		if (on instanceof Cube) {
			ObjectUtils.updateProperties(o, ((Cube) on).o);
		} else {
			ObjectUtils.updateProperties(o, on);
		}
	}

	/**
	 * @return
	 * @author wonka
	 * @since 14/04/2013
	 */
	public String stringify() {
		return StringUtils.stringify(o);
	}

	@Override
	public String toString() {
		return o.toString();
	}
}
