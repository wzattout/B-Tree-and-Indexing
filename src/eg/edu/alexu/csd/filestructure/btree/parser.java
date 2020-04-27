package eg.edu.alexu.csd.filestructure.btree;

import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class parser {

    public ArrayList<Pair<String, Integer>> parse(String filePath) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<Pair<String, Integer>> content = new ArrayList<>();
        File xmlFile = new File(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("doc");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;
                int id = Integer.parseInt(elem.getAttribute("id"));
                String text = nNode.getTextContent();
                String[] words = text.split("[ \n,]");
                for (String word : words) {
                    if (!word.equals(""))
                        content.add(new Pair<>(word.toLowerCase(), id));
                }
            }
        }
        return content;
    }

    /*public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        parser x = new parser();
        ArrayList<Pair<String, String>> y = x.parse("res\\wiki_00");
        for (Pair<String, String> z : y) {
            System.out.println(z.getValue() + "\n" + z.getKey() + "\n" + "----------------");
        }
    }*/
}
