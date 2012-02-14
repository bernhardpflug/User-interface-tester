package model;

import org.w3c.dom.Element;

import xml.XmlUtils;

public class Slider extends ScreenCollection {

	public Slider(Window parent) {
		super(parent);

	}

	@Override
	public void applyXmlValues(Element elem) {

		super.applyXmlValues(elem);

	}

	public Window getLeftElement() {

		if (this.getCurrentPosition() > 0) {
			return this.getWindowItems().get(getCurrentPosition() - 1);
		}

		return null;
	}

	public Window getRightElement() {

		if (this.getCurrentPosition() < this.getWindowItems().size()-1) {
			return this.getWindowItems().get(getCurrentPosition() + 1);
		}

		return null;
	}
	
	public Screen getLeftScreen() {
		if (getLeftElement() != null) {
			return ModelUtils.getActiveScreen(getLeftElement());
		}
		return null;
	}
	
	public Screen getRightScreen() {
		if (getRightElement() != null) {
			return ModelUtils.getActiveScreen(getRightElement());
		}
		return null;
	}

	// private int getChildPosition(Window screen) {
	//
	// if (screen.getParent().equals(this)) {
	// return getWindowItems().indexOf(screen);
	// }
	// else {
	// return getChildPosition(screen.getParent());
	// }
	// }
}
