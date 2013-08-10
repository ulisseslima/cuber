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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
 * @author wonka
 * @since 16/09/2012
 */
public class ReflectionUtils {

	/**
	 * @return
	 * @author wonka
	 * @since 16/09/2012
	 */
	public static <T> Type[] getGenericTypes(final Class<T> jClass) {
		final ParameterizedType genericSuperclass = (ParameterizedType) jClass.getGenericSuperclass();
		return genericSuperclass.getActualTypeArguments();
	}
}
