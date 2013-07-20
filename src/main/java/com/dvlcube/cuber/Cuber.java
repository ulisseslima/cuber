package com.dvlcube.cuber;

import java.awt.image.BufferedImage;

/**
 * 
 * @author wonka
 * @since 14/04/2013
 */
public class Cuber {
	public static CubeImage $(BufferedImage image) {
		return new CubeImage(image);
	}

	public static CubeString $(char... string) {
		if (string != null && string.length > 0) {
			return new CubeString(new String(string));
		} else {
			return new CubeString();
		}
	}

	public static CubeString $(char[] string, int... indexes) {
		return new CubeString(string, indexes);
	}

	public static <T> CubeClass<T> $(Class<T> c) {
		return new CubeClass<T>(c);
	}

	public static CubeNumber $(double n) {
		return new CubeNumber(n);
	}

	public static CubeNumber $(double n, double range0, double range1) {
		return new CubeNumber(n, range0, range1);
	}

	public static CubeNumber $(double n, Range<Double> range) {
		return new CubeNumber(n, range);
	}

	public static CubeNumber $(int n, Range<Double> range) {
		return new CubeNumber(n, range);
	}

	public static Cube $(Object object, String... objects) {
		return new Cube(object, objects);
	}

	public static CubeString $(String string) {
		return new CubeString(string);
	}

	public static CubeString $(String string, int times) {
		return new CubeString(string, times);
	}

	public static <T> CubeArray<T> $(T[] array) {
		return new CubeArray<T>(array);
	}

	@SafeVarargs
	public static <T> CubeArray<T> $(T[] array, T[]... arrays) {
		return new CubeArray<T>(array, arrays);
	}

	public static CubeDate $date() {
		return new CubeDate();
	}

	public static CubeDate $date(String pattern) {
		return new CubeDate(pattern);
	}

	public static CubeDebug $debug() {
		return new CubeDebug();
	}

	public static CubeFile $f(CubeString path) {
		return new CubeFile(path);
	}

	public static CubeFile $f(String path) {
		return new CubeFile(path);
	}

	public static CubeImage $img(Class<?> origin, String path) {
		return new CubeImage(origin, path);
	}

	public static CubeImage $img(int width, int height) {
		return new CubeImage(width, height);
	}

	public static CubeImage $img(String path) {
		return new CubeImage(path);
	}

	public static CubeInput $in(String message) {
		return new CubeInput(message);
	}

	public static CubeSequence $midi(CubeFile file) {
		if (file.is(CubeSequence.FILE_EXTENSION)) {
			return CubeSequence.valueOf(file);
		}

		return new CubeSequence(file);
	}

	public static CubeSequence $midi(int length) {
		return new CubeSequence(length);
	}

	public static CubeSequence $midi(int instrument, int tempo) {
		return new CubeSequence(instrument, tempo);
	}

	public static CubeSequence $midi(int instrument, int tempo, String... sequences) {
		return new CubeSequence(instrument, tempo, sequences);
	}

	public static CubeSequence $midi(String... sequences) {
		if (sequences != null) {
			if (sequences.length == 1) {
				if (sequences[0].contains(CubeSequence.ATTRIBUTE_SEPARATOR)) {
					return CubeSequence.valueOf(sequences[0]);
				}
			}
		}
		return new CubeSequence(sequences);
	}

	public static void $out(Object object) {
		System.out.println(object);
	}

	public static void $printf(String message, Object... objects) {
		System.out.printf(message, objects);
	}
}
