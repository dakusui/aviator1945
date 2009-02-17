package avis.spec;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import javax.imageio.ImageIO;

import avis.base.AException;
import avis.base.AExceptionThrower;
import avis.base.Avis;
import avis.sprite.AImageSprite;
import avis.sprite.ASprite;


public class AImageSpriteSpec extends ASpriteSpec{
	protected static final String KEY_DISPLAY_HEIGHT     = "image.display.height";
	protected static final String KEY_DISPLAY_WIDTH      = "image.display.width";
	protected static final String KEY_IMAGES     = "image.images";
	protected static final String KEY_HEIGHT     = "image.height";
	protected static final String KEY_WIDTH      = "image.width";
	protected static final String KEY_DIRECTIONS = "image.directions";
	protected static final String KEY_IMAGE      = "image.{0}";
	protected static final String KEY_NAME       = "image.name";

	protected static final double UNIT_RATIO = 1.41;

	protected int displayWidth;
	protected int displayHeight;
	protected int unitWidth;
	protected int unitHeight;
	protected int biasX;
	protected int biasY;
	protected int[] unitX;
	protected int[] unitY;
	protected int directionUnits;
	protected int imageUnits;
	protected BufferedImage image;
	protected int patternDenominator;

	@Override
	public void init(Object propertiesResourceName) throws AException {
		InputStream is = Avis.openUrl(propertiesResourceName.toString());
		try {
			init(is);
		} finally {
			Avis.closeStream(is);
		}
	}
	
	public void init(InputStream is) throws AException {
		Properties properties = Avis.loadPropertes(is);
		init(properties);
	}

	public void init(Properties properties) throws AException {
		if (directionUnits > 0) {
			this.unitWidth  = (int) (Math.ceil(getWidth(properties) * UNIT_RATIO));
			this.unitHeight = (int) (Math.ceil(getHeight(properties) * UNIT_RATIO));
		} else {
			this.unitWidth  = (int) (Math.ceil(getWidth(properties) ));
			this.unitHeight = (int) (Math.ceil(getHeight(properties) ));
		}
		this.displayHeight = getDisplayHeight(properties);
		this.displayWidth = getDisplayWidth(properties);
		biasX = displayWidth  / 2;
		biasY = displayHeight / 2;
		this.directionUnits = getNumberOfDirections(properties);
		this.imageUnits = getNumberOfImages(properties);
		this.patternDenominator = Avis.BANK_STEPS / imageUnits;
		int units = Math.max(1, directionUnits / 2);
		unitX = new int[units];
		unitY = new int[imageUnits];
		for (int i = 0; i < units; i++) {
			unitX[i] = i * unitWidth;
		}
		for (int i = 0; i < imageUnits; i++) {
			unitY[i] = i * unitHeight;
		}
		if (directionUnits > 0) {
			this.image = generateTurnedImages(properties);
		} else {
			this.image = generateNonturnedImage(properties);
		}
	}

	private BufferedImage generateNonturnedImage(Properties properties) throws AException {
		BufferedImage ret = new BufferedImage(
				(int)(unitWidth),
				(int)(unitHeight),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) ret.getGraphics();
		initializeImage(g, ret.getWidth(), ret.getHeight());
		Image sourceImage = getImageFromProperty(properties, 0);
		g.drawImage(
				sourceImage, 
				0, 0, this.unitWidth, this.unitHeight, 
				0, 0, sourceImage.getWidth(null), sourceImage.getHeight(null), 
				null);
		
		g.dispose();
		return ret;
	}

	public int getNumberOfImages(Properties properties) throws ASpecException {
		return getIntegerFromProperty(properties, KEY_IMAGES);
	}

	public int getNumberOfDirections(Properties properties) throws ASpecException {
		return getIntegerFromProperty(properties, KEY_DIRECTIONS);
	}

	public int getWidth(Properties properties) throws ASpecException {
		return getIntegerFromProperty(properties, KEY_WIDTH);
	}

	public int getHeight(Properties properties) throws ASpecException {
		return getIntegerFromProperty(properties, KEY_HEIGHT);
	}
	public int getDisplayWidth(Properties properties) throws ASpecException {
		return getIntegerFromProperty(properties, KEY_DISPLAY_WIDTH);
	}

	public int getDisplayHeight(Properties properties) throws ASpecException {
		return getIntegerFromProperty(properties, KEY_DISPLAY_HEIGHT);
	}

	protected int getIntegerFromProperty(Properties properties, String key) throws ASpecException {
		int ret;
		String s = properties.getProperty(key);
		if (s == null)  {
			AExceptionThrower.throwSpecifiedKeyNotFoundException(key);
		}
		try {
			ret = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			AExceptionThrower.throwSpecifiedKeyMalformattedException(key, s);
			// this path will never be passed.
			return -1;
		}
		return ret;
	}
	
	protected BufferedImage generateTurnedImages(Properties properties) throws AException {
		BufferedImage ret = new BufferedImage(
				(int)(unitWidth  * this.directionUnits) / 2, 
				(int)(unitHeight) * this.imageUnits,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) ret.getGraphics();
		initializeImage(g, ret.getWidth(), ret.getHeight());
		for (int i = 0; i < imageUnits; i++) {
			Image sourceImage = getImageFromProperty(properties, i);
			int units = Math.max(1, directionUnits / 2);
			for (int j = 0; j < units; j++) {
				generateRotatedImage(ret, sourceImage, j, i);
			}
		}
		g.dispose();
		return ret;
	}

	protected Image getImageFromProperty(Properties properties, int i) throws AException {
		Image ret = null;
		String s = (s = "00" + i).substring(s.length() - 2);
		String imageResource = properties.getProperty(MessageFormat.format(KEY_IMAGE, s));
		InputStream is = Avis.openUrl(imageResource);
		try {
			ret = ImageIO.read(is);
		} catch (IOException e) {
			AExceptionThrower.throwFailedToLoadImage(imageResource + "(" + i + ")", e);
		} finally {
			Avis.closeStream(is);
		}
		return ret;
	}

	protected void initializeImage(Graphics g, int w, int h) {
		Color backup = g.getColor();
		Color cc = new Color(0, true);
		g.setColor(cc);
		g.fillRect(0, 0, w, h);
		g.setColor(backup);
	}

	/**
	 * render specified angle rotated image on destination image object.
	 * @param destination destination graphics object
	 * @param source source image object.
	 * @param direction direction index to set.
	 * @param image image index to set.
	 */
	protected void generateRotatedImage(Image destination, Image source, int direction, int image) {
		int originalWidth  = source.getWidth(null);
		int originalHeight = source.getHeight(null);
		double theta = (double)direction / (double)this.directionUnits * Avis.DIRECTION_STEPS;
		Graphics2D g = (Graphics2D) destination.getGraphics();

		BufferedImage tmpImage = new BufferedImage(unitWidth, unitHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D tmpG = (Graphics2D) tmpImage.getGraphics();
		initializeImage(tmpG, unitWidth, unitHeight);
		tmpG.rotate(
				((double)(Avis.DIRECTION_STEPS / 4 - theta) / (double)Avis.DIRECTION_STEPS) * 2 * Math.PI,
				(unitWidth)/ 2.0,
				(unitHeight) / 2.0
				);
		tmpG.drawImage(
				source, 
				0, 0, tmpImage.getWidth() - 1, tmpImage.getHeight() - 1, 
				0, 0, originalWidth, originalHeight,
				null);
		tmpG.dispose();
		g.drawImage(tmpImage, 
				unitX[direction],                  unitY[image],  
				unitX[direction] + unitWidth - 1,  unitY[image] + unitHeight - 1,
				0,                 0,
				 unitWidth - 1, unitHeight - 1,
				null,
				null);
		tmpImage.flush();
		tmpImage = null;
				/*
		g.rotate(((double)(Avis.DIRECTION_STEPS / 4 - theta) / (double)Avis.DIRECTION_STEPS) * 2 * Math.PI,
				unitX[direction] + (unitWidth)/ 2.0,
				unitY[image] +     (unitHeight) / 2.0);
		g.drawImage(source,     
				unitX[direction],                  unitY[image],  
				unitX[direction] + unitWidth - 1,  unitY[image] + unitHeight - 1,
				0,                 0,
				originalWidth - 1, originalHeight - 1,
				null,
				null);
				*/
		g.dispose();
	}
	
	/**
	 * render specified angle rotated image on destination image object.
	 * @param g destination graphics object
	 * @param source source image object.
	 * @param direction direction index to set.
	 * @param image image index to set.
	 */
	protected void generateRotatedImage(Graphics2D g, Image source, int direction, int image) {
		int originalWidth  = source.getWidth(null);
		int originalHeight = source.getHeight(null);
		double theta = (double)direction / (double)this.directionUnits * Avis.DIRECTION_STEPS;
		g.rotate(((double)(Avis.DIRECTION_STEPS / 4 - theta) / (double)Avis.DIRECTION_STEPS) * 2 * Math.PI,
				unitX[direction] + (unitWidth)/ 2.0,
				unitY[image] +     (unitHeight) / 2.0);
		g.drawImage(source,     
				unitX[direction],                  unitY[image],  
				unitX[direction] + unitWidth - 1,  unitY[image] + unitHeight - 1,
				0,                 0,
				originalWidth - 1, originalHeight - 1,
				null,
				null);
	}
	
	
	public int defaultDisplayHeight() {
		return unitWidth;
	}

	public int defaultDisplayWidth() {
		return unitHeight;
	}
	
	public int unitWidth() {
		return unitWidth;
	}

	public int unitHeight() {
		return unitHeight;
	}

	public void paint(Graphics g, int x, int y, int pattern, int direction,
			int displayWidth, int displayHeight, ImageObserver observer) {
		if (directionUnits > 0) {
			double directionRatio = (double)directionUnits / (double)Avis.DIRECTION_STEPS;
			int j = pattern;
			int i = (int) (direction *  directionRatio) % this.directionUnits;
			if (i < this.directionUnits / 2 || this.directionUnits == 1) {
				g.drawImage(
						this.image,
						x - biasX,            
						y - biasY, 
						x + biasX - 1,       
						y + biasY - 1, 
						unitX[i],         
						unitY[j], 
						unitX[i] + (int)(unitWidth)- 1, 
						unitY[j] + (int)(unitHeight) - 1,
						observer
						);
			} else {
				i = i - this.directionUnits / 2;
				g.drawImage(
						this.image,
						x - biasX,            
						y - biasY, 
						x + biasX - 1,       
						y + biasY - 1, 
						unitX[i] + (int)(unitWidth)- 1, 
						unitY[j] + (int)(unitHeight) - 1,
						unitX[i],         
						unitY[j], 
						observer
						);
			}
		} else {
			g.drawImage(
					this.image,
					x - biasX,            
					y - biasY, 
					x + biasX - 1,       
					y + biasY - 1, 
					0,         
					0,
					unitWidth,
					unitHeight,
					observer
					);
		}
	}
	
	public int patternDenominator() {
		return this.patternDenominator;
	}
	
	@Override
	public ASprite createSprite_Protected() {
		AImageSprite ret = new AImageSprite();
		return ret;
	}
}
