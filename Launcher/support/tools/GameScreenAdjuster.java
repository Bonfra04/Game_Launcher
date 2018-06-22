package tools;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameScreenAdjuster {

	 /**
	 * Image to adjust
	 */
	private static final String PATH = "res/textures/screen.png";

	private static final int MAGIC_NUMBER = 16777216;

	public static void main(String[] args) {
		BufferedImage input = null;
		try {
			input = ImageIO.read(new File(PATH));
		} catch (IOException e) {
			System.out.println("Error while loading image");
		}

		input = adjustImage(input, 9, 5);

		try {
			ImageIO.write(input, "png", new File(PATH));
		} catch (IOException e) {
			System.err.println("Error while saving image");
		}

	}

	public static BufferedImage adjustImage(BufferedImage input, int radius, int width) {
		BufferedImage image = new BufferedImage(300, 155, BufferedImage.TYPE_INT_ARGB);
		BufferedImage reference = resize(input, 300, 155);
		BufferedImage mask = border(radius, width);
		for (int y = 0; y < image.getHeight(); y++)
			for (int x = 0; x < image.getWidth(); x++)
				image.setRGB(x, y,
						mask.getRGB(x, y) + MAGIC_NUMBER == 0 ? 0 : (reference.getRGB(x, y) + 256 * MAGIC_NUMBER));
		return image;
	}

	private static BufferedImage resize(BufferedImage image, int width, int height) {
		BufferedImage res = new BufferedImage(width, height, image.getType());

		AffineTransform at = new AffineTransform();
		at.scale((double) width / image.getWidth(), (double) height / image.getHeight());

		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

		res = scaleOp.filter(image, res);

		return res;
	}

	private static BufferedImage border(int radius, int width) {
		BufferedImage res = new BufferedImage(300, 155, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < res.getHeight(); y++)
			for (int x = 0; x < res.getWidth(); x++)
				res.setRGB(x, y,
						isOutOfCircle(x, y, width + radius, width + radius, radius, 2)
								|| isOutOfCircle(x, y, res.getWidth() - width - radius - 1, width + radius, radius, 1)
								|| isOutOfCircle(x, y, width + radius, res.getHeight() - width - radius - 1, radius, 3)
								|| isOutOfCircle(x, y, res.getWidth() - width - radius - 1,
										res.getHeight() - width - radius - 1, radius, 4)
								|| y < width || x < width || y >= res.getHeight() - width || x >= res.getWidth() - width
										? 0
										: MAGIC_NUMBER - 1);

		return res;
	}

	private static boolean isOutOfCircle(int x, int y, int xoff, int yoff, int radius, int quarter) {
		switch (quarter) {
		case 1:
			return !((0 - Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(x - xoff, 2))) <= y - yoff
					&& y - yoff <= Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(x - xoff, 2))))
					|| (0 - Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(y - yoff, 2))) <= x - xoff
							&& x - xoff <= Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(y - yoff, 2)))))
					&& 0 - radius <= y - yoff && y - yoff <= 0 && 0 <= x - xoff && x - xoff <= radius;

		case 2:
			return !((0 - Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(x - xoff, 2))) <= y - yoff
					&& y - yoff <= Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(x - xoff, 2))))
					|| (0 - Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(y - yoff, 2))) <= x - xoff
							&& x - xoff <= Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(y - yoff, 2)))))
					&& 0 - radius <= y - yoff && y - yoff <= 0 && 0 - radius <= x - xoff && x - xoff <= 0;

		case 3:
			return !((0 - Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(x - xoff, 2))) <= y - yoff
					&& y - yoff <= Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(x - xoff, 2))))
					|| (0 - Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(y - yoff, 2))) <= x - xoff
							&& x - xoff <= Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(y - yoff, 2)))))
					&& 0 <= y - yoff && y - yoff <= radius && 0 - radius <= x - xoff && x - xoff <= 0;

		case 4:
			return !((0 - Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(x - xoff, 2))) <= y - yoff
					&& y - yoff <= Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(x - xoff, 2))))
					|| (0 - Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(y - yoff, 2))) <= x - xoff
							&& x - xoff <= Math.round(Math.sqrt(Math.pow(radius, 2) - Math.pow(y - yoff, 2)))))
					&& 0 <= y - yoff && y - yoff <= radius && 0 <= x - xoff && x - xoff <= radius;
		}

		return false;
	}
}
