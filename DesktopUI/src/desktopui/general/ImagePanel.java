package desktopui.general;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.Button;
import model.ModelUtils;
import model.Screen;
import model.Window;
import desktopui.event.ButtonEvent;
import desktopui.event.ButtonListener;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel implements ButtonListener{

	protected Screen screen;
	private BufferedImage[] bgImage;
	private int alignment;
	
	private ButtonListener listener;

	public ImagePanel (Window window) {
		
		screen = ModelUtils.getActiveScreen(window);
		
		setLayout(null);
		
		//TODO make this variable
		this.alignment = SwingConstants.VERTICAL;
		
		bgImage = new BufferedImage[screen.getImages().size()];
		
		Dimension size = new Dimension();

		//load images and define overall size
		for (int i = 0; i < bgImage.length; i++) {
			
			bgImage[i] = ImagePool.getImage(screen.getImages().get(i));

			if (bgImage[i] != null) {
				if (alignment == SwingConstants.VERTICAL) {
					size.width = size.width > bgImage[i].getWidth() ? size.width : bgImage[i].getWidth();
					size.height += bgImage[i].getHeight();
				} else if (alignment == SwingConstants.HORIZONTAL) {
					size.width += bgImage[i].getWidth();
					size.height = size.height > bgImage[i].getHeight() ? size.height : bgImage[i].getHeight();
				} else {
					throw new IllegalArgumentException("Unknown SwingConstant " + alignment);
				}
			}
		}

		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		
		//add button panels
		for (Button button : screen.getButtons()) {
			ButtonPanel bPanel = new ButtonPanel(button);
			bPanel.setButtonListener(this);
			this.add(bPanel);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		int x = 0, y = 0;

		for (int i = 0; i < bgImage.length; i++) {

			if (bgImage[i] != null) {
				g2.drawImage(bgImage[i], x, y, null);

				if (alignment == SwingConstants.VERTICAL) {
					y += bgImage[i].getHeight();
				} else {
					x += bgImage[i].getWidth();
				}
			}
		}
		
		if (Launcher.DEBUG_LINKS) {
			g2.setFont(new Font("Arial", Font.BOLD, 14));
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.BLACK);
			g2.drawString("ID "+screen.getFullId(), 6, 16);
			g2.setColor(Color.CYAN);
			g2.drawString("ID "+screen.getFullId(), 5, 15);
		}

		g2.dispose();
	}
	
	public void repaintAll() {
		
		for (int i=0; i < this.getComponentCount(); i++) {
			Component comp = this.getComponent(i);
			
			if (comp instanceof ButtonPanel) {
				comp.repaint();
			}
		}
		
		super.repaint();
	}

	public void actionPerformed(ButtonEvent event) {
		//re-throw event
		listener.actionPerformed(event);
	}
	
	public void requestRepaint() {
		//re-throw event
		listener.requestRepaint();
	}
	
	public void onDisplay() {
		
	}
	
	public void destroy() {
		
		for (int i=0; i < bgImage.length; i++) {
			bgImage[i].flush();
			bgImage[i] = null;
		}
	}

	/**
	 * @param listener the listener to set
	 */
	public void setButtonListener(ButtonListener listener) {
		this.listener = listener;
	}

	/**
	 * @return the listener
	 */
	public ButtonListener getButtonListener() {
		return listener;
	}

	public Screen getScreen() {
		return screen;
	}
}
