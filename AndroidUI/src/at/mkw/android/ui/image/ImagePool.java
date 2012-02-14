package at.mkw.android.ui.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import model.Screen;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import at.mkw.android.ui.ConvenienceMethods;
import at.mkw.android.ui.R;

public class ImagePool {

	private static HashMap<String, ImageObject> imageMap = new HashMap<String, ImageObject>();

	public static void reset() {
		imageMap = new HashMap<String, ImageObject>();
	}

	// public static void removeImages(Screen screen, boolean scrollable) {
	// ImageObject imageObject = imageMap.get(screen.getFullId()+scrollable);
	// imageObject.image.recycle();
	// imageMap.remove(screen.getFullId()+scrollable);
	// }

	public static ImageObject loadImages(Screen screen, Resources res, DisplayMetrics metrics, boolean scrollable) {

		Bitmap finalImage = null;

		String alignment = "vertical";

		// TODO does not support different display metrics, if other metrics are
		// given but image is already in map it is returned
		ImageObject imageObject = ImagePool.getImageFor(screen.getFullId() + scrollable);

		// if image is not in store load all partimages from files, combine them
		// to one image and save it
		if (imageObject == null) {

			// load images
			Bitmap[] images = new Bitmap[screen.getImages().size()];

			int rawWidth = 0, rawHeight = 0;

			for (int i = 0; i < images.length; i++) {

				// load image from file
				images[i] = BitmapFactory.decodeFile(ConvenienceMethods.IMAGE_DIR + screen.getImages().get(i));

				if (images[i] != null) {
					if (alignment == "vertical") {
						rawWidth = rawWidth > images[i].getWidth() ? rawWidth : images[i].getWidth();
						rawHeight += images[i].getHeight();
					} else if (alignment == "horizontal") {
						rawWidth += images[i].getWidth();
						rawHeight = rawHeight > images[i].getHeight() ? rawHeight : images[i].getHeight();
					} else {
						throw new IllegalArgumentException("Unknown alignment " + alignment);
					}
				}
				else {
					Log.e("UITester", "Image from source"+ConvenienceMethods.IMAGE_DIR + screen.getImages().get(i)+" couldn't be found");
				}
			}

			Bitmap imageBitmap;
			
			// if loaded images couldn't be found load "no image" image
			if (rawWidth == 0 || rawHeight == 0) {
				imageBitmap = BitmapFactory.decodeResource(res, R.drawable.noimg);
			}
			else {
				imageBitmap = Bitmap.createBitmap(rawWidth, rawHeight, Bitmap.Config.ARGB_8888);
				
				// paint all part images into combined image
				Canvas canvas = new Canvas(imageBitmap);

				int x = 0, y = 0;

				for (int i = 0; i < images.length; i++) {

					if (images[i] != null) {
						canvas.drawBitmap(images[i], x, y, null);

						if (alignment == "vertical") {
							y += images[i].getHeight();
						} else {
							x += images[i].getWidth();
						}

						images[i].recycle();
					}
				}
			}

			// scales are needed for scaling button panels
			float scaleX = (float) (metrics.widthPixels) / (float) imageBitmap.getWidth();
			float scaleY = ((float) (metrics.heightPixels) / (float) imageBitmap.getHeight());

			if (scrollable) {
				// scale that image fits x axis (y variable)
				finalImage = Bitmap.createScaledBitmap(imageBitmap, metrics.widthPixels, (int) (imageBitmap.getHeight() * scaleX), true);
				imageObject = new ImageObject(finalImage, scaleX, scaleX);
			} else {
				// scale to fit x and y axis
				finalImage = Bitmap.createScaledBitmap(imageBitmap, metrics.widthPixels, metrics.heightPixels, true);
				imageObject = new ImageObject(finalImage, scaleX, scaleY);
			}

			imageBitmap.recycle();

			// ImagePool.putImage(screen.getFullId()+scrollable, imageObject);
		}

		return imageObject;

	}

	private static ImageObject getImageFor(String path) {

		return imageMap.get(path);
	}

	private static void putImage(String path, ImageObject image) {
		imageMap.put(path, image);
	}

	/**
	 * Should load to large images, not used yet
	 * 
	 * @param f
	 * @param IMAGE_MAX_SIZE
	 * @return
	 */
	private static Bitmap decodeFile(File f, int IMAGE_MAX_SIZE) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();

			int scale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}
}
