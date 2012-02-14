package model.effect;

import model.Animation;
import model.Bounds;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xml.XmlUtils;

public class InfoObjectAssignment extends Effect {

	Bounds start,end;
	Animation animation;
	
	@Override
	public void applyXmlValues(Element elem) {
		super.applyXmlValues(elem);

		//bounds
		NodeList boundsList = elem.getElementsByTagName(XmlUtils.getXmlName(Bounds.class));
		
		for (int i=0; i < boundsList.getLength(); i++) {
			Element boundsElem = (Element)boundsList.item(i);
			
			Bounds temp = new Bounds();
			temp.applyXmlValues(boundsElem);
			
			if (temp.getId() != null) {
				
				if (temp.getId().equals("start")) {
					start = temp;
				}
				else if (temp.getId().equals("end")) {
					end = temp;
				}
				else {
					throw new RuntimeException("Unknown bounds id "+temp.getId()+" for usage in InfoObjectAssignment effect");
				}
			}
			else {
				throw new RuntimeException("effect requires start and end id for bounds");
			}
			
		}
		
		//animation
		Element animElement = XmlUtils.getFirstElement(elem, XmlUtils.getXmlName(Animation.class));
		animation = new Animation();
		animation.applyXmlValues(animElement);
	}

	public Bounds getStart() {
		return start;
	}

	public void setStart(Bounds start) {
		this.start = start;
	}

	public Bounds getEnd() {
		return end;
	}

	public void setEnd(Bounds end) {
		this.end = end;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
}
