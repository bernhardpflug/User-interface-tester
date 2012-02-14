import static org.junit.Assert.*;

import java.io.File;

import model.ModelUtils;
import model.Scenario;

import org.junit.Test;

import xml.XmlParser;


public class TestModelUtils {

	@Test
	public void Slidertest() throws Exception{
		Scenario scenario = XmlParser.parseFile(new File("src-test/schema.xsd"), new File("src-test/MobileS1.xml"));
		
//		assertTrue(ModelUtils.getSliderElements(scenario.getStartScreen()).size() == 2);
	}
}
