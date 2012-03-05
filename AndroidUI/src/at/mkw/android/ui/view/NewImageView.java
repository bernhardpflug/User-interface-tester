package at.mkw.android.ui.view;

import model.ModelUtils;
import model.Screen;
import model.Window;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import at.mkw.android.ui.ConvenienceMethods;
import at.mkw.android.ui.R;

public class NewImageView extends View {

	protected Screen screen;
	protected DisplayMetrics metrics;
	protected Resources res;
	private float scaleX, scaleY;
	private int rawWidth = 0, rawHeight = 0, sizeX, sizeY;
	private String alignment;
	private BitmapDrawable bitmapDrawable;

	public NewImageView(Context context, Window window) {
		super(context);
		this.metrics = context.getResources().getDisplayMetrics();
		this.screen = ModelUtils.getActiveScreen(window);
		
		// load images
				rawWidth = 0;
				rawHeight = 0;
				Bitmap[] images = new Bitmap[screen.getImages().size()];
				alignment = "vertical";

				for (int i = 0; i < screen.getImages().size(); i++) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					images[i] = BitmapFactory.decodeFile(ConvenienceMethods.IMAGE_DIR + screen.getImages().get(i), options);

					if (alignment == "vertical") {
						rawWidth = rawWidth > options.outWidth ? rawWidth : options.outWidth;
						rawHeight += options.outHeight;
					} else if (alignment == "horizontal") {
						rawWidth += options.outWidth;
						rawHeight = rawHeight > options.outHeight ? rawHeight : options.outHeight;
					} else {
						throw new IllegalArgumentException("Unknown alignment "	+ alignment);
					}
				}

				// scales are needed for scaling button panels
				scaleX = (float) (metrics.widthPixels) / (float) rawWidth;
				scaleY = ((float) (metrics.heightPixels) / (float) rawHeight);

				if (rawWidth == 0 || rawHeight == 0) {
					// scale to fit x and y axis
					sizeX = metrics.widthPixels;
					sizeY = metrics.heightPixels;
				} else {
					if (screen.isScrollable()) {
						// scale that image fits x axis (y variable)
						sizeX = metrics.widthPixels;
						sizeY = (int) (rawHeight * scaleX);
					} else {
						// scale to fit x and y axis
						sizeX = metrics.widthPixels;
						sizeY = metrics.heightPixels;

					}
				}
	}
	
	@Override
	protected void onDraw(Canvas c) {
		if(bitmapDrawable == null || bitmapDrawable.getBitmap().isRecycled()){
//			Bitmap bitmap = Bitmap.createBitmap(sizeX, sizeY, Bitmap.Config.ARGB_8888);
			Bitmap bitmap = BitmapGenerator.createBitmap(sizeX, sizeY);
			Canvas canvas = new Canvas(bitmap);
			// load images
			Bitmap[] images = new Bitmap[screen.getImages().size()];

			for (int i = 0; i < screen.getImages().size(); i++) {
				// load image from file
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPurgeable = true;
				options.inInputShareable = true;
				options.inTempStorage=new byte[32 * 1024];
				options.inDither = false;
				images[i] = BitmapFactory.decodeFile(ConvenienceMethods.IMAGE_DIR + screen.getImages().get(i), options);
			}

			if (rawWidth == 0 || rawHeight == 0) {
				bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeResource(res, R.drawable.noimg));
			} else {

				// paint all part images into combined image
				int x = 0, y = 0;

				for (int i = 0; i < images.length; i++) {

					if (images[i] != null) {
						BitmapDrawable bitmapDrawable = new BitmapDrawable(images[i]);
						float scale = (float) metrics.heightPixels / (float) images[i].getHeight();
						int height = (int)(images[i].getHeight() * scaleX);
						int width = (int)(images[i].getWidth() * scaleX);
						if (!screen.isScrollable()) {
							height = sizeY;
						}
						bitmapDrawable.setBounds(x, y, x + width, y + height);
						bitmapDrawable.draw(canvas);

						if (alignment == "vertical") {
							y += bitmapDrawable.getBounds().height();
						} else {
							x += bitmapDrawable.getBounds().width();
						}

						images[i].recycle();
					}
				}
				bitmapDrawable = new BitmapDrawable(bitmap);
				bitmapDrawable.setBounds(0, 0, sizeX, sizeY);
			}
			
		}
		bitmapDrawable.draw(c);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		

		setMeasuredDimension(sizeX, sizeY);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		if(bitmapDrawable != null){
			if(bitmapDrawable.getBitmap() != null){
				bitmapDrawable.getBitmap().recycle();
			}
			bitmapDrawable = null;
		}
		super.onDetachedFromWindow();
	}

	public float getScaleX() {
		return scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}
	
	private static class BitmapGenerator{
		private static final int CACHE_SIZE = 8;
		private static final Bitmap bitmaps[] = new Bitmap[CACHE_SIZE];
		private static int counter = 0;
		
		private static Bitmap createBitmap(int width, int height){
			counter++;
			Log.i("BitmapGenerator", "creating Bitmap number " + counter);
			Bitmap bitmap = bitmaps[counter % CACHE_SIZE];
			if(bitmap != null){
				Log.i("BitmapGenerator", "recycling Bitmap");
				bitmap.recycle();
			}
			try{
				bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			} catch(OutOfMemoryError e){
				Log.w("BitmapGenerator", "Heap limit reached: clearing Cache...");
				for(Bitmap b : bitmaps){
					if(b != null){
						b.recycle();
					}
				}
				bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			}
			bitmaps[counter % CACHE_SIZE] = bitmap;
			
			return bitmap;
		}
		
	}

}
