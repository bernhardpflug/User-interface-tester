package desktopui.general;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import model.Button;
import model.Button.KlickDuration;
import desktopui.event.ButtonEvent;
import desktopui.event.ButtonListener;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel {

	final Button button;
	private ButtonListener listener;

	public ButtonPanel(final Button button) {
		this.button = button;

		setLayout(null);

		setBounds(button.getBounds().getX(), button.getBounds().getY(), button.getBounds().getWidth(), button.getBounds().getHeight());

		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 1 && button.getTarget(Button.KlickDuration.SINGLE) != null) {
					listener.actionPerformed(new ButtonEvent(button.getTarget(Button.KlickDuration.SINGLE)));
				}

				if (e.getClickCount() == 2 && button.getTarget(Button.KlickDuration.DOUBLE) != null) {
					listener.actionPerformed(new ButtonEvent(button.getTarget(Button.KlickDuration.DOUBLE)));
				}

			}
		});
		
		DragListener dragListener = new DragListener(this);
		addMouseListener(dragListener);
		addMouseMotionListener(dragListener);
	}

	private final static float dash1[] = { 10.0f };
	private final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
	private final static int resizeCorner = 15;

	public void paintComponent(Graphics g) {

		if (Launcher.DEBUG_LINKS) {
			Graphics2D g2 = (Graphics2D) g.create();

			if (Launcher.LINK_TEXT && (button.getTarget(Button.KlickDuration.SINGLE) != null || button.getTarget(Button.KlickDuration.DOUBLE) != null)) {
				g2.setFont(new Font("Arial", Font.BOLD, 10));
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				Iterator<Entry<KlickDuration, String>> klicks = button.getKlicks();
				int counter=0;
				
				while (klicks.hasNext()) {
					Entry<KlickDuration, String> next = klicks.next();
					
					// Containing rect
					Composite oldComp = g2.getComposite();
					g2.setComposite(AlphaComposite.SrcOver.derive(0.5f));
					g2.setColor(Color.RED);
					g2.fillRect(0, 30 * counter, getWidth(), 30);
					g2.setComposite(oldComp);
					
					// text - dark shadow
					g2.setColor(Color.black);
					g2.drawString("Type " + next.getKey(), 4, 30 * counter + 11);
					g2.drawString(next.getValue().substring(next.getValue().lastIndexOf(".") + 1), 4, 22 + 30 * counter);

					// text - light
					g2.setColor(Color.WHITE);
					g2.drawString("Type " + next.getKey(), 3, 30 * counter + 12);
					g2.drawString(next.getValue().substring(next.getValue().lastIndexOf(".") + 1), 3, 23 + 30 * counter);
					
					counter++;
				}
			}
			g2.setColor(Color.RED);

			// draw dashed stroke to indicate modifyability of links
			if (Launcher.MODIFY_LINKS) {

				// resize corner
				g2.drawRect((int) (getWidth() - resizeCorner), (int) (getHeight() - resizeCorner), getWidth() - 1, getHeight() - 1);

				g2.setStroke(dashed);
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			}

			// border
			g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

			g2.dispose();
		}
	}

	public ButtonListener getButtonListener() {
		return listener;
	}

	public void setButtonListener(ButtonListener listener) {
		this.listener = listener;
	}
	
	private class DragListener extends MouseInputAdapter {

		ButtonPanel parent;
		Rectangle initBounds;
		int startX, startY;
		boolean resizeFlag = false;

		public DragListener(ButtonPanel parent) {
			this.parent = parent;
		}

		public void mousePressed(MouseEvent e) {

			if (Launcher.DEBUG_LINKS && Launcher.MODIFY_LINKS) {
				startX = e.getXOnScreen();
				startY = e.getYOnScreen();
				initBounds = parent.getBounds();

				// bottom right corner -> resize
				if (e.getX() > parent.getWidth() - resizeCorner
						&& e.getY() > parent.getHeight() - resizeCorner) {
					resizeFlag = true;
				} else {
					resizeFlag = false;
				}
			}
		}

		public void mouseDragged(MouseEvent e) {
			updateLocation(e);
		}

		public void mouseReleased(MouseEvent e) {
			updateLocation(e);
			
			if (Launcher.DEBUG_LINKS && Launcher.MODIFY_LINKS) {
				System.out.println("<bounds x=\""+parent.getX()+"\" y=\""+parent.getY()+"\" w=\""+parent.getWidth()+"\" h=\""+parent.getHeight()+"\" />");
			}
		}

		void updateLocation(MouseEvent e) {

			if (Launcher.DEBUG_LINKS && Launcher.MODIFY_LINKS) {
				int deltaX = e.getXOnScreen() - startX;
				int deltaY = e.getYOnScreen() - startY;

				if (!resizeFlag) {
					parent.setLocation(initBounds.x + deltaX, initBounds.y
							+ deltaY);
				} else {
					parent.setSize(initBounds.width + deltaX, initBounds.height
							+ deltaY);
				}

				listener.requestRepaint();
			}
		}
	}
}
