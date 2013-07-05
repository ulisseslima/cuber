package com.dvlcube.cuber;

import static com.dvlcube.cuber.Cuber.$f;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.dvlcube.cuber.ImageUtils.RandomMode;

/**
 * 
 * @since 21/06/2013
 * @author wonka
 */
public class CubeImage {

	public BufferedImage o;
	public Class<?> origin;
	public final CubeString path = new CubeString();

	public CubeImage(int width, int height) {
		this.o = RandomMode.getAny().randomize(new Dimension(width, height));
	}

	public CubeImage(BufferedImage image) {
		this.o = image;
	}

	/**
	 * @param path
	 *            path to file.
	 * @since 26/06/2013
	 * @author wonka
	 */
	public CubeImage(String path) {
		try {
			this.path.reset(path);
			BufferedImage image = ImageIO.read($f(this.path).o);
			this.o = image;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param aClass
	 *            class to load the resource from.
	 * @param path
	 *            path to resource.
	 * @since 27/06/2013
	 * @author wonka
	 */
	public CubeImage(Class<?> aClass, String path) {
		try {
			this.path.reset(path);
			this.origin = aClass;
			BufferedImage image = ImageIO.read(aClass.getResourceAsStream(this.path.o));
			this.o = image;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return image pixels.
	 * @since 26/06/2013
	 * @author wonka
	 */
	public int[] pixels() {
		return ImageUtils.pixels(o);
	}

	public Object pixelsg() {
		return ImageUtils.pixelsg(o);
	}

	public int[] pixelsi() {
		return ImageUtils.pixelsi(o);
	}

	public byte[] pixelsb() {
		return ImageUtils.pixelsb(o);
	}

	/**
	 * @param pixels
	 *            pixels to override.
	 * @since 26/06/2013
	 * @author wonka
	 */
	public CubeImage write(int[] pixels) {
		if (path.isBlank())
			throw new IllegalArgumentException("path was not initialized");

		ImageUtils.write(pixels, new Dimension(o.getWidth(), o.getHeight()), path.o);
		return this;
	}

	/**
	 * @param pixels
	 *            pixels to override.
	 * @param path
	 *            desired file path.
	 * @since 26/06/2013
	 * @author wonka
	 */
	public CubeImage write(int[] pixels, String path) {
		this.path.reset(path);
		write(pixels);
		return this;
	}

	/**
	 * Writes this image to disc. The image have to be already initialized
	 * 
	 * @param path
	 *            destination.
	 * @return this.
	 * @since 03/07/2013
	 * @author wonka
	 */
	public CubeImage write(String path) {
		if (o == null)
			throw new IllegalStateException("this BufferedImage was not initialized");

		this.path.reset(path);
		ImageUtils.write(o, this.path.o);

		return this;
	}

	public int w() {
		return o.getWidth();
	}

	public int h() {
		return o.getHeight();
	}

	/**
	 * Fills this buffered image with a random pattern.
	 * 
	 * @param width
	 * @param height
	 * @return this.
	 * @since 04/07/2013
	 * @author wonka
	 */
	public CubeImage randomize(int width, int height) {
		RandomMode mode = RandomMode.getAny();
		return randomize(mode, width, height);
	}

	/**
	 * Fills this buffered image with a random pattern.
	 * 
	 * @return this.
	 * @since 04/07/2013
	 * @author wonka
	 */
	public CubeImage randomize() {
		if (o == null)
			throw new IllegalStateException("The BufferedImage was not initialized");

		RandomMode mode = RandomMode.getAny();
		return randomize(mode, o.getWidth(), o.getHeight());
	}

	public CubeImage randomize(RandomMode mode, int width, int height) {
		o = mode.randomize(new Dimension(width, height));
		return this;
	}
}
