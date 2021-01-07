package br.fritzen.engine.formats.collada;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import br.fritzen.engine.formats.collada.libraries.ColladaGeometry;

public class ColladaLoader {

	private final static Logger LOG = Logger.getLogger(ColladaLoader.class.getName());
	
	
	public ColladaLoader() {
		// TODO Auto-generated constructor stub
	}
	
	
	public ColladaModel load(final String filename) {
		
		LOG.info("Loading collada file: " + filename);
		
		ColladaModel model = new ColladaModel();
		
		File xmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            Element docElement = doc.getDocumentElement();
            
            for (int i = 0; i < docElement.getElementsByTagName("geometry").getLength(); i++) {
            	Node currentGeometry = doc.getDocumentElement().getElementsByTagName("geometry").item(i);
            	model.libraryGeometries.add(new ColladaGeometry(currentGeometry));
            }
                      
            
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }
		
		return model;
	}
	

}
