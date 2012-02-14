package model;

import org.w3c.dom.Element;

import xml.XmlUtils;

public class Bounds extends ModelElement {

	private static final String att_id = "id";
	private static final String att_x = "x";
	private static final String att_y = "y";
	private static final String att_w = "w";
	private static final String att_h = "h";

	private String id;
	private int x, y, width, height;

	public Bounds() {

	}

	public Bounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean contains(int x, int y) {

		if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height) {
			return true;
		}
		return false;
	}

	@Override
	public void applyXmlValues(Element item) {

		if (item.getAttribute(att_id).length() > 0) {
			setId(item.getAttribute(att_id));
		}
		
		setX(Integer.parseInt(item.getAttribute(att_x)));
		setY(Integer.parseInt(item.getAttribute(att_y)));
		setWidth(Integer.parseInt(item.getAttribute(att_w)));
		setHeight(Integer.parseInt(item.getAttribute(att_h)));

	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
