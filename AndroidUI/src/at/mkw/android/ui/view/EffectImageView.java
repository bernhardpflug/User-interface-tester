package at.mkw.android.ui.view;

import java.util.ArrayList;

import model.Window;
import model.effect.Effect.Occurance;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import at.mkw.android.effect.Effect;
import at.mkw.android.effect.Effect.Rendertype;
import at.mkw.android.ui.image.ImagePool;

public class EffectImageView extends ImageView {

	private ArrayList<Effect> preEffects, postEffects;
	private Bitmap effectBitmap;

	public EffectImageView(Context context, Window window) {
		super(context, window);
		
		preEffects = new ArrayList<Effect>();
		postEffects = new ArrayList<Effect>();

		createEffects();
	}
	
	@Override
	public void onDisplay() {
		
		imageObject = ImagePool.loadImages(screen,res, metrics,screen.isScrollable());
		
		for (Effect preEffect : preEffects) {
			if (preEffect.getModelEffect().getOccurance().equals(Occurance.ONDISPLAY)) {
				preEffect.onDisplay();
			}
		}
		
		for (Effect postEffect : postEffects) {
			if (postEffect.getModelEffect().getOccurance().equals(Occurance.ONDISPLAY)) {
				postEffect.onDisplay();
			}
		}
		
		effectBitmap = paint();
		setImageBitmap(effectBitmap);
	}
	
	@Override
	public void onDisappear() {
		
		super.onDisappear();
		
		effectBitmap.recycle();
		effectBitmap = null;
		
		for (Effect preEffect : preEffects) {
			preEffect.onDisappear();
		}
		
		for (Effect postEffect : postEffects) {
			postEffect.onDisappear();
		}
	}
	
	@Override
	public Bitmap paint() {
		
		Bitmap bitmap = Bitmap.createBitmap(getScreenImage().getWidth(), getScreenImage().getHeight(), Bitmap.Config.ARGB_8888);
		
		//paint all pre effects
		for (Effect preEffect : preEffects) {
			preEffect.paint(bitmap);
		}
		//paint background
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(imageObject.image, 0, 0, null);
		
		//paint all post effects
		for (Effect postEffect : postEffects) {
			postEffect.paint(bitmap);
		}
		
		return bitmap;
	}

	private void createEffects() {

		for (model.effect.Effect modelEffect : screen.getEffects()) {
			Effect effect = Effect.getEffect(this, modelEffect);

			if (effect.getRendertype().equals(Rendertype.PRE_BG)) {
				preEffects.add(effect);
			} else if (effect.getRendertype().equals(Rendertype.POST_BG)) {
				postEffects.add(effect);
			} else {
				throw new RuntimeException("unknown rendertype");
			}
		}
	}

}
