package com.dvlcube.cuber;

import static com.dvlcube.cuber.Cuber.$;

import java.io.File;

import com.dvlcube.cuber.utils.DebugUtils;

/**
 * 
 * @since 06/07/2013
 * @author wonka
 */
public class CubeDebug {
	private static final boolean ENABLED;

	static {
		if (new File($("${user.dir}/.debug-enabled").o).exists()) {
			ENABLED = true;
		} else {
			ENABLED = false;
		}
	}

	public void sleep(int millis) {
		DebugUtils.sleep(millis);
	}

	public void sleeps(int seconds) {
		DebugUtils.sleeps(seconds);
	}

	public static void log(String msg, Object... args) {
		if (ENABLED) {
			System.out.printf(msg + "\n", args);
		}
	}

	public static void logIf(Boolean condition, String msg, Object... args) {
		if (condition != null && condition) {
			log(msg, args);
		}
	}

	/**
	 * @param string
	 * @param query
	 * @author wonka
	 * @since 01/04/2013
	 */
	public static void logObj(String msg, Object obj) {
		log(msg + "%s", $(obj).stringify());
	}

	@Override
	public String toString() {
		return String.valueOf(ENABLED);
	}
}
