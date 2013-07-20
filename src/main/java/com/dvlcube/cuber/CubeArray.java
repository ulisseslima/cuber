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
