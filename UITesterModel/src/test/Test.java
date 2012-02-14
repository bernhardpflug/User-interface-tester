package test;

import java.io.File;

import model.Scenario;
import xml.XmlParser;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Scenario szenario = XmlParser.parseFile(new File("/Users/Bernhard/Dropbox/Masterarbeit/UITesting/UITesterModel/schema.xsd"), new File("/Users/Bernhard/Dropbox/Masterarbeit/UITesting/DesktopS1/DesktopS1.xml"));
//			Szenario szenario = XmlParser.parseFile(null, new FileInputStream("/Users/Bernhard/Dropbox/Masterarbeit/UITesting/UserInterfaceTester/imgs/desktopS1.xml"));
//			System.out.println(szenario.getTarget("new animal.homescreen"));
			szenario.validateTargets();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
