package eg.edu.alexu.csd.filestructure.btree;

import java.util.List;

public class BTreeNode<K extends Comparable<K>, V> implements IBTreeNode<K, V> {
    @Override
    public int getNumOfKeys() {
        return 0;
    }

    @Override
    public void setNumOfKeys(int numOfKeys) {

    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public void setLeaf(boolean isLeaf) {

    }

    @Override
    public List<K> getKeys() {
        return null;
    }

    @Override
    public void setKeys(List<K> keys) {

    }

    @Override
    public List<V> getValues() {
        return null;
    }

    @Override
    public void setValues(List<V> values) {

    }

    @Override
    public List<IBTreeNode<K, V>> getChildren() {
        return null;
    }

    @Override
    public void setChildren(List<IBTreeNode<K, V>> children) {

    }
}
