package eg.edu.alexu.csd.filestructure.btree;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

public class parser {

    public ArrayList<String> parse(String fileName) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<String> content = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder loader = factory.newDocumentBuilder();
        Document document = loader.parse(String.valueOf(getClass().getClassLoader().getResource(fileName)));
        DocumentTraversal trav = (DocumentTraversal) document;
        NodeIterator it = trav.createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);
        for (Node node = it.nextNode(); node != null; node = it.nextNode()) {
            String text = node.getTextContent();
            content.add(text);
        }
        return content;
    }
}
