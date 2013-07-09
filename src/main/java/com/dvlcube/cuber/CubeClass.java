package com.dvlcube.cuber;

import com.dvlcube.cuber.utils.ClassUtils;

/**
 * 
 * @author wonka
 * @since 18/04/2013
 */
public class CubeClass<C> {
	public Class<C> o;

	/**
	 * @param c
	 * @author wonka
	 * @since 18/04/2013
	 */
	public CubeClass(Class<C> c) {
		this.o = c;
	}

	/**
	 * @param interfaceClass
	 * @return
	 * @author wonka
	 * @since 18/04/2013
	 */
	public boolean doesImplement(Class<?> interfaceClass) {
		return ClassUtils.doesImplement(o, interfaceClass);
	}

	/**
	 * @param interfaceClasses
	 * @return
	 * @author wonka
	 * @since 18/04/2013
	 */
	public boolean doesImplementAll(Class<?>... interfaceClasses) {
		return ClassUtils.doesImplementAll(o, interfaceClasses);
	}

	@Override
	public String toString() {
		return o.toString();
	}
}
