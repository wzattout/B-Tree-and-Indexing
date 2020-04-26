package eg.edu.alexu.csd.filestructure.btree;

import java.util.*;

public class Tst {
    public static void main(String[] args) {
        testDeletion2();
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
    private static void testDeletion2() {
        List<Integer> inp = Arrays.asList(new Integer[]{560 , 994 , 552 , 66 , 598 , 904 , 577 , 800 , 30 , 782 , 445 , 81 , 889 , 950 , 998 , 968 , 602 , 77 , 333 , 971 , 213 , 321 , 794 , 259 , 417 , 110 , 388 , 825 , 774 , 556 , 53 , 186 , 984 , 626 , 783 , 532 , 697 , 950 , 512 , 30 , 394 , 482 , 537 , 859 , 250 , 608 , 227 , 543 , 860 , 862 , 368 , 200 , 942 , 371 , 535 , 982 , 476 , 415 , 128 , 322});
        List<Integer> del = Arrays.asList(new Integer[]{512 , 128 , 577 , 321 , 66 , 322 , 904 , 968 , 394 , 333 , 782 , 81 , 598 , 982 , 602 , 862 , 608 , 552 , 110 , 371});
        BTree<Integer, String> tree = new BTree<>(3);
        for (Integer value : inp)
            tree.insert(value, "Ziad" + value);
        tree.Travers(tree.getRoot());
        System.out.println();
        for (Integer integer : del) {
            System.out.println("I will delete " + integer);
            tree.delete(integer);
            tree.Travers(tree.getRoot());
            System.out.println();
        }
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
