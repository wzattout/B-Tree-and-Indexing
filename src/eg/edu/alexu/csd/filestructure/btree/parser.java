package eg.edu.alexu.csd.filestructure.btree;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class parser {

    public ArrayList<Pair<String, String>> parse(String fileName) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<Pair<String, String>> content = new ArrayList<>();
        File xmlFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("doc");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;
                String id = elem.getAttribute("id"), text = nNode.getTextContent();
                content.add(new Pair<>(id, text));
            }
        }
        return content;
    }
}
