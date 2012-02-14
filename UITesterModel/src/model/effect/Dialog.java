package model.effect;

import model.Animation;

import org.w3c.dom.Element;

import xml.XmlUtils;

public class Dialog extends Effect {

	private static final String BACKGROUND = "background";
	
	private String backgroundtarget;
	private Animation animation;

	@Override
	public void applyXmlValues(Element elem) {
		super.applyXmlValues(elem);

		// background
		if (XmlUtils.getElementCount(elem, BACKGROUND) == 1) {
			setBackgroundtarget(XmlUtils.getFirstElementsValue(elem,
					BACKGROUND));
		} else {
			XmlUtils.error("missing background elem in dialog effect: "
					+ elem.getNodeName());
		}

		// animation
		if (XmlUtils
				.getElementCount(elem, XmlUtils.getXmlName(Animation.class)) > 0) {
			
			Element firstElement = XmlUtils.getFirstElement(elem, XmlUtils.getXmlName(Animation.class));
			
			animation = new Animation();
			
			animation.applyXmlValues(firstElement);
		}
	}

	public void setBackgroundtarget(String backgroundtarget) {
		this.backgroundtarget = backgroundtarget;
	}

	public String getBackgroundtarget() {
		return backgroundtarget;
	}
	
	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
}
