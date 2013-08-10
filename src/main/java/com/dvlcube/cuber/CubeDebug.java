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

	public void sleep(int millis) {
		DebugUtils.sleep(millis);
	}

	public void sleeps(int seconds) {
		DebugUtils.sleeps(seconds);
	}

	@Override
	public String toString() {
		return String.valueOf(ENABLED);
	}
}
