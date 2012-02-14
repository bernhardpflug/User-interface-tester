package desktopui.effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.SwingUtilities;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;

import desktopui.general.ImagePanel;
import desktopui.general.Launcher;

public class InfoObjectAssignment extends Effect {

	private Rectangle startBounds;
	private Rectangle endBounds;

	private Rectangle currentBounds;

	public InfoObjectAssignment(ImagePanel parent, model.effect.Effect modelEffect) {
		super(parent, modelEffect);
	}

	@Override
	public void paint(Graphics2D g2) {

		if (currentBounds != null && currentBounds.width != endBounds.width) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.GREEN);
			g2.drawRoundRect(currentBounds.x, currentBounds.y, currentBounds.width, currentBounds.height, 10, 10);
		}
		
		if (Launcher.DEBUG_LINKS && startBounds != null && endBounds != null) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g2.setColor(Color.GREEN.darker());
			g2.drawRoundRect(startBounds.x, startBounds.y, startBounds.width, startBounds.height, 10, 10);
			
			g2.setColor(Color.GREEN.brighter());
			g2.drawRoundRect(endBounds.x, endBounds.y, endBounds.width, endBounds.height, 10, 10);
		}
	}

	@Override
	public Rendertype getRendertype() {
		return Rendertype.POST_BG;
	}

	@Override
	public void onDisplay() {

		final model.effect.InfoObjectAssignment assign = (model.effect.InfoObjectAssignment) modelEffect;

		startBounds = new Rectangle(assign.getStart().getX(), assign.getStart().getY(), assign.getStart().getWidth(), assign.getStart().getHeight());

		endBounds = new Rectangle(assign.getEnd().getX(), assign.getEnd().getY(), assign.getEnd().getWidth(), assign.getEnd().getHeight());

		currentBounds = new Rectangle(startBounds);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TimingTarget target = new TimingTarget() {

					public void begin() {
					}

					public void end() {
					}

					public void repeat() {
					}

					public void timingEvent(float fraction) {
						currentBounds.x = (int) (startBounds.x + (endBounds.x - startBounds.x) * fraction);
						currentBounds.y = (int) (startBounds.y + (endBounds.y - startBounds.y) * fraction);
						currentBounds.width = (int) (startBounds.width + (endBounds.width - startBounds.width) * fraction);
						currentBounds.height = (int) (startBounds.height + (endBounds.height - startBounds.height) * fraction);
						parent.repaint();
					}

				};
				
				Animator animator = new Animator(0, target);
				setAnimatorProperties(animator, assign.getAnimation());
				animator.start();
			}
		});
	}
	
	@Override
	public void destroy() {
		
	}
}
