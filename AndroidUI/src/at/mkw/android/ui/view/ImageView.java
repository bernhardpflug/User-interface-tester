package at.mkw.android.ui.view;

import model.ModelUtils;
import model.Screen;
import model.Window;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import at.mkw.android.ui.image.ImageObject;
import at.mkw.android.ui.image.ImagePool;

public class ImageView extends android.widget.ImageView {

	protected Screen screen;
	protected ImageObject imageObject;

	protected DisplayMetrics metrics;
	
	protected Resources res;
	
	public ImageView(Context context, Window window) {

		super(context);

		this.screen = ModelUtils.getActiveScreen(window);

		setAdjustViewBounds(true);
		setScaleType(ScaleType.CENTER);
		
		metrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		res = getContext().getResources();

	}

	public void onDisplay() {
		
		imageObject = ImagePool.loadImages(screen, res, metrics,screen.isScrollable());
		
		setImageBitmap(imageObject.image);
	}
	
	public void onDisappear() {
		imageObject.image.recycle();
		imageObject = null;
		
		if (this.getDrawable() != null) {
			((BitmapDrawable)this.getDrawable()).getBitmap().recycle();
			this.getDrawable().setCallback(null);
			this.setImageDrawable(null);
		}
		setImageBitmap(null);
	}
	
	public Bitmap paint() {
		return getScreenImage();
	}

	public Bitmap getScreenImage() {
		return imageObject.image;
	}

	public float getScaleX() {
		return imageObject.xScale;
	}

	public float getScaleY() {
		return imageObject.yScale;
	}

	public Screen getScreen() {
		return screen;
	}
}
