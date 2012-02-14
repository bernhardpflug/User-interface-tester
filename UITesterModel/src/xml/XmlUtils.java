package xml;

import org.w3c.dom.Element;

public class XmlUtils {

	public static String getXmlName(Class myclass) {
		//return  class name with first character lower case
		return myclass.getSimpleName().substring(0, 1).toLowerCase().concat(myclass.getSimpleName().substring(1));
	}
	
	public static boolean equals(Element elem, Class name) {
		return elem.getNodeName().equals(getXmlName(name));
	}

	public static int getElementCount(Element parent, String elementname) {
		return parent.getElementsByTagName(elementname).getLength();
	}
	
	public static String getFirstElementsValue(Element parent, String elementname) {

		return getElementValue(getFirstElement(parent,elementname));
	}
	
	public static Element getFirstElement(Element parent, String elementname) {
		
		if (getElementCount(parent,elementname) > 0) {
			return (Element)parent.getElementsByTagName(elementname).item(0);
		}
		error("Couldn't find "+elementname+" elements in "+parent.getNodeName());
		return null;
	}
	
	public static String getElementValue(Element element) {
		
		if (element.getChildNodes().getLength() > 0) {
			return element.getChildNodes().item(0).getNodeValue();
		}
		return "";
	}
	

	public static void error(String msg) {
		throw new RuntimeException(msg);
	}

	public static void warning(String msg) {
		System.out.println(msg);
	}

}
