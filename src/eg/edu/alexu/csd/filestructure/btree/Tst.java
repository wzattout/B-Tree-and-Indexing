package eg.edu.alexu.csd.filestructure.btree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Tst {
    public static void main(String[] args) {
        testDeletion();
    }

    private static void testInsertion() {
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            arr.add(i);
        BTree<Integer, String> tree = new BTree<>(2);
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            int r = rand.nextInt(arr.size());
            tree.insert(arr.get(r), "Ziad" + arr.get(r));
            arr.remove(r);
        }
        tree.Travers(tree.getRoot());
    }

    private static void testDeletion() {
        int upperBound = 40;
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < upperBound; i++)
            arr.add(i);
        BTree<Integer, String> tree = new BTree<>(4);
        Random rand = new Random();
        for (int i = 0; i < upperBound; i++) {
            int r = rand.nextInt(arr.size());
            tree.insert(arr.get(r), "Ziad" + arr.get(r));
            arr.remove(r);
        }
        tree.Travers(tree.getRoot());
        System.out.println();
        for (int i = 0; i < upperBound; i++)
            arr.add(i);
        ArrayList<Integer> deleted = new ArrayList<>();
        for (int i = 0; i < upperBound / 5; i++) {
            int r = rand.nextInt(arr.size());
            tree.delete(arr.get(r));
            deleted.add(arr.get(r));
            arr.remove(r);
        }
        Collections.sort(deleted);
        for (Integer integer : deleted) System.out.print(integer + " ");
        System.out.println();
        tree.Travers(tree.getRoot());
        System.out.println();

    }
}
