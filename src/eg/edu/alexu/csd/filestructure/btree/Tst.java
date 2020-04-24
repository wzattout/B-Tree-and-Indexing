package eg.edu.alexu.csd.filestructure.btree;

import java.util.ArrayList;
import java.util.Random;

public class Tst {
    public static void main(String args[]){
        testInsertion();
    }
    private static void testInsertion(){
        ArrayList<Integer> arr = new ArrayList<>();
        for(int i=0;i<100;i++)
            arr.add(i);
        BTree<Integer,String> tree = new BTree<>(3);
        Random rand = new Random();
        for(int i=0;i<100;i++){
            int r=rand.nextInt(arr.size());
            tree.insert(arr.get(r),"Ziad"+arr.get(r));
            arr.remove(r);
        }
        tree.Travers(tree.getRoot());
    }
}
