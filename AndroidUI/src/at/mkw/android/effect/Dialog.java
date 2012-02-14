package at.mkw.android.effect;

import model.ModelUtils;
import model.Screen;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import at.mkw.android.ui.ConvenienceMethods;
import at.mkw.android.ui.image.ImageObject;
import at.mkw.android.ui.image.ImagePool;
import at.mkw.android.ui.view.ImageView;

public class Dialog extends Effect {

	ImageObject dialogBackground;
	
	public Dialog(ImageView parent, model.effect.Effect modelEffect) {
		super(parent, modelEffect);
	}

	@Override
	public void paint(Bitmap bitmap) {
		Canvas canvas = new Canvas(bitmap);

		if (currentState == State.RUNNING && dialogBackground != null) {
			
			Paint p = new Paint();
			p.setMaskFilter(new BlurMaskFilter(50, Blur.INNER));
			canvas.drawBitmap(dialogBackground.image, 0, 0, p);
		}
	}

	@Override
	public void onDisplay() {

		//get display metrics
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) parent.getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		Resources res = parent.getResources();
		
		final model.effect.Dialog dialog = (model.effect.Dialog) modelEffect;
		
		//get screen defined as background
		model.Window dialogBackgroundWindow = ConvenienceMethods.SZENARIO.getTarget(dialog.getBackgroundtarget());
		Screen dialogBackgroundScreen = ModelUtils.getActiveScreen(dialogBackgroundWindow);
		
		//load image of background depending whether this view is scrollable or not
		dialogBackground = ImagePool.loadImages(dialogBackgroundScreen,res, metrics, parent.getScreen().isScrollable());
		
		currentState = State.RUNNING;
	}
	
	@Override
	public void onDisappear() {
		dialogBackground.image.recycle();
		dialogBackground = null;
	}

	@Override
	public Rendertype getRendertype() {
		return Effect.Rendertype.PRE_BG;
	}
}
