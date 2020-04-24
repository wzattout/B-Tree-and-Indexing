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
            System.out.print(keys.get(i)+" ");
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
    public boolean delete(K key){
        if(key == null)
            throw new RuntimeException("deletion with null key");

        //if tree is empty return false
        if(root == null)
            return false;

        boolean toReturn = deleteAtRoot(key,root);
        if(root.getNumOfKeys() == 0){
            if(root.isLeaf())
                root=null;
            else
                root=root.getChildren().get(0);
        }
        return toReturn;

    }
    private boolean deleteAtRoot(K key,IBTreeNode<K, V> node) {

        //search for key until found or return false
        while (true ){
            List<K>  keys=node.getKeys();
            int i=0;
            while ( i<keys.size() && keys.get(i).compareTo(key) < 0)
                i++;
            //   node with given key found  so break
            if(i<keys.size() && keys.get(i).compareTo(key) == 0)
                break;
            //if key not found update node to correct child
            // if node is leaf so key is not present in tree
            if(node.isLeaf())
                return false;
            //if the child node has t-1 keys some changes have to be done
            List<IBTreeNode<K, V>> parentChildren=node.getChildren();
            IBTreeNode<K, V> child=parentChildren.get(i);
            if( isMinimum(child) ){
                //if child left sibling has more than t-1 keys borrow one key from him
                if( i>0  && !isMinimum(parentChildren.get(i-1)))
                    fromLeftToRight(node,i);
                //if child right sibling has more than t-1 keys borrow one key from him
                else if(i+1 < parentChildren.size() && !isMinimum(parentChildren.get(i+1)) )
                    fromRightToLeft(node,i);
                //merge with left sibling
                else if( i>0 )
                    merge(node,i-1);
                //merge with right sibling
                else
                    merge(node,i);
            }
            else
                node=child;
        }
        // a node with given key has been reached with at least t keys
        //if node is a leaf just delete key and its value
        if(node.isLeaf()){
            int i=0;
            while (node.getKeys().get(i).compareTo(key) != 0)
                i++;
            node.getKeys().remove(i);
            node.getValues().remove(i);
            node.setNumOfKeys(node.getNumOfKeys()-1);
        }
        // need to replace deleted node or merge children
        else{
            int i=0;
            while (node.getKeys().get(i).compareTo(key) != 0)
                i++;
            IBTreeNode<K, V> left = node.getChildren().get(i);
            IBTreeNode<K, V> right = node.getChildren().get(i+1);

            List<K> parentKeys=node.getKeys();
            List<K> leftKeys=left.getKeys();
            List<K> rightKeys=right.getKeys();

            List<V> parentValues=node.getValues();
            List<V> leftValues=left.getValues();
            List<V> rightValues=right.getValues();


            if(!isMinimum(left)){
                //adjust parent ( node )
                parentKeys.remove(i);
                parentValues.remove(i);
                parentKeys.add(i,leftKeys.get(leftKeys.size()-1));
                parentValues.add(i,leftValues.get(leftValues.size()-1));
                //delete the moved key from left recursively
                deleteAtRoot(leftKeys.get(leftKeys.size()-1),left);
            }else if(!isMinimum(right)){
                //adjust parent ( node )
                parentKeys.remove(i);
                parentValues.remove(i);
                parentKeys.add(i,rightKeys.get(0));
                parentValues.add(i,rightValues.get(0));
                //delete the moved key from right recursively
                deleteAtRoot(rightKeys.get(0),right);
            }else {
                //merge left and right
                merge(node,i);
                //recursively delete key from left (merged node)
                deleteAtRoot(key,left);
            }
        }
        return true;
    }
    private void fromRightToLeft(IBTreeNode<K, V> parent,int i){
        IBTreeNode<K, V> left = parent.getChildren().get(i);
        IBTreeNode<K, V> right = parent.getChildren().get(i+1);

        List<K> parentKeys=parent.getKeys();
        List<K> leftKeys=left.getKeys();
        List<K> rightKeys=right.getKeys();

        List<V> parentValues=parent.getValues();
        List<V> leftValues=left.getValues();
        List<V> rightValues=right.getValues();

        List<IBTreeNode<K, V>> parentChildren=parent.getChildren();
        List<IBTreeNode<K, V>> leftChildren=left.getChildren();
        List<IBTreeNode<K, V>> rightChildren=right.getChildren();

        //adjust left
        leftKeys.add(parentKeys.get(i));
        leftValues.add(parentValues.get(i));
        if(!left.isLeaf())
            leftChildren.add(rightChildren.get(0));
        left.setNumOfKeys(left.getNumOfKeys()+1);
        //adjust parent
        parentKeys.remove(i);
        parentValues.remove(i);
        parentKeys.add(i,rightKeys.get(0));
        parentValues.add(i,rightValues.get(0));
        //adjust right
        rightKeys.remove(0);
        rightValues.remove(0);
        if(!right.isLeaf())
            rightChildren.remove(0);
        right.setNumOfKeys(right.getNumOfKeys()-1);
    }
    private void fromLeftToRight(IBTreeNode<K, V> parent,int i){
        IBTreeNode<K, V> right = parent.getChildren().get(i);
        IBTreeNode<K, V> left = parent.getChildren().get(i-1);

        List<K> parentKeys=parent.getKeys();
        List<K> leftKeys=left.getKeys();
        List<K> rightKeys=right.getKeys();

        List<V> parentValues=parent.getValues();
        List<V> leftValues=left.getValues();
        List<V> rightValues=right.getValues();

        List<IBTreeNode<K, V>> parentChildren=parent.getChildren();
        List<IBTreeNode<K, V>> leftChildren=left.getChildren();
        List<IBTreeNode<K, V>> rightChildren=right.getChildren();

        //adjust right
        rightKeys.add(0,parentKeys.get(i-1));
        rightValues.add(0,parentValues.get(i-1));
        if(!right.isLeaf())
            rightChildren.add(0,leftChildren.get(leftChildren.size()-1));
        right.setNumOfKeys(right.getNumOfKeys()+1);
        //adjust parent
        parentKeys.remove(i-1);
        parentValues.remove(i-1);
        parentKeys.add(i-1,leftKeys.get(leftKeys.size()-1));
        parentValues.add(i-1,leftValues.get(leftValues.size()-1));
        //adjust left
        leftKeys.remove(leftKeys.size()-1);
        leftValues.remove(leftValues.size()-1);
        if(!left.isLeaf())
            leftChildren.remove(leftChildren.size()-1);
        left.setNumOfKeys(left.getNumOfKeys()-1);
    }
    private boolean isMinimum(IBTreeNode<K, V> node){
        return node.getNumOfKeys() == t-1;
    }
    private void merge(IBTreeNode<K, V> parent,int i){
        IBTreeNode<K, V> left  = parent.getChildren().get(i);
        IBTreeNode<K, V> right = parent.getChildren().get(i+1);

        List<K> parentKeys=parent.getKeys();
        List<K> leftKeys=left.getKeys();
        List<K> rightKeys=right.getKeys();

        List<V> parentValues=parent.getValues();
        List<V> leftValues=left.getValues();
        List<V> rightValues=right.getValues();

        List<IBTreeNode<K, V>> parentChildren=parent.getChildren();
        List<IBTreeNode<K, V>> leftChildren=left.getChildren();
        List<IBTreeNode<K, V>> rightChildren=right.getChildren();

        //move one element from parent to left
        leftKeys.add(parentKeys.get(i));
        leftValues.add(parentValues.get(i));
        parentKeys.remove(i);
        parentValues.remove(i);
        parentChildren.remove(i+1);
        parent.setNumOfKeys(parent.getNumOfKeys()-1);

        //move t-1 elements from right to left
        leftKeys.addAll(rightKeys);
        leftValues.addAll(rightValues);
        if(!left.isLeaf())
            leftChildren.addAll(rightChildren);
        left.setNumOfKeys(2*t-1);
        //no need to adjust right . It is deleted anyway
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
}
