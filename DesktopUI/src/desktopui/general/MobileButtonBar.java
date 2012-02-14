package desktopui.general;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.ModelUtils;
import model.Screen;
import model.Slider;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;

import desktopui.event.ButtonEvent;
import desktopui.event.ButtonListener;

@SuppressWarnings("serial")
public class MobileButtonBar extends JXPanel implements ActionListener {

	private static final String SLIDER_LEFT = "slide left";
	private static final String SLIDER_RIGHT = "slide right";
	private static final String MENU = "menu";
	private static final String BACK = "back";

	private Screen screen;
	private boolean debug = Launcher.DEBUG_LINKS;
	private ButtonListener buttonListener;

	JXButton sliderLeft, sliderRight, menu, back;

	public MobileButtonBar() {
		setLayout(new FlowLayout());

		sliderLeft = new MobileButton("/imgs/sliderLeft_enable.png","/imgs/sliderLeft.png");
		sliderRight = new MobileButton("/imgs/sliderRight_enable.png","/imgs/sliderRight.png");
		menu = new MobileButton("/imgs/menu_enable.png","/imgs/menu.png");
		back = new MobileButton("/imgs/back_enable.png","/imgs/back.png");
		
		sliderLeft.setName(SLIDER_LEFT);
		sliderRight.setName(SLIDER_RIGHT);
		menu.setName(MENU);
		back.setName(BACK);
		
		sliderLeft.addActionListener(this);
		sliderRight.addActionListener(this);
		menu.addActionListener(this);
		back.addActionListener(this);
		
		sliderLeft.setFocusable(false);
		sliderRight.setFocusable(false);
		menu.setFocusable(false);
		back.setFocusable(false);
		
		this.add(sliderLeft);
		this.add(sliderRight);
//		this.add(menu);
		this.add(back);

		applyBehaviour(null);
	}
	
	public void applyBehaviour(Screen screen) {
		this.screen = screen;
		
		applyBehaviour();
	}
	
	public void setDebugMode(boolean debug) {
		this.debug = debug;
		
		applyBehaviour();
	}

	private void applyBehaviour() {

		if (screen == null) {
			sliderLeft.setEnabled(false);
			sliderRight.setEnabled(false);
			menu.setEnabled(false);
			back.setEnabled(false);

		} else if (!debug) {
			sliderLeft.setEnabled(true);
			sliderRight.setEnabled(true);
			menu.setEnabled(true);
			back.setEnabled(true);
		}
		
		else {
			
			Slider slider = ModelUtils.getSlider(screen);

			// slider
			if (slider == null) {
				sliderLeft.setEnabled(false);
				sliderRight.setEnabled(false);
			} else {
				Screen leftScreen = slider.getLeftScreen();
				Screen rightScreen = slider.getRightScreen();

				if (leftScreen != null) {
					sliderLeft.setEnabled(true);
				} else {
					sliderLeft.setEnabled(false);
				}

				if (rightScreen != null) {
					sliderRight.setEnabled(true);
				} else {
					sliderRight.setEnabled(false);
				}
			}

			// back
			if (screen.getBackButtonTarget() != null) {
				back.setEnabled(true);
			} else {
				back.setEnabled(false);
			}

			// menu
			menu.setEnabled(false);
		}
	}

	public void actionPerformed(ActionEvent event) {

		if (screen != null) {
			JXButton source = (JXButton) event.getSource();

			if (source.getName().equals(SLIDER_LEFT)) {
				Slider slider = ModelUtils.getSlider(screen);
				
				if (slider != null&& slider.getLeftScreen() != null) {
					buttonListener.actionPerformed(new ButtonEvent(slider.getLeftScreen().getFullId()));
				}
			}
			else if (source.getName().equals(SLIDER_RIGHT)) {
				Slider slider = ModelUtils.getSlider(screen);
				
				if (slider != null && slider.getRightScreen() != null) {
					buttonListener.actionPerformed(new ButtonEvent(slider.getRightScreen().getFullId()));
				}
			}
			else if (source.getName().equals(BACK)) {
				
				if (screen.getBackButtonTarget() != null) {
					buttonListener.actionPerformed(new ButtonEvent(screen.getBackButtonTarget()));
				}
			}
		}

	}

	public void setButtonListener(ButtonListener buttonListener) {
		this.buttonListener = buttonListener;
	}

	public ButtonListener getButtonListener() {
		return buttonListener;
	}
}
