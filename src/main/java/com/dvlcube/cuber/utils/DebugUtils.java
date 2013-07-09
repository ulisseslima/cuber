package com.dvlcube.cuber.utils;

/**
 * 
 * @since 06/07/2013
 * @author wonka
 */
public class DebugUtils {
	public static void sleeps(int seconds) {
		sleep(seconds * 1000);
	}

	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
