package model.effect;

import org.w3c.dom.Element;

import xml.XmlParsable;
import xml.XmlUtils;

public abstract class Effect implements XmlParsable {

	public enum Occurance {
		ONDISPLAY,
		ONDISAPPEAR
	}
	private static final String OCCURANCE = "occurance";
	
	private Occurance occurance;

	public static Effect getEffect(Element elem) {

		for (int i=0; i < elem.getChildNodes().getLength(); i++) {
			
			String nodeName = elem.getChildNodes().item(i).getNodeName();
			
			if (nodeName.equals(XmlUtils.getXmlName(Dialog.class))) {
				Dialog dialog = new Dialog();
				dialog.applyXmlValues(elem);
				return dialog;
			}
			
			if (nodeName.equals(XmlUtils.getXmlName(InfoObjectAssignment.class))) {
				InfoObjectAssignment assignment = new InfoObjectAssignment();
				assignment.applyXmlValues(elem);
				return assignment;
			}
		}
		
		throw new RuntimeException("Couldn't find effect class for "+elem);
	}
	
	@Override
	public void applyXmlValues(Element elem) {
		
		setOccurance(Occurance.valueOf(elem.getAttribute(OCCURANCE).toUpperCase()));
	}

	public void setOccurance(Occurance occurance) {
		this.occurance = occurance;
	}

	public Occurance getOccurance() {
		return occurance;
	}
	
}
