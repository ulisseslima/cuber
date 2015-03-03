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
