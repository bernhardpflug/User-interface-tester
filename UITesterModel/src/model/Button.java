package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xml.XmlUtils;

public class Button extends ModelElement {

	private static final String KLICK_VALUE = "klick";
	private static final String TYPE_VALUE = "type";
	private static final String TARGET_VALUE = "target";
	
	private Bounds bounds;
	private HashMap<KlickDuration,String> klicks;
	
	public enum KlickDuration {
		SINGLE, DOUBLE, TWOSEC
	}

	public Button() {
		klicks = new HashMap<KlickDuration,String>();
	}

	@Override
	public void applyXmlValues(Element buttonElem) {

		// check click
		NodeList nodeList = buttonElem.getElementsByTagName(KLICK_VALUE);
		
		for (int i=0; i < nodeList.getLength(); i++) {
			Element klickElem = (Element)nodeList.item(i);
			
			String type = XmlUtils.getFirstElementsValue(klickElem, TYPE_VALUE);
			String target = XmlUtils.getFirstElementsValue(klickElem, TARGET_VALUE);
			
			addKlick(KlickDuration.valueOf(type.toUpperCase()),target);
		}
		
		// check bounds
		Element item = XmlUtils.getFirstElement(buttonElem, XmlUtils.getXmlName(Bounds.class));

		setBounds(new Bounds());
		getBounds().applyXmlValues(item);

	}
	
	public void addKlick(KlickDuration duration,String target) {
		
		if (!klicks.containsKey(duration)) {
			klicks.put(duration, target);
		}
		else {
			throw new RuntimeException("Trying to add two targets "+klicks.get(duration)+" / "+target+"\n for type "+duration);
		}
	}
	
	public Iterator<Entry<KlickDuration,String>> getKlicks() {
		return klicks.entrySet().iterator();
	}
	
	public String getTarget(KlickDuration duration) {
		return klicks.get(duration);
	}

	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}

	/**
	 * @return the bounds
	 */
	public Bounds getBounds() {
		return bounds;
	}
}
