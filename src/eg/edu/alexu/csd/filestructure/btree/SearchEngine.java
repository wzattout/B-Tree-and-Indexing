package eg.edu.alexu.csd.filestructure.btree;

import javafx.util.Pair;
import org.xml.sax.SAXException;

import javax.management.RuntimeErrorException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SearchEngine implements ISearchEngine {
    private IBTree<String, HashMap<Integer, Integer>> engine;
    private parser DOMParser;
    private ArrayList<Pair<String, Integer>> document;

    public SearchEngine(int t) {
        engine = new BTree<>(t);
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
        for (Pair<String, Integer> stringStringPair : document) {
            HashMap<Integer, Integer> field = engine.search(stringStringPair.getKey());
            if (field == null) {
                HashMap<Integer, Integer> in = new HashMap<>();
                in.put(stringStringPair.getValue(), 1);
                engine.insert(stringStringPair.getKey(), in);
            } else {
                engine.delete(stringStringPair.getKey());
                if (!field.containsKey(stringStringPair.getValue())) {
                    field.put(stringStringPair.getValue(), 1);
                } else {
                    int temp = field.get(stringStringPair.getValue()) + 1;
                    field.remove(stringStringPair.getValue());
                    field.put(stringStringPair.getValue(), temp);
                }
                engine.insert(stringStringPair.getKey(), field);
            }
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
        } else {
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
        for (Pair<String, Integer> stringStringPair : document) {
            HashMap<Integer, Integer> field = engine.search(stringStringPair.getKey());
            if (field == null) {
                continue;
            } else {
                engine.delete(stringStringPair.getKey());
                if (!field.containsKey(stringStringPair.getValue())) {
                    continue;
                } else {
                    int temp = field.get(stringStringPair.getValue()) - 1;
                    field.remove(stringStringPair.getValue());
                    if (temp > 0)
                        field.put(stringStringPair.getValue(), temp);
                    engine.insert(stringStringPair.getKey(), field);
                }
            }
        }
    }

    @Override
    public List<ISearchResult> searchByWordWithRanking(String word) {
        if (word == null)
            throw new RuntimeErrorException(new Error());
        word = word.toLowerCase();
        String[] words = word.split("[ \n]");
        if (words.length > 1)
            throw new RuntimeErrorException(new Error());
        HashMap<Integer, Integer> map = engine.search(word);
        return hashToIResult(map);
    }

    @Override
    public List<ISearchResult> searchByMultipleWordWithRanking(String sentence) {
        if (sentence == null)
            throw new RuntimeErrorException(new Error());
        List<ISearchResult> result = new ArrayList<>();
        String[] words = sentence.split("[ \t]");
        for (String word : words) {
            Collections.sort(result, Comparator.comparingInt(o -> Integer.parseInt(o.getId())));
            if (word.length() <= 0)
                continue;
            List<ISearchResult> temp = searchByWordWithRanking(word);
            Collections.sort(temp, Comparator.comparingInt(o -> Integer.parseInt(o.getId())));
            if (temp.size() == 0) {
                result.clear();
                break;
            }
            int i = 0, j = 0;
            for (; i < result.size() && j < temp.size();){
                if (Integer.parseInt(temp.get(j).getId()) < Integer.parseInt(result.get(i).getId())){
                    j++;
                }else if (Integer.parseInt(temp.get(j).getId()) > Integer.parseInt(result.get(i).getId())){
                    result.remove(i);
                }else {
                    ISearchResult x = new SearchResult("", 0);
                    x = result.get(i);
                    x.setRank(Math.min(result.get(i).getRank(), temp.get(j).getRank()));
                    result.remove(i);
                    result.add(i, x);
                    i++; j++;
                }
            }
            while (i < result.size())
                result.remove(i);
            if (result.size() == 0)
                result.addAll(temp);
        }
        return result;
    }

    List<ISearchResult> hashToIResult(HashMap<Integer, Integer> map) {
        if (map == null)
            return new ArrayList<>();
        List<ISearchResult> result = new ArrayList<>();
        for (Map.Entry mapElement : map.entrySet()) {
            result.add(new SearchResult(mapElement.getKey().toString(), (Integer) mapElement.getValue()));
        }
        return result;
    }

    /*public static void main(String[] args) {
        ISearchEngine x = new SearchEngine(50);
        //x.indexDirectory("res\\test");
        x.indexDirectory("res\\wiki_01");
        x.indexDirectory("res\\test2");
        x.deleteWebPage("res\\test2");
        x.deleteWebPage("res\\test");
        ArrayList<ISearchResult> y = (ArrayList<ISearchResult>) x.searchByMultipleWordWithRanking("addressing path computer");
        for (ISearchResult iSearchResult : y) {
            System.out.println(iSearchResult.getId() + "\n" + iSearchResult.getRank() + "\n" + "---------");
        }
    }*/
}
