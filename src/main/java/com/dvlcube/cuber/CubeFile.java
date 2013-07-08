package com.dvlcube.cuber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 
 * @since 25/06/2013
 * @author wonka
 */
public class CubeFile {
	public File o;

	/**
	 * @param path
	 * @since 25/06/2013
	 * @author wonka
	 */
	public CubeFile(String path, boolean create) {
		o = new File(path);
		if (!o.isFile()) {
			if (create)
				try {
					o.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			else
				throw new IllegalArgumentException("'" + path + "' is not a file");
		}
	}

	public CubeFile(String path) {
		this(path, true);
	}

	/**
	 * @param path
	 * @since 04/07/2013
	 * @author wonka
	 */
	public CubeFile(CubeString path) {
		this(path.o, true);
	}

	public boolean isDir() {
		return o.isDirectory();
	}

	public String firstLine() {
		return FileUtils.firstLine(o);
	}

	public List<String> head(int lines) {
		return FileUtils.head(o, lines);
	}

	/**
	 * Appends a line to the file, line ends with line break.
	 * 
	 * @param line
	 *            line to append.
	 * @return this.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public CubeFile append(String line) {
		try (PrintWriter writer = new PrintWriter(o)) {
			writer.println(line);
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * @param extension
	 *            the desired extension.
	 * @return true if the file has the passed extension.
	 * @since 07/07/2013
	 * @author wonka
	 */
	public boolean is(String extension) {
		if (o == null)
			throw new IllegalStateException("file was not initialized");

		if (o.getPath().endsWith(extension))
			return true;

		return false;
	}
}
