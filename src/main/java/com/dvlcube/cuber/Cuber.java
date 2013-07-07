package com.dvlcube.cuber;

import java.awt.image.BufferedImage;

/**
 * 
 * @author wonka
 * @since 14/04/2013
 */
public class Cuber {
	public static <T> CubeClass<T> $(Class<T> c) {
		return new CubeClass<T>(c);
	}

	public static Cube $(Object object) {
		return new Cube(object);
	}

	public static CubeString $(String string) {
		return new CubeString(string);
	}

	public static <T> CubeArray<T> $(T[] array) {
		return new CubeArray<T>(array);
	}

	public static CubeImage $(BufferedImage image) {
		return new CubeImage(image);
	}

	public static CubeFile $f(String path) {
		return new CubeFile(path);
	}

	public static CubeFile $f(CubeString path) {
		return new CubeFile(path);
	}

	public static CubeImage $img(String path) {
		return new CubeImage(path);
	}

	public static CubeImage $img(int width, int height) {
		return new CubeImage(width, height);
	}

	public static CubeImage $img(Class<?> origin, String path) {
		return new CubeImage(origin, path);
	}

	public static CubeNumber $(double n) {
		return new CubeNumber(n);
	}

	public static CubeNumber $(double n, Range<Double> range) {
		return new CubeNumber(n, range);
	}

	public static CubeNumber $(double n, double range0, double range1) {
		return new CubeNumber(n, range0, range1);
	}

	public static CubeDate $date(String pattern) {
		return new CubeDate(pattern);
	}

	public static CubeDate $date() {
		return new CubeDate();
	}

	public static CubeSequence $seq() {
		return new CubeSequence();
	}

	public static CubeSequence $seq(String sequence) {
		return new CubeSequence(sequence);
	}

	public static CubeSequence $seq(int instrument, int tempo) {
		return new CubeSequence(instrument, tempo);
	}

	public static CubeSequence $seq(int length) {
		return new CubeSequence(length);
	}

	public static CubeInput $input(String message) {
		return new CubeInput(message);
	}

	public static CubeDebug $debug() {
		return new CubeDebug();
	}
}
