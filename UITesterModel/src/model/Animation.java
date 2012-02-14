package model;

import org.w3c.dom.Element;

public class Animation extends ModelElement {

	private static final String DURATION = "duration";
	private static final String STARTDELAY = "startDelay";
	private static final String ACCELERATION = "acceleration";
	private static final String DECELERATION = "deceleration";
	int duration;
	int startDelay;
	float acceleration;
	float deceleration;

	@Override
	public void applyXmlValues(Element elem) {
		setDuration(Integer.parseInt(elem.getAttribute(DURATION)));
		
		if (elem.getAttribute(STARTDELAY).length() >0) {
			setStartDelay(Integer.parseInt(elem.getAttribute(STARTDELAY)));
		}
		
		if (elem.getAttribute(ACCELERATION).length() >0) {
			setAcceleration(Float.parseFloat(elem.getAttribute(ACCELERATION)));
		}
		
		if (elem.getAttribute(DECELERATION).length() >0) {
			setDeceleration(Float.parseFloat(elem.getAttribute(DECELERATION)));
		}
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getStartDelay() {
		return startDelay;
	}

	public void setStartDelay(int startDelay) {
		this.startDelay = startDelay;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	public float getDeceleration() {
		return deceleration;
	}

	public void setDeceleration(float deceleration) {
		this.deceleration = deceleration;
	}
}
