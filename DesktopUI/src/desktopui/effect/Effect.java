package desktopui.effect;

import java.awt.Graphics2D;

import model.Animation;

import org.jdesktop.animation.timing.Animator;

import desktopui.general.ImagePanel;

public abstract class Effect {

	public enum State {
		RUNNING,
		IDLE
	}
	public enum Rendertype {
		PRE_BG, POST_BG
	}
	
	protected State currentState;
	protected ImagePanel parent;
	protected model.effect.Effect modelEffect;

	public abstract void paint(Graphics2D g2);
	
	public abstract void onDisplay();
	
	public abstract void destroy();

	public abstract Rendertype getRendertype();
	
	public Effect(ImagePanel parent, model.effect.Effect modelEffect) {
		this.parent = parent;
		this.setModelEffect(modelEffect);
		currentState = State.IDLE;
	}

	public static Effect getEffect(ImagePanel parent, model.effect.Effect modelEffect) {
		
		if (modelEffect instanceof model.effect.Dialog) {
			return new Dialog(parent,modelEffect);
		}
		
		if (modelEffect instanceof model.effect.InfoObjectAssignment) {
			return new InfoObjectAssignment(parent, modelEffect);
		}
		
		throw new RuntimeException("Undefined or unsupported effect type"+modelEffect.getClass());
	}

	public void setModelEffect(model.effect.Effect modelEffect) {
		this.modelEffect = modelEffect;
	}

	public model.effect.Effect getModelEffect() {
		return modelEffect;
	}
	
	public void setAnimatorProperties(Animator animator, Animation animation) {
		animator.setDuration(animation.getDuration());
		animator.setStartDelay(animation.getStartDelay());
		animator.setAcceleration(animation.getAcceleration());
		animator.setDeceleration(animation.getDeceleration());
	}
}
