package model;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xml.XmlUtils;

public class ScreenCollection extends Window {
	
	private static final String ID = "id";
	private static final String TYPE = "type";
	
	public enum Type {
		SLIDER, XOR
	}

	private int currentPosition;
	private Type type;
	protected ArrayList<Window> collectionItems;

	public ScreenCollection(Window parent) {
		super(parent);

		collectionItems = new ArrayList<Window>();
	}

	public ArrayList<Window> getWindowItems() {
		return collectionItems;
	}

	public void setWindowItems(ArrayList<Window> sliderItems) {
		this.collectionItems = sliderItems;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void addCollectionItem(Screen c) {
		if (collectionItems == null) {
			collectionItems = new ArrayList<Window>();
		}

		collectionItems.add(c);
	}

	public void addCollectionItem(Screen c, int index) {
		if (collectionItems == null) {
			collectionItems = new ArrayList<Window>();
		}

		collectionItems.add(index, c);
	}

	public void removeCollectionItem(Screen c) {
		if (collectionItems == null) {
			return;
		}

		collectionItems.remove(c);
	}

	public void removeCollectionItem(int index) {
		if (collectionItems == null) {
			return;
		}

		collectionItems.remove(index);
	}

	public void setCurrentPosition(int currentPosition) {
		
		if (currentPosition >= 0 && currentPosition < collectionItems.size()) {
			this.currentPosition = currentPosition;
		}
	}

	public int getCurrentPosition() {
		return currentPosition;
	}
	
	public Window getActiveChild() {
		
		if (currentPosition < collectionItems.size()) {
			return collectionItems.get(currentPosition);
		}
		
		return null;
	}
	
	@Override
	public void applyXmlValues(Element collectionElem) {

		// set id
		super.setId(collectionElem.getAttribute(ID));

		NodeList childNodes = collectionElem.getChildNodes();
		
		//set type
		if (collectionElem.getAttribute(TYPE).length() > 0) {
			type = Type.valueOf(collectionElem.getAttribute(TYPE).toUpperCase());
		}

		// get all screens, sliders and screenCollections
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);

			if (item instanceof Element) {
				Element elem = (Element) item;

				if (XmlUtils.equals(elem, Screen.class)) {
					Screen screen = new Screen(this);
					screen.applyXmlValues(elem);
					collectionItems.add(screen);
				}
				else if (XmlUtils.equals(elem, ScreenCollection.class)) {
					ScreenCollection collection = new ScreenCollection(this);
					collection.applyXmlValues(elem);
					collectionItems.add(collection);
				}
				else if (XmlUtils.equals(elem, Slider.class)) {
					Slider slider = new Slider(this);
					slider.applyXmlValues(elem);
					collectionItems.add(slider);
				}
				else {
					XmlUtils.error("Unkown element "+elem.getNodeName());
				}
			}
			else {
				//Ignored item
			}

		}
	}
}