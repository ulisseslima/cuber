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
	public T[] array;

	/**
	 * @param array
	 * @author wonka
	 * @since 15/04/2013
	 */
	public CubeArray(T[] array) {
		this.array = array;
	}

	/**
	 * @return
	 * @author wonka
	 * @since 15/04/2013
	 */
	public Set<T> asSet() {
		return ArrayUtils.asSet(array);
	}

	@SafeVarargs
	public final T[] concat(T[]... rest) {
		return ArrayUtils.concat(array, rest);
	}

	public Set<T> concatIntoSet(Collection<T> collection) {
		return ArrayUtils.concatIntoSet(array, collection);
	}

	@Override
	public String toString() {
		return Arrays.toString(array);
	}
}
