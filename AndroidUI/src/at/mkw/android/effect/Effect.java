package at.mkw.android.effect;

import android.graphics.Bitmap;
import at.mkw.android.ui.view.ImageView;


public abstract class Effect {

	public enum State {
		RUNNING,
		IDLE
	}
	public enum Rendertype {
		PRE_BG, POST_BG
	}
	
	protected State currentState;
	protected ImageView parent;
	protected model.effect.Effect modelEffect;

	public abstract void paint(Bitmap bitmap);
	
	public abstract void onDisplay();
	
	public abstract void onDisappear();

	public abstract Rendertype getRendertype();
	
	public Effect(ImageView parent, model.effect.Effect modelEffect) {
		this.parent = parent;
		this.setModelEffect(modelEffect);
		currentState = State.IDLE;
	}

	public static Effect getEffect(ImageView parent, model.effect.Effect modelEffect) {
		
		if (modelEffect instanceof model.effect.Dialog) {
			return new Dialog(parent,modelEffect);
		}
		
		throw new RuntimeException("Undefined or unsupported effect type"+modelEffect.getClass());
	}

	public void setModelEffect(model.effect.Effect modelEffect) {
		this.modelEffect = modelEffect;
	}

	public model.effect.Effect getModelEffect() {
		return modelEffect;
	}
}
