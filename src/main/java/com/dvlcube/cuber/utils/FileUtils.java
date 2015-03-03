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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * 
 * @since 07/07/2013
 * @author wonka
 */
public class FileUtils {
	public static final char EXTENSION_SEPARATOR = '.';
	public static final char UNIX_SEPARATOR = '/';
	public static final char WINDOWS_SEPARATOR = '\\';

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

	/**
	 * http://grepcode.com/file/repo1.maven.org/maven2/commons-io/commons-io/1.2/org/apache/commons/io/
	 * <p>
	 * Removes the extension from a filename. This method returns the textual part of the filename before the
	 * last dot. There must be no directory separator after the dot.
	 * <p>
	 * foo.txt --> foo <br>
	 * a\b\c.jpg --> a\b\c <br>
	 * a\b\c --> a\b\c <br>
	 * a.b\c --> a.b\c
	 * 
	 * The output will be the same irrespective of the machine that the code is running on.
	 * 
	 * @param filename
	 *            the filename to query, null returns null.
	 * @return the filename minus the extension
	 * @since 09/07/2013
	 * @author wonka
	 */
	public static String removeExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfExtension(filename);
		if (index == -1) {
			return filename;
		} else {
			return filename.substring(0, index);
		}
	}

	public static int indexOfExtension(String filename) {
		if (filename == null) {
			return -1;
		}
		int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
		int lastSeparator = indexOfLastSeparator(filename);
		return (lastSeparator > extensionPos ? -1 : extensionPos);
	}

	public static int indexOfLastSeparator(String filename) {
		if (filename == null) {
			return -1;
		}
		int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
		int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
		return Math.max(lastUnixPos, lastWindowsPos);
	}

	/**
	 * Copies a file to a directory preserving the file date. This method copies the contents of the specified
	 * source file to a file of the same name in the specified destination directory. The destination
	 * directory is created if it does not exist. If the destination file exists, then this method will
	 * overwrite it.
	 * 
	 * @param srcFile
	 *            an existing file to copy, must not be null
	 * @param destDir
	 *            the directory to place the copy in, must not be null
	 * @throws NullPointerException
	 *             if source or destination is null
	 * @throws IOException
	 *             if source or destination is invalid
	 * @throws IOException
	 *             if an IO error occurs during copying
	 * @since 09/07/2013
	 * @author wonka
	 */
	public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (destDir.exists() && destDir.isDirectory() == false) {
			throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
		}
		copyFile(srcFile, new File(destDir, srcFile.getName()), true);
	}

	public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (srcFile.exists() == false) {
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' exists but is a directory");
		}
		if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
			throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
		}
		if (destFile.getParentFile() != null && destFile.getParentFile().exists() == false) {
			if (destFile.getParentFile().mkdirs() == false) {
				throw new IOException("Destination '" + destFile + "' directory cannot be created");
			}
		}
		if (destFile.exists() && destFile.canWrite() == false) {
			throw new IOException("Destination '" + destFile + "' exists but is read-only");
		}
		doCopyFile(srcFile, destFile, preserveFileDate);
	}

	private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
		if (destFile.exists() && destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile + "' exists but is a directory");
		}

		FileInputStream input = new FileInputStream(srcFile);
		try {
			FileOutputStream output = new FileOutputStream(destFile);
			try {
				IOUtils.copy(input, output);
			} finally {
				IOUtils.closeQuietly(output);
			}
		} finally {
			IOUtils.closeQuietly(input);
		}

		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
		}
		if (preserveFileDate) {
			destFile.setLastModified(srcFile.lastModified());
		}
	}

	/**
	 * @param file
	 * @param dir
	 * @return the new file.
	 * @since 09/07/2013
	 * @author wonka
	 */
	public static File moveFileToDirectory(File file, File dir) {
		try {
			String fileName = file.getName();
			copyFileToDirectory(file, dir);
			file.delete();
			return new File(dir, fileName);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("couldn't move '" + file + "' to '" + dir + "'");
		}
	}
}
