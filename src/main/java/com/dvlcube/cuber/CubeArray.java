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

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import com.dvlcube.cuber.utils.ArrayUtils;

/**
 * 
 * @author wonka
 * @since 15/04/2013
 */
public class CubeArray<T> {
	public T[] o;

	/**
	 * @param array
	 * @author wonka
	 * @since 15/04/2013
	 */
	public CubeArray(T[] array) {
		this.o = array;
	}

	@SafeVarargs
	public CubeArray(T[] array, T[]... arrays) {
		this.o = array;
		concat(arrays);
	}

	/**
	 * @return
	 * @author wonka
	 * @since 15/04/2013
	 */
	public Set<T> asSet() {
		return ArrayUtils.asSet(o);
	}

	@SafeVarargs
	public final CubeArray<T> concat(T[]... rest) {
		o = ArrayUtils.concat(o, rest);
		return this;
	}

	public Set<T> concatIntoSet(Collection<T> collection) {
		return ArrayUtils.concatIntoSet(o, collection);
	}

	@Override
	public String toString() {
		return Arrays.toString(o);
	}
}
