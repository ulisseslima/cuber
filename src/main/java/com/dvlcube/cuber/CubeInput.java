package com.dvlcube.cuber;

import static com.dvlcube.cuber.Cuber.$;

import java.util.Scanner;

/**
 * @since 06/07/2013
 * @author wonka
 */
public class CubeInput {
	public static Scanner o = new Scanner(System.in);
	public String string;

	/**
	 * @param message
	 * @since 06/07/2013
	 * @author wonka
	 */
	public CubeInput(String message) {
		System.out.println($(message + ": "));
	}

	public String next() {
		return o.next();
	}

	/**
	 * @param message
	 * @return a number from the user.
	 */
	public int nextInt() {
		return o.nextInt();
	}
}
