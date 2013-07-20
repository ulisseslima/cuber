package com.dvlcube.cuber.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.dvlcube.cuber.Cuber;

/**
 * 
 * @author wonka
 * @since 24/05/2013
 */
public class ImageUtils {
	public enum RandomMode {
		HSB_PATTERN {
			@Override
			public BufferedImage randomize(Dimension dimension) {
				return ImageUtils.randomPattern(dimension);
			}
		},
		RGB {
			@Override
			public BufferedImage randomize(Dimension dimension) {
				return ImageUtils.random(dimension);
			}
		};
		private static Random random = new Random();

		/**
		 * @return
		 * @since 04/07/2013
		 * @author wonka
		 */
		public static RandomMode getAny() {
			int modes = RandomMode.values().length;
			return RandomMode.values()[random.nextInt(modes)];
		}

		public BufferedImage randomize(Dimension dimension) {
			return null;
		}
	}

	private static final int[] RGB_MASKS = { 0xFF0000, 0xFF00, 0xFF };

	private static final ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);

	/**
	 * @param original
	 * @return binarized image.
	 * @since 21/06/2013
	 * @author wonka
	 */
	public static BufferedImage binarize(BufferedImage original) {
		int red;
		int newPixel;

		int threshold = otsuTreshold(original);

		BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

		for (int i = 0; i < original.getWidth(); i++) {
			for (int j = 0; j < original.getHeight(); j++) {
				// Get pixels
				red = new Color(original.getRGB(i, j)).getRed();
				int alpha = new Color(original.getRGB(i, j)).getAlpha();
				if (red > threshold) {
					newPixel = 255;
				} else {
					newPixel = 0;
				}

				newPixel = colorFromRGBA(newPixel, newPixel, newPixel, alpha);
				binarized.setRGB(i, j, newPixel);
			}
		}
		return binarized;
	}

	public static int color(Color color) {
		int red = color.getRed(), green = color.getGreen(), blue = color.getBlue();
		int newPixel = 0;
		int alpha = 1;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red;
		newPixel = newPixel << 8;
		newPixel += green;
		newPixel = newPixel << 8;
		newPixel += blue;

		return newPixel;
	}

	public static int colorFromHSB(float h, float s, float b) {
		Color c = Color.getHSBColor(h, s, b);
		return color(c);
	}

	public static int colorFromRGB(int red, int green, int blue) {
		int newPixel = 0;
		int alpha = 1;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red;
		newPixel = newPixel << 8;
		newPixel += green;
		newPixel = newPixel << 8;
		newPixel += blue;

		return newPixel;
	}

	/**
	 * Convert R, G, B, Alpha to standard 8 bit
	 * 
	 * @param alpha
	 * @param red
	 * @param green
	 * @param blue
	 * @return standard 8 bit
	 * @since 21/06/2013
	 * @author wonka
	 */
	public static int colorFromRGBA(int red, int green, int blue, int alpha) {
		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red;
		newPixel = newPixel << 8;
		newPixel += green;
		newPixel = newPixel << 8;
		newPixel += blue;

		return newPixel;
	}

	/**
	 * http://stackoverflow.com/questions/4386446/problem-using-imageio-write-jpg-file/4388542#4388542
	 * <p>
	 * I needed to make all this stuff with RGB masks and the color model because otherwise pixels wouldn't
	 * get drawn correctly.
	 * 
	 * @param pixels
	 *            the pixels.
	 * @param width
	 *            width.
	 * @param height
	 *            height.
	 * @return a buffered image created from the pixel array.
	 * @since 25/06/2013
	 * @author wonka
	 */
	public static BufferedImage draw(int[] pixels, int width, int height) {
		DataBuffer buffer = new DataBufferInt(pixels, width * height);
		WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
		BufferedImage image = new BufferedImage(RGB_OPAQUE, raster, false, null);
		return image;
	}

	/**
	 * @param image
	 *            the image to label.
	 * @param text
	 *            the text to use.
	 * @return the image with a label on it.
	 * @since 08/07/2013
	 * @author wonka
	 */
	public static BufferedImage drawLabel(BufferedImage image, String text) {
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(new Color(255, 255, 255, 255));
		int w1 = image.getWidth() * 2 / 100;
		int y1 = 10;
		int labelSize = 30;
		g.fillRect(w1, y1, image.getWidth() - (w1 + w1), labelSize);

		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		int fontSize = labelSize * 70 / 100;
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
		g.drawString(text, w1, y1 + fontSize);
		return image;
	}

	/**
	 * @param w
	 * @param h
	 * @return the pixels of an HSB color palette.
	 * @author wonka
	 * @since 11/07/2013
	 */
	public static int[] hsbPallete(int w, int h) {
		new Random();
		int[] pixels = new int[w * h];
		int x = 0, y = 0;

		for (int i = 0; i < pixels.length; i++) {
			if (x == w) {
				x = 0;
				y++;
			}
			float hue = Cuber.$(x, 0, w).map(0, 1).f();
			float sb = Cuber.$(y, 0, h).map(0, 1).f();

			pixels[i] = colorFromHSB(hue, sb, sb);
			x++;
		}
		return pixels;
	}

	/**
	 * @param input
	 * @return Return histogram of grayscale image
	 * @since 21/06/2013
	 * @author wonka
	 */
	public static int[] imageHistogram(BufferedImage input) {
		int[] histogram = new int[256];

		for (int i = 0; i < histogram.length; i++) {
			histogram[i] = 0;
		}

		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {
				int red = new Color(input.getRGB(i, j)).getRed();
				histogram[red]++;
			}
		}

		return histogram;
	}

	// Get binary threshold using Otsu's method
	public static int otsuTreshold(BufferedImage original) {
		int[] histogram = imageHistogram(original);
		int total = original.getHeight() * original.getWidth();

		float sum = 0;
		for (int i = 0; i < 256; i++) {
			sum += i * histogram[i];
		}

		float sumB = 0;
		int wB = 0;
		int wF = 0;

		float varMax = 0;
		int threshold = 0;

		for (int i = 0; i < 256; i++) {
			wB += histogram[i];
			if (wB == 0) {
				continue;
			}
			wF = total - wB;

			if (wF == 0) {
				break;
			}

			sumB += i * histogram[i];
			float mB = sumB / wB;
			float mF = (sum - sumB) / wF;

			float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

			if (varBetween > varMax) {
				varMax = varBetween;
				threshold = i;
			}
		}
		return threshold;
	}

	public static int[] pixels(BufferedImage image) {
		return image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
	}

	public static byte[] pixelsb(BufferedImage image) {
		DataBuffer dataBuffer = image.getRaster().getDataBuffer();
		if (dataBuffer instanceof DataBufferByte) {
			return ((DataBufferByte) dataBuffer).getData();
		}
		throw new IllegalArgumentException("image is instanceof " + dataBuffer.getClass());
	}

	/**
	 * http://stackoverflow.com/questions/4386446/problem-using-imageio-write-jpg-file/4388542#4388542
	 * 
	 * @param image
	 * @return image pixels.
	 * @since 26/06/2013
	 * @author wonka
	 */
	public static Object pixelsg(BufferedImage image) {
		PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, true);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return pg.getPixels();
	}

	/**
	 * @param image
	 *            the image.
	 * @return image pixels.
	 * @since 21/06/2013
	 * @author wonka
	 */
	public static int[] pixelsi(BufferedImage image) {
		DataBuffer dataBuffer = image.getRaster().getDataBuffer();
		if (dataBuffer instanceof DataBufferInt) {
			return ((DataBufferInt) dataBuffer).getData();
		}
		throw new IllegalArgumentException("image is instanceof " + dataBuffer.getClass());
	}

	/**
	 * @param dimension
	 *            desired dimensions.
	 * @return a random BufferedImage with the specified dimensions.
	 * @since 03/07/2013
	 * @author wonka
	 */
	public static BufferedImage random(Dimension dimension) {
		Random random = new Random();
		int[] pixels = new int[dimension.width * dimension.height];
		for (int i = 0; i < pixels.length; i++) {
			int r = random.nextInt(256);
			int g = random.nextInt(256);
			int b = random.nextInt(256);
			pixels[i] = colorFromRGB(r, g, b);
		}
		return draw(pixels, dimension.width, dimension.height);
	}

	/**
	 * @param dimension
	 * @return crack like patterns.
	 * @author wonka
	 * @since 11/07/2013
	 */
	public static BufferedImage randomCrackPattern(Dimension dimension) {
		Random random = new Random();
		int w = dimension.width;
		int h = dimension.height;
		int[] pixels = new int[w * h];
		int x = 0;
		// int thickness = 1;
		int cracks = 5;
		int[] previousPlots = new int[cracks];
		int[] nextPlots = new int[cracks];
		for (int i = 0; i < nextPlots.length; i++) {
			nextPlots[i] = random.nextInt(w / 2);
		}

		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < cracks; j++) {
				if (x == nextPlots[j]) {
					pixels[i] = colorFromRGB(0, 0, 0);
					previousPlots[j] = nextPlots[j];
					nextPlots[j] = previousPlots[j] + random.nextInt(3) - 1;
				} else {
					pixels[i] = colorFromRGB(255, 255, 255);
				}
			}

			if (x == w) {
				x = 0;
			}
			x++;
		}
		return draw(pixels, w, h);
	}

	/**
	 * @param dimension
	 * @return a random pattern.
	 * @author wonka
	 * @since 11/07/2013
	 */
	public static BufferedImage randomPattern(Dimension dimension) {
		Random random = new Random();
		int w = dimension.width;
		int h = dimension.height;

		int[] pixels = new int[w * h];
		int x = 0, y = 0;

		int factorFunc = random.nextInt(4);
		double xFactor = 0;
		double yFactor = 0;
		switch (factorFunc) {
		case 0:
			xFactor = random.nextInt(w / 2 + h * 3) + 1;
			yFactor = random.nextInt(h / 2 + w * 2) + 1;
			break;
		case 1:
			xFactor = random.nextInt(56) + 1;
			yFactor = random.nextInt(56) + 1;
			break;
		case 2:
			xFactor = random.nextInt(w * h) + 1;
			yFactor = random.nextInt(h / w + 56) + 1;
			break;
		case 3:
			xFactor = random.nextInt(w * 23 * h) + 1;
			yFactor = random.nextInt(h / w + 56 * 23) + 1;
			break;
		}

		double fixer = 0.001;

		int hRange1 = random.nextInt(999) + 1;
		int hRange2 = random.nextInt(hRange1 * 2) + 1 + hRange1;

		int sRange1 = random.nextInt(999) + 1;
		int sRange2 = random.nextInt(sRange1 * 2) + 1 + sRange1;

		int bRange1 = random.nextInt(999) + 1;
		int bRange2 = random.nextInt(bRange1 * 2) + 1 + bRange1;

		for (int i = 0; i < pixels.length; i++) {
			if (x == w) {
				x = 0;
				y++;
			}

			double hAngle = 0;
			double sAngle = 0;
			double bAngle = 0;
			int func = random.nextInt(4);
			switch (func) {
			case 0:
				hAngle = Math.sin(x * xFactor / (y + 1) * w);
				sAngle = Math.sin(x * y / xFactor * h);
				bAngle = Math.sin(y / yFactor * (x / xFactor));
				break;
			case 1:
				hAngle = Math.sin(y * x / Math.PI * w);
				sAngle = Math.cos(x / (y + 1) * (y + 1));
				bAngle = Math.sin(x + xFactor * (y + 1) * Math.PI);
				break;
			case 2:
				hAngle = Math.cos(x + y / xFactor * h);
				sAngle = Math.sin(x * xFactor / (y + 1) * w);
				bAngle = Math.cos(y + yFactor / ((x + 1) * xFactor));
				break;
			case 3:
				hAngle = Math.cos(x + y / yFactor * h);
				sAngle = Math.cos(x * yFactor / (y + 1) * w);
				bAngle = Math.cos(y + xFactor / ((x + 1) * yFactor));
				break;
			}

			float hue = Cuber.$(hAngle, -1, 1).map(hRange1 * fixer, hRange2 * fixer).f();
			float saturation = Cuber.$(sAngle, -1, 1).map(sRange1 * fixer, sRange2 * fixer).f();
			float brightness = Cuber.$(bAngle, -1, 1).map(bRange1 * fixer, bRange2 * fixer).f();

			pixels[i] = colorFromHSB(hue, saturation, brightness);

			x++;
		}
		return draw(pixels, w, h);
	}

	/**
	 * @param dimension
	 * @return a random pattern.
	 * @since 04/07/2013
	 * @author wonka
	 */
	public static BufferedImage randomPattern1(Dimension dimension) {
		Random random = new Random();
		int[] pixels = new int[dimension.width * dimension.height];
		int x = 0, y = 0;
		int xFactor = random.nextInt(56) + 1;
		int yFactor = random.nextInt(56) + 1;
		for (int i = 0; i < pixels.length; i++) {
			double sin = Math.sin(y / yFactor + x / xFactor);
			double s = Math.sin((x / yFactor + y / xFactor) / (y + 1));
			double b = Math.sin(y / yFactor * (x / xFactor));

			float hue = Cuber.$(sin, -1, 1).map(0, 1).f();
			float saturation = Cuber.$(s, -1, 1).map(0, 1).f();
			float brightness = Cuber.$(b, -1, 1).map(0, 1).f();

			pixels[i] = colorFromHSB(hue, saturation, brightness);
			if (x == dimension.width) {
				x = 0;
				y++;
			}
			x++;
		}
		return draw(pixels, dimension.width, dimension.height);
	}

	/**
	 * @param dimension
	 * @return a random pattern.
	 * @author wonka
	 * @since 11/07/2013
	 */
	public static BufferedImage randomPattern2(Dimension dimension) {
		Random random = new Random();
		int[] pixels = new int[dimension.width * dimension.height];
		int x = 0, y = 0;
		int xFactor = random.nextInt(56) + 1;
		int yFactor = random.nextInt(56) + 1;
		for (int i = 0; i < pixels.length; i++) {
			double sin = Math.sin(y / yFactor + x / xFactor);
			double s = Math.sin(x * y / xFactor);
			double b = Math.cos(y / yFactor * (x / xFactor));

			float hue = Cuber.$(sin, -1, 1).map(0, 1).f();
			float saturation = Cuber.$(s, -1, 1).map(0, 1).f();
			float brightness = Cuber.$(b, -1, 1).map(0, 1).f();

			pixels[i] = colorFromHSB(hue, saturation, brightness);
			if (x == dimension.width) {
				x = 0;
				y++;
			}
			x++;
		}
		return draw(pixels, dimension.width, dimension.height);
	}

	/**
	 * @param image
	 * @param w
	 *            new width
	 * @param h
	 *            new height
	 * @return a resized version of the image.
	 * @author wonka
	 * @since 24/05/2013
	 */
	public static BufferedImage resize(BufferedImage image, int w, int h) {
		BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, w, h, null);
		g.dispose();
		return resizedImage;
	}

	/**
	 * @param image
	 * @param percent
	 *            percent of the size desired.
	 * @return
	 * @author wonka
	 * @since 24/05/2013
	 */
	public static BufferedImage scale(BufferedImage image, int percent) {
		// original height / original width x new width = new height
		int width = image.getWidth();
		int height = image.getHeight();
		int newWidth = width * percent / 100;
		int newHeight = (int) ((double) height / width * newWidth);
		return resize(image, newWidth, newHeight);
	}

	/**
	 * @param image
	 * @return The image, ascii format.
	 * @author wonka
	 * @since 24/05/2013
	 */
	public static String toAscii(BufferedImage image) {
		StringBuilder builder = new StringBuilder();

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int red = new Color(image.getRGB(x, y)).getRed();

				if (red >= 0 && red <= 15) {
					builder.append("██");
				} else if (red > 15 && red <= 75) {
					builder.append("▓▓");
				} else if (red > 75 && red <= 135) {
					builder.append("▒▒");
				} else if (red > 135 && red <= 195) {
					builder.append("░░");
				} else if (red > 195 && red <= 255) {
					builder.append("  ");
				} else if (red < 0 || red > 255) {
					builder.append("oo");
				}
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	/**
	 * @param fileName
	 *            file name.
	 * @return the image, ascii format
	 * @author wonka
	 * @since 24/05/2013
	 */
	public static String toAscii(String fileName) {
		File file = new File(fileName);
		BufferedImage image;
		try {
			image = ImageIO.read(file);
			return toAscii(image);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * @param fileName
	 * @param scale
	 *            scale factor (in percent)
	 * @return the ascii representation.
	 * @author wonka
	 * @since 24/05/2013
	 */
	public static String toAscii(String fileName, int scale) {
		File file = new File(fileName);
		BufferedImage image;
		try {
			image = ImageIO.read(file);
			return toAscii(scale(image, scale));
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * The luminance method
	 * 
	 * @param original
	 * @return grayscale image.
	 * @since 21/06/2013
	 * @author wonka
	 */
	public static BufferedImage toGray(BufferedImage original) {
		int alpha, red, green, blue;
		int newPixel;

		BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		for (int i = 0; i < original.getWidth(); i++) {
			for (int j = 0; j < original.getHeight(); j++) {

				// Get pixels by R, G, B
				alpha = new Color(original.getRGB(i, j)).getAlpha();
				red = new Color(original.getRGB(i, j)).getRed();
				green = new Color(original.getRGB(i, j)).getGreen();
				blue = new Color(original.getRGB(i, j)).getBlue();

				red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
				// Return back to original format
				newPixel = colorFromRGBA(red, red, red, alpha);

				// Write pixels into image
				lum.setRGB(i, j, newPixel);
			}
		}
		return lum;
	}

	/**
	 * Writes a BufferedImage to the disc.
	 * 
	 * @param image
	 *            BufferedImage.
	 * @param file
	 *            destination.
	 * @since 08/07/2013
	 * @author wonka
	 */
	public static void write(BufferedImage image, File file) {
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes a BufferedImage to the disc.
	 * 
	 * @param image
	 *            BufferedImage.
	 * @param file
	 *            destination.
	 * @since 03/07/2013
	 * @author wonka
	 */
	public static void write(BufferedImage image, String file) {
		write(image, new File(file));
	}

	/**
	 * Writes an array of pixels to the disc.
	 * 
	 * @param pixels
	 *            the pixels.
	 * @param dimension
	 *            dimensions.
	 * @param file
	 *            destination.
	 * @since 03/07/2013
	 * @author wonka
	 */
	public static void write(int[] pixels, Dimension dimension, String file) {
		BufferedImage image = draw(pixels, dimension.width, dimension.height);
		write(image, file);
	}
}
