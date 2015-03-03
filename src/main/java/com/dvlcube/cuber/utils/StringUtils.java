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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.dvlcube.cuber.CubeDate;
import com.dvlcube.cuber.factory.NameFactory;

/**
 * @author wonka
 * @since 28/02/2013
 */
public class StringUtils {
	public enum Expand {
		DATE("${date}") {
			@Override
			public String doExpansion(String string) {
				return string.replace(var, new CubeDate("yyyy-MM-dd").toString());
			}
		},
		DATETIME("${datetime}") {
			@Override
			public String doExpansion(String string) {
				return string.replace(var, new CubeDate().toString());
			}
		},
		USER("${user.name}") {
			@Override
			public String doExpansion(String string) {
				return string.replace(var, System.getProperty("user.name"));
			}
		},
		USER_DIR("${user.dir}") {
			@Override
			public String doExpansion(String string) {
				return string.replace(var, System.getProperty("user.dir"));
			}
		},
		NAME("${name}") {
			@Override
			public String doExpansion(String string) {
				return string.replace(var, StringUtils.randomName());
			}
		},
		THREAD("${thread}") {
			@Override
			public String doExpansion(String string) {
				return string.replace(var, Thread.currentThread().getName());
			}
		},
		TID("${tid}") {
			@Override
			public String doExpansion(String string) {
				String tid = String.valueOf(Thread.currentThread().getId());
				return string.replace(var, tid);
			}
		};
		protected final String var;

		Expand(String var) {
			this.var = var;
		}

		public String doExpansion(String string) {
			throw new UnsupportedOperationException("no-op");
		}

		/**
		 * @param string
		 *            string to be expanded.
		 * @return the resulting string.
		 * @since 04/07/2013
		 * @author wonka
		 */
		public static String that(String string) {
			for (Expand expansion : values()) {
				if (string.contains(expansion.var)) {
					string = expansion.doExpansion(string);
				}
			}
			return string;
		}
	}

	/**
	 * @param string
	 *            the string to escape.
	 * @return a string formatted for use in web pages.
	 * @author wonka
	 * @since 15/03/2013
	 */
	public static String escapeHTML(final String string) {
		return string.replace("\"", "&quot;");
	}

	/**
	 * @param list
	 *            the list to calculate.
	 * @return a string representation of a list's size. Returning 0 for null.
	 * @author wonka
	 * @since 29/03/2013
	 */
	public static String getSize(List<?> list) {
		return (String) (list != null ? list.size() : "0");
	}

	/**
	 * @param string
	 *            the string to check.
	 * @return whether a string contains no printable characters.
	 * @author wonka
	 * @since 25/04/2013
	 */
	public static boolean isBlank(final String string) {
		if (string == null) {
			return true;
		}
		return string.trim().isEmpty();
	}

	/**
	 * @param array
	 *            the array to randomize.
	 * @return an array that is a result of mixing the index values of the first array.
	 * @author wonka
	 * @since 23/04/2013
	 */
	private static char[] randomizeIndex(char[] array) {
		char[] randomArray = new char[array.length];
		List<Integer> set = new ArrayList<>();
		while (set.size() < array.length) {
			int index = (int) (Math.random() * array.length);
			if (!set.contains(index)) {
				set.add(index);
			}
		}
		Queue<Integer> queue = new LinkedList<>(set);
		int i = 0;
		while (!queue.isEmpty()) {
			randomArray[i++] = array[queue.poll()];
		}
		return randomArray;
	}

	/**
	 * Will alter the order of the letters in the String representation of an Object.
	 * 
	 * @param object
	 *            the object.
	 * @return a scrambled string representation of the object.
	 * @author wonka
	 * @since 23/04/2013
	 */
	public static String scramble(Object object) {
		String string = object.toString();
		char[] array = string.toCharArray();
		char[] randomArray = randomizeIndex(array);
		return new String(randomArray);
	}

	/**
	 * @param object
	 *            object to stringify.
	 * @return A stringified representation of the Object.
	 * @author wonka
	 * @since 29/03/2013
	 */
	public static String stringify(Object object) {
		try {
			StringBuilder builder = new StringBuilder();
			Field[] fields = object.getClass().getDeclaredFields();
			builder.append(object.getClass().getSimpleName() + " {");
			for (Field field : fields) {
				field.setAccessible(true);
				builder.append("\n\t" + field.getName() + ": ");
				try {
					if (field.getType().isArray()) {
						builder.append(Arrays.toString((Object[]) field.get(object)));
					} else {
						builder.append(field.get(object));
					}
				} catch (Exception e) {
					builder.append("n/a");
				}
			}
			builder.append("\n}");
			return builder.toString();
		} catch (Exception e) {
			return "Couldn't stringify: " + e.getMessage();
		}
	}

	/**
	 * Takes a string and replaces known variables:
	 * <p>
	 * ${date} - today's date in the format yyyy-MM-dd<br>
	 * ${datetime} - today's date and time in the format yyyy-MM-dd_HH-mm-ss.SSS<br>
	 * ${thread} - current thread name<br>
	 * ${tid} - current thread id<br>
	 * 
	 * @param string
	 * @return the resulting string.
	 * @since 04/07/2013
	 * @author wonka
	 */
	public static String expand(String string) {
		return Expand.that(string);
	}

	/**
	 * @return a random name.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public static String randomName() {
		return randomName(1);
	}

	/**
	 * @param n
	 *            how many names?
	 * @return random names.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public static String randomName(int n) {
		return new NameFactory(n).getName();
	}
}
