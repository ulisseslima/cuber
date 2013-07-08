package com.dvlcube.cuber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @since 07/07/2013
 * @author wonka
 */
public class FileUtils {
	/**
	 * @param file
	 *            input.
	 * @return the first line of a file.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public static String firstLine(File file) {
		return head(file, 1).get(0);
	}

	/**
	 * @param file
	 *            input.
	 * @param nLines
	 *            desired number of lines.
	 * @return the first n lines of text.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public static List<String> head(File file, int nLines) {
		if (file == null)
			throw new IllegalStateException("file not initialized");

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			int i = 0;
			ArrayList<String> lines = new ArrayList<>();
			String line = null;
			while ((line = reader.readLine()) != null && i < nLines) {
				lines.add(line);
				i++;
			}
			return lines;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
