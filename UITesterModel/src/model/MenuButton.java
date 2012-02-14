package model;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xml.XmlUtils;

public class MenuButton extends ModelElement {

	private ArrayList<MenuItem> menuItems;
	
	public MenuButton() {
		menuItems = new  ArrayList<MenuItem>();
		
	}
	@Override
	public void applyXmlValues(Element elem) {
		
		NodeList nodeList = elem
		.getElementsByTagName(XmlUtils.getXmlName(MenuItem.class));
		
		if (nodeList.getLength() > 0) {
			
			for (int i=0; i < nodeList.getLength(); i++) {
				Element item = (Element)nodeList.item(i);
				
				MenuItem menuItem = new MenuItem();
				menuItem.applyXmlValues(item);
				addMenuItem(menuItem);
			}
		}
		else {
			XmlUtils.warning("MenuButton without menuitems: "+elem.getNodeName());
		}
		
	}
	
	public void addMenuItem(MenuItem item) {
		menuItems.add(item);
	}

}
