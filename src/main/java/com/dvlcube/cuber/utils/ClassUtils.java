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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author wonka
 * @since 05/04/2013
 */
public class ClassUtils {
	/**
	 * @param aClass
	 * @param interfaceClass
	 * @return true if class1 implements interfaceClass.
	 * @author wonka
	 * @since 05/04/2013
	 */
	public static boolean doesImplement(Class<?> aClass, Class<?> interfaceClass) {
		Class<?>[] interfaces = aClass.getInterfaces();
		for (Class<?> anInterface : interfaces) {
			if (anInterface == interfaceClass) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param class1
	 * @param interfaceClasses
	 * @return true if class1 implements all interfaces in the list.
	 * @author wonka
	 * @since 05/04/2013
	 */
	public static boolean doesImplementAll(Class<?> class1, Class<?>... interfaceClasses) {
		Set<Class<?>> set = new HashSet<>(Arrays.asList(interfaceClasses));
		for (Class<?> class2 : interfaceClasses) {
			if (doesImplement(class1, class2)) {
				set.remove(class2);
			}
		}
		return set.isEmpty();
	}
}
