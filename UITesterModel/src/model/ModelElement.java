package model;

import xml.XmlParsable;
import xml.XmlUtils;

public abstract class ModelElement implements XmlParsable{

	private String id;
	
	public ModelElement() {
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id.length()==0) {
			XmlUtils.error("id must not be empty");
			return;
		}
		this.id = id;
	}
}
