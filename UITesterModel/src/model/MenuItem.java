package model;

import org.w3c.dom.Element;

import xml.XmlUtils;

public class MenuItem extends ModelElement {
	
	private static final String TARGET = "target";
	private static final String TEXT = "text";
	private static final String ICON = "icon";
	
	private String text;
	private String icon;
	private String target;

	public MenuItem() {

	}

	public MenuItem(String text, String icon, String target) {
		super();
		this.text = text;
		this.icon = icon;
		this.target = target;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public void applyXmlValues(Element menuItemElem) {

		// check text
		text = XmlUtils.getFirstElementsValue(menuItemElem, TEXT);

		// check target
		target = XmlUtils.getFirstElementsValue(menuItemElem, TARGET);
		
		// check icon
		if (XmlUtils.getElementCount(menuItemElem, ICON) > 0) {
			icon = XmlUtils.getFirstElementsValue(menuItemElem, ICON);
		}
	}
}
