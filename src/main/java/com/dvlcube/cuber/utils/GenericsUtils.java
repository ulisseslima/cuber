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

import java.util.List;

/**
 * 
 * @author wonka
 * @since 19/03/2013
 */
@SuppressWarnings("unchecked")
public class GenericsUtils {
	public static <T> T unchecked(Object object) {
		return (T) object;
	}

	public static <T> List<T> uncheckedList(Object object) {
		return (List<T>) object;
	}
}
