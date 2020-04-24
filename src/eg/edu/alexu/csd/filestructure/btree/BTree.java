package eg.edu.alexu.csd.filestructure.btree;

import javax.management.RuntimeErrorException;
import java.util.List;

public class BTree<K extends Comparable<K>, V> implements IBTree<K, V> {
    int t;
    IBTreeNode<K, V> root;
    public BTree(int t){
        this.t=t;
    }
    @Override
    public int getMinimumDegree() {
        return t;
    }

    @Override
    public IBTreeNode<K, V> getRoot() {
        return root;
    }
    //for test
    public void Travers(IBTreeNode<K, V> node){
        List<IBTreeNode<K, V>> children = node.getChildren();
        List<K> keys = node.getKeys();
        List<V> values = node.getValues();
        if(!node.isLeaf())
            Travers(children.get(0));
        for(int i=0;i<keys.size();i++){
            System.out.print(keys.get(i)+" "+values.get(i)+" ");
            if(!node.isLeaf())
                Travers(children.get(i+1));
        }
    }
    @Override
    public void insert(K key, V value) {
        if(key == null || value == null)
            throw new RuntimeException("insertion with null key or value");

        //if tree is empty create root as leaf node
        if(root == null)
            root = new BTreeNode<>(true);

        //if root is full create new root
        if(isFull(root)){
            IBTreeNode<K, V> newRoot=new BTreeNode<>(false);
            newRoot.getChildren().add(root);
            root=newRoot;
            split(root,0);
        }

        //find where node should be inserted
        IBTreeNode<K, V> node=root;
        while ( !node.isLeaf() ){
            List<K>  keys=node.getKeys();
            int i=0;
            while ( i<keys.size() && keys.get(i).compareTo(key) < 0)
                i++;
            //key found so just ignore call
            if(i<keys.size() && keys.get(i).compareTo(key) == 0)
                return;
            //if key not found update node to correct child
            if( isFull(node.getChildren().get(i)) )
                split(node,i);
            else
                node=node.getChildren().get(i);
        }
        //inserting key at leaf node
        List<K>  keys=node.getKeys();
        int i=0;
        while ( i<keys.size() && keys.get(i).compareTo(key) < 0)
            i++;
        //key found so just ignore call
        if(i<keys.size() && keys.get(i).compareTo(key) == 0)
            return;
        //add key and its value at the right position
        keys.add(i,key);
        node.getValues().add(i,value);
        node.setNumOfKeys(node.getNumOfKeys()+1);
    }
    private boolean isFull(IBTreeNode<K, V> node){
        return node.getNumOfKeys() == 2*t-1;
    }
    // split child of parent at index i
    private void split(IBTreeNode<K, V> parent,int i){
        IBTreeNode<K, V> left=parent.getChildren().get(i);
        IBTreeNode<K, V> right = new BTreeNode<>( left.isLeaf() );

        List<K> parentKeys=parent.getKeys();
        List<K> leftKeys=left.getKeys();
        List<K> rightKeys=right.getKeys();

        List<V> parentValues=parent.getValues();
        List<V> leftValues=left.getValues();
        List<V> rightValues=right.getValues();

        List<IBTreeNode<K, V>> parentChildren=parent.getChildren();
        List<IBTreeNode<K, V>> leftChildren=left.getChildren();
        List<IBTreeNode<K, V>> rightChildren=right.getChildren();

        //add the median of left to parent
        parentKeys.add(i,leftKeys.get(t-1));
        parentValues.add(i,leftValues.get(t-1));
        parentChildren.add(i+1,right);
        parent.setNumOfKeys(parent.getNumOfKeys()+1);
        //move last t-1 keys and values
        for(int j=0 ; j< t-1 ;j++){
            rightKeys.add(leftKeys.get(t+j));
            rightValues.add(leftValues.get(t+j));
        }
        //delete last t keys and values
        for(int j=0 ; j< t ;j++){
            leftKeys.remove(leftKeys.size()-1);
            leftValues.remove(leftValues.size()-1);
        }
        right.setNumOfKeys(t-1);
        left.setNumOfKeys(t-1);
        if( !left.isLeaf()){
            //move last t children
            for(int j=0 ; j< t ;j++)
                rightChildren.add(leftChildren.get(t+j));
            //delete last t children
            for(int j=0 ; j< t ;j++)
                leftChildren.remove(leftChildren.size()-1);
        }
    }

    @Override
    public V search(K key) {
        if(key == null)
            throw new RuntimeException("search with null key");
        IBTreeNode<K, V> node=root;
        while (node != null){
            List<K>  keys=node.getKeys();
            int i=0;
            while ( i<keys.size() && keys.get(i).compareTo(key) < 0)
                i++;
            //key found
            if(i<keys.size() && keys.get(i).compareTo(key) == 0)
                return node.getValues().get(i);
            //if key was not found in a leaf node return null
            if(node.isLeaf())
                return null;
            //if key not found update node to correct child
            node=node.getChildren().get(i);
        }
        return null;
    }

    @Override
    public boolean delete(K key) {
        return false;
    }
}
