package xml;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import model.ModelElement;
import model.Scenario;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlParser {

	public static Scenario parseFile(File xsdFile, File xmlFile)
			throws Exception {

		ArrayList<ModelElement> elements = new ArrayList<ModelElement>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		if (xsdFile != null) {
			validateXml(xsdFile, xmlFile);
		}

		DocumentBuilder builder = factory.newDocumentBuilder();
		Document dom = builder.parse(xmlFile);
		Element szenarioelem = dom.getDocumentElement();

		Scenario szenario = new Scenario();
		szenario.applyXmlValues(szenarioelem);

		return szenario;

	}

	public static void validateXml(File xsdFile, File xmlFile)
			throws Exception {
		// define the type of schema - we use W3C:
		String schemaLang = "http://www.w3.org/2001/XMLSchema";

		// get validation driver:
		SchemaFactory factory = SchemaFactory.newInstance(schemaLang);

		// create schema by reading it from an XSD file:
		Schema schema = factory.newSchema(new StreamSource(xsdFile));
		Validator validator = schema.newValidator();

		// at last perform validation:
		validator.validate(new StreamSource(xmlFile));
	}
}
