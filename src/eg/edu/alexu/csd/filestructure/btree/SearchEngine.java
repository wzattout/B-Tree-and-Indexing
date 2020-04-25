package eg.edu.alexu.csd.filestructure.btree;

import javafx.util.Pair;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchEngine implements ISearchEngine {
    IBTree<String, String> engine = new BTree<>(64);
    parser DOMParser = new parser();
    ArrayList<Pair<String, String>> document = new ArrayList<>();

    @Override
    public void indexWebPage(String filePath) {
        try {
            document = DOMParser.parse(filePath);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        for (Pair<String, String> stringStringPair : document) {
            engine.insert(stringStringPair.getKey(), stringStringPair.getValue());
        }
    }

    @Override
    public void indexDirectory(String directoryPath) {
        File dir = new File(directoryPath);
        File[] firstLevelFiles = dir.listFiles();
        if (firstLevelFiles != null && firstLevelFiles.length > 0) {
            for (File aFile : firstLevelFiles) {
                if (aFile.isDirectory()) {
                    indexDirectory(aFile.getAbsolutePath());
                } else {
                    indexWebPage(aFile.getPath());
                }
            }
        }
    }

    @Override
    public void deleteWebPage(String filePath) {
        try {
            document = DOMParser.parse(filePath);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        for (Pair<String, String> stringStringPair : document) {
            engine.delete(stringStringPair.getKey());
        }
    }

    @Override
    public List<ISearchResult> searchByWordWithRanking(String word) {
        return null;
    }

    @Override
    public List<ISearchResult> searchByMultipleWordWithRanking(String sentence) {
        return null;
    }

    /*int searchRank(String str, String findStr) {
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = str.indexOf(findStr, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return count;
    }

    public static void main(String[] args) {
        ISearchEngine x = new SearchEngine();
        x.indexDirectory("C:\\Users\\wzatt\\Documents\\Programming\\JAVA\\CSED\\Y2_T2\\Lab3\\B-Tree-and-Indexing\\Wikipedia-Data-Sample");
        x.deleteWebPage("C:\\Users\\wzatt\\Documents\\Programming\\JAVA\\CSED\\Y2_T2\\Lab3\\B-Tree-and-Indexing\\Wikipedia-Data-Sample\\wiki_00.xml");
    }*/
}
