package desktopui.general;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import model.Screen;
import model.ScreenCollection;
import model.Window;

import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.image.GaussianBlurFilter;

public class ImagePool {

	private static String WORKING_DIR;
	
	private static HashMap<String,BufferedImage> images = new HashMap<String,BufferedImage>();

	public static void reset() {
		
		for (BufferedImage image : images.values()) {
			image.flush();
		}
		
		images.clear();
		
		images = new HashMap<String,BufferedImage>();
	}
	
	public static BufferedImage getImage(String fileName) {

		if (images.containsKey(fileName)) {
			return images.get(fileName);
		}
		else {
			//load image
			try {
				if (fileName != null) {
					
					BufferedImage newImage=null;
					
					//load image from scenario
					if (new File(WORKING_DIR+fileName).exists()) {
						newImage=  ImageIO.read(new File(WORKING_DIR+fileName));
					}
					//if scenario image isn't available load "no image" from filesystem
					else if (new File("src/imgs/noimg.png").exists()) {
						newImage= ImageIO.read(new File("src/imgs/noimg.png"));
					}
					else { 
						newImage = ImageIO.read(ClassLoader.getSystemResource("imgs/noimg.png"));
					}
//					Disabled to save memory (no more memory leak)
//					images.put(fileName, newImage);
					return newImage;
				}
			} catch (IOException ex) {
				throw new RuntimeException("Failed loading image from resource "+WORKING_DIR + fileName);
				
			}
			
			return null;
		}
	}
	
	public static boolean isSupportedImage(String path) {
		
		String[] readerFileSuffixes = ImageIO.getReaderFileSuffixes();
		
		for (int i=0; i < readerFileSuffixes.length; i++) {
			if (path.endsWith("."+readerFileSuffixes[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Can be used to load all images at startup (memory intense)
	 * @param window
	 */
	public static void loadImages(Window window) {
		
		if (window instanceof Screen) {
			Screen screen = (Screen) window;
			
			for (String imagePath : screen.getImages()) {
				getImage(imagePath);
			}
		}
		else if (window instanceof ScreenCollection) {
			ScreenCollection collection = (ScreenCollection)window;
			
			for (Window child : collection.getWindowItems()) {
				loadImages(child);
			}
		}
		else {
			throw new RuntimeException("Unknown window type "+window.getClass());
		}
	}

	public static void setWorkingDir(String path) {
		WORKING_DIR = path;
	}
	
	public static BufferedImage getImage(ImagePanel panel) {

		BufferedImage image = GraphicsUtilities.createCompatibleImage(
				panel.getWidth(), panel.getHeight());
		Graphics2D g2 = image.createGraphics();
		panel.paint(g2);
		g2.dispose();
		return image;
	}
	
	public static BufferedImage getBlurredImage(BufferedImage image) {
		
		return new GaussianBlurFilter(15).filter(image, null);
	}

	public static BufferedImage getBlurredImage(ImagePanel panel) {

		BufferedImage blurImage = getImage(panel);
		blurImage = new GaussianBlurFilter(15).filter(blurImage, null);
		return blurImage;
	}
}
