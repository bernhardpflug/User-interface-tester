package desktopui.effect;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import model.Animation;
import model.ModelUtils;
import model.Screen;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import desktopui.general.ImagePanel;
import desktopui.general.ImagePool;

public class Dialog extends Effect {

	BufferedImage dialogBackImage, blurDialogBackImage;

	private Animator animator;
	private float alpha = 0.0f;

	public Dialog(ImagePanel parent, model.effect.Effect modelEffect) {
		super(parent, modelEffect);
	}

	@Override
	public void paint(Graphics2D g2) {

		if (currentState.equals(State.RUNNING)) {

			if (dialogBackImage != null && alpha < 1.0f) {
				g2.drawImage(dialogBackImage, 0, 0, null);
			}

			if (blurDialogBackImage != null) {
				g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
				g2.drawImage(blurDialogBackImage, 0, 0, null);
			}
		}
	}

	@Override
	public void onDisplay() {

		currentState = State.RUNNING;

		final model.effect.Dialog dialog = (model.effect.Dialog) modelEffect;

		// background is defined as image file
		if (ImagePool.isSupportedImage(dialog.getBackgroundtarget())) {
			dialogBackImage = ImagePool.getImage(dialog.getBackgroundtarget());
		}
		// background is defined as screen
		else {
			Screen dialogback = ModelUtils.getActiveScreen(parent.getScreen().getTarget(dialog.getBackgroundtarget()));

			dialogBackImage = ImagePool.getImage(new ImagePanel(dialogback));
		}

		blurDialogBackImage = ImagePool.getBlurredImage(dialogBackImage);

		if (dialog.getAnimation() != null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {

					Animation anim = dialog.getAnimation();
					animator = PropertySetter.createAnimator(0, Dialog.this, "alpha", 1.0f);
					setAnimatorProperties(animator, anim);
					animator.start();

				}
			});
		} else {
			alpha = 1.0f;
		}

		parent.repaint();
	}
	
	@Override
	public void destroy() {
		dialogBackImage.flush();
		dialogBackImage = null;
		blurDialogBackImage.flush();
		blurDialogBackImage = null;
	}

	@Override
	public Rendertype getRendertype() {
		return Rendertype.PRE_BG;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
		parent.repaint();
	}

	public float getAlpha() {
		return alpha;
	}

}
