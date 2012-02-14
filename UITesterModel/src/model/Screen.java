package model;

import java.util.ArrayList;

import model.effect.Effect;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xml.XmlUtils;

public class Screen extends Window {

	private static final String ID = "id";
	private static final String SCROLLABLE = "scrollable";
	private static final String IMAGE = "image";
	private static final String BACK_BUTTON = "backButton";
	private static final String TARGET = "target";
	
	private boolean scrollable;
	private ArrayList<String> images;
	private ArrayList<Button> buttons;
	private ArrayList<Effect> effects;
	private String backButtonTarget;
	private MenuButton menuButton;

	public Screen(Window parent) {
		super(parent);
		buttons = new ArrayList<Button>(2);
		images = new ArrayList<String>(1);
		effects = new ArrayList<Effect>(1);
	}

	@Override
	public void applyXmlValues(Element elem) {

		// set id
		super.setId(elem.getAttribute(ID));
		
		//scrollable
		if(elem.getAttribute(SCROLLABLE).length() > 0) {
			setScrollable(Boolean.valueOf(elem.getAttribute(SCROLLABLE)).booleanValue());
		}
		else {
			setScrollable(false);
		}

		// images
		NodeList imageList = elem.getElementsByTagName(IMAGE);

		for (int i = 0; i < imageList.getLength(); i++) {
			this.addImage(XmlUtils.getElementValue((Element) imageList.item(i)));
		}

		// effects
		int effectLength = XmlUtils.getElementCount(elem, XmlUtils.getXmlName(Effect.class));

		if (effectLength > 0) {

			NodeList effectList = elem.getElementsByTagName(XmlUtils.getXmlName(Effect.class));

			for (int i = 0; i < effectList.getLength(); i++) {
				addEffect(Effect.getEffect((Element) effectList.item(i)));
			}
		}

		// buttons
		NodeList buttonList = elem.getElementsByTagName(XmlUtils.getXmlName(Button.class));

		if (buttonList.getLength() > 0) {
			for (int i = 0; i < buttonList.getLength(); i++) {
				Button button = new Button();
				Element buttonElem = (Element) buttonList.item(i);
				button.applyXmlValues(buttonElem);
				addButton(button);
			}
		}
		
		// option menu
		int menuButtonLength = XmlUtils.getElementCount(elem, XmlUtils.getXmlName(MenuButton.class));
		if (menuButtonLength > 0) {

			Element menuButtonElem = XmlUtils.getFirstElement(elem, XmlUtils.getXmlName(MenuButton.class));
			menuButton = new MenuButton();
			menuButton.applyXmlValues(menuButtonElem);
		}

		// back button
		int backButtonLength = XmlUtils.getElementCount(elem, BACK_BUTTON);
		if (backButtonLength > 0) {

			Element backButtonElem = XmlUtils.getFirstElement(elem, BACK_BUTTON);
			// get target out of button
			backButtonTarget = XmlUtils.getFirstElementsValue(backButtonElem, TARGET);
		}
	}

	public ArrayList<Button> getButtons() {
		return buttons;
	}

	public void setButtons(ArrayList<Button> buttons) {
		this.buttons = buttons;
	}

	public String getBackButtonTarget() {
		return backButtonTarget;
	}

	public void setBackButtonTarget(String backButtonTarget) {
		this.backButtonTarget = backButtonTarget;
	}

	public void addButton(Button b) {
		buttons.add(b);
	}

	public void addButton(Button b, int index) {
		buttons.add(index, b);
	}

	public void removeButton(Button b) {
		if (buttons == null) {
			return;
		}

		buttons.remove(b);
	}

	public void removeButton(int index) {
		if (buttons == null) {
			return;
		}

		buttons.remove(index);
	}

	public void addEffect(Effect effect) {
		if (effect != null) {
			effects.add(effect);
		} else {
			XmlUtils.error("adding effect is null");
		}
	}

	public ArrayList<Effect> getEffects() {
		return effects;
	}

	public void addImage(String image) {
		if (image.length() == 0) {
			XmlUtils.error("image must not be empty");
			return;
		}
		this.images.add(image);
	}

	public ArrayList<String> getImages() {
		return images;
	}

	/**
	 * @param scrollable the scrollable to set
	 */
	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}

	/**
	 * @return the scrollable
	 */
	public boolean isScrollable() {
		return scrollable;
	}

}
