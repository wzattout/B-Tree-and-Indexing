package eg.edu.alexu.csd.filestructure.btree;

import javafx.util.Pair;
import org.xml.sax.SAXException;

import javax.management.RuntimeErrorException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchEngine implements ISearchEngine {
    private int n;
    private IBTree<String, String> engine;
    private parser DOMParser;
    private ArrayList<Pair<String, String>> document;

    public SearchEngine(int n){
        this.n = n;
        engine = new BTree<>(n);
        DOMParser = new parser();
        document = new ArrayList<>();
    }

    @Override
    public void indexWebPage(String filePath) {
        if (filePath == null)
            throw new RuntimeErrorException(new Error());
        try {
            document = DOMParser.parse(filePath);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeErrorException(new Error());
        }
        for (Pair<String, String> stringStringPair : document) {
            engine.insert(stringStringPair.getKey(), stringStringPair.getValue());
        }
    }

    @Override
    public void indexDirectory(String directoryPath) {
        if (directoryPath == null)
            throw new RuntimeErrorException(new Error());
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
        }else {
            indexWebPage(directoryPath);
        }
    }

    @Override
    public void deleteWebPage(String filePath) {
        if (filePath == null)
            throw new RuntimeErrorException(new Error());
        try {
            document = DOMParser.parse(filePath);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeErrorException(new Error());
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
    }*/

    public static void main(String[] args) {
        ISearchEngine x = new SearchEngine(64);
        x.indexDirectory("res\\wiki_00");
        x.deleteWebPage("C:\\Users\\wzatt\\Documents\\Programming\\JAVA\\CSED\\Y2_T2\\Lab3\\B-Tree-and-Indexing\\Wikipedia-Data-Sample\\wiki_00");
    }
}
