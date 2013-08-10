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
package com.dvlcube.cuber.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author wonka
 * @since 06/04/2013
 */
public class ObjectUtils {
	/**
	 * Adapted from
	 * http://stackoverflow.com/questions/835416/how-to-copy-properties-from-one-java-bean-to-another
	 * 
	 * @param from
	 *            an object
	 * @param to
	 *            to another object
	 * @param ignoreNull
	 *            whether null values should be copied.
	 * @author wonka
	 * @since 06/04/2013
	 */
	public static void copyProperties(Object from, Object to, boolean ignoreNull) {
		try {
			Class<? extends Object> fromClass = from.getClass();
			Class<? extends Object> toClass = to.getClass();

			BeanInfo fromBean = Introspector.getBeanInfo(fromClass);
			BeanInfo toBean = Introspector.getBeanInfo(toClass);

			PropertyDescriptor[] fromDescriptors = fromBean.getPropertyDescriptors();
			HashMap<String, PropertyDescriptor> toDescriptors = map(toBean.getPropertyDescriptors());

			for (PropertyDescriptor fromDescriptor : fromDescriptors) {
				PropertyDescriptor toDescriptor = toDescriptors.get(fromDescriptor.getDisplayName());
				if (toDescriptor != null) {
					if (toDescriptor.getWriteMethod() != null) {
						Object value = get(fromDescriptor, from);
						if (value == null && ignoreNull) {
						} else {
							String packageName = value.getClass().getPackage().getName();
							if (packageName.startsWith("java")) {
								toDescriptor.getWriteMethod().invoke(to, get(fromDescriptor, from));
							} else {
								copyProperties(get(fromDescriptor, from), get(toDescriptor, to), ignoreNull);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param property
	 *            the property descriptor.
	 * @param from
	 *            the object to call the getter on.
	 * @return the result of calling the getter of the property represented by the property descriptor on
	 *         object "from".
	 * @author wonka
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @since 06/04/2013
	 */
	private static Object get(PropertyDescriptor property, Object from) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		return property.getReadMethod().invoke(from);
	}

	/**
	 * @param propertyDescriptors
	 *            the array of property descriptors.
	 * @return a hash map composed by a property name and a property descriptor. created from an array of
	 *         property descriptors.
	 * @author wonka
	 * @since 06/04/2013
	 */
	private static HashMap<String, PropertyDescriptor> map(PropertyDescriptor[] propertyDescriptors) {
		HashMap<String, PropertyDescriptor> map = new HashMap<>();
		for (PropertyDescriptor descriptor : propertyDescriptors) {
			String displayName = descriptor.getDisplayName();
			if (!"class".equals(displayName)) {
				map.put(displayName, descriptor);
			}
		}
		return map;
	}

	public static void safeCopyProperties(Object from, Object to, boolean ignoreNull) {
		try {
			Class<? extends Object> fromClass = from.getClass();
			Class<? extends Object> toClass = to.getClass();

			BeanInfo fromBean = Introspector.getBeanInfo(fromClass);
			BeanInfo toBean = Introspector.getBeanInfo(toClass);

			PropertyDescriptor[] toPd = toBean.getPropertyDescriptors();
			List<PropertyDescriptor> fromPd = Arrays.asList(fromBean.getPropertyDescriptors());

			for (PropertyDescriptor property : toPd) {
				int index = fromPd.indexOf(property);
				if (index < 0) {
					continue;
				}
				PropertyDescriptor descriptor = fromPd.get(index);
				String displayName = descriptor.getDisplayName();
				if (displayName.equals(property.getDisplayName()) && !displayName.equals("class")) {
					if (property.getWriteMethod() != null) {
						Object value = get(property, from);
						if (value == null && ignoreNull) {
						} else {
							String packageName = value.getClass().getPackage().getName();
							if (!packageName.startsWith("java")) {
								copyProperties(get(property, from), get(property, to), ignoreNull);
							} else {
								property.getWriteMethod().invoke(to, get(descriptor, from));
							}
						}
					}
				}

			}
		} catch (Exception e) {
			throw new RuntimeException("Could not update properties", e);
		}
	}

	/**
	 * @param from
	 *            an object
	 * @param to
	 *            another object
	 * @author wonka
	 * @since 06/04/2013
	 */
	public static void updateProperties(Object from, Object to) {
		copyProperties(from, to, true);
	}

	/**
	 * @param o
	 *            object to compare.
	 * @param objects
	 *            objects available.
	 * @return true if the object is equal to any of the other objects.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public static boolean in(Object o, Object... objects) {
		for (Object object : objects) {
			if (o.equals(object))
				return true;
		}
		return false;
	}
}
