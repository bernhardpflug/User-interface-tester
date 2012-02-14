package desktopui.general;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import model.Window;
import model.effect.Effect.Occurance;
import desktopui.effect.Effect;
import desktopui.effect.Effect.Rendertype;

@SuppressWarnings("serial")
public class EffectImagePanel extends ImagePanel {

	ArrayList<Effect> preEffects,postEffects;
	
	public EffectImagePanel(Window window) {
		
		super(window);
		
		preEffects = new ArrayList<Effect>();
		postEffects = new ArrayList<Effect>();
		
		createEffects();
	}
	
	@Override
	public void paint(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g.create();
		
		//paint all pre effects
		for (Effect preEffect : preEffects) {
			Graphics2D preG = (Graphics2D)g2.create();
			preEffect.paint(preG);
			preG.dispose();
		}
		//paint background
		super.paint(g2);
		
		//paint all post effects
		for (Effect postEffect : postEffects) {
			Graphics2D preG = (Graphics2D)g2.create();
			postEffect.paint(preG);
			preG.dispose();
		}
		
		g2.dispose();
	}
	
	@Override
	public void onDisplay() {
		
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
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		for (Effect preEffect : preEffects) {
			preEffect.destroy();
		}
		
		for (Effect postEffect : postEffects) {
			postEffect.destroy();
		}
	}
	
	private void createEffects() {
		
		for (model.effect.Effect modelEffect : screen.getEffects()) {
			Effect effect = Effect.getEffect(this,modelEffect);
			
			if (effect.getRendertype().equals(Rendertype.PRE_BG)) {
				preEffects.add(effect);
			}
			else if (effect.getRendertype().equals(Rendertype.POST_BG)) {
				postEffects.add(effect);
			}
			else {
				throw new RuntimeException("unknown rendertype");
			}
		}
	}
}
