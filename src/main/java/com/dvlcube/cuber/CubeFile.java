package com.dvlcube.cuber;

import java.io.File;
import java.io.IOException;

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

	public boolean isDir() {
		return o.isDirectory();
	}
}
