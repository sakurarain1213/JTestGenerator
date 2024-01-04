package jtg.graphics;

import java.util.ArrayList;
import java.util.List;

public abstract class INode<T>  {
    private List<T> succeeds;
    private List<T> precursors;
    public INode() {
        succeeds = new ArrayList<>();
        precursors = new ArrayList<>();
    }
    public void setSucceeds(List<T> succeeds) {
        this.succeeds = succeeds;
    }
    public void setPrecursors(List<T> precursors) {
        this.precursors = precursors;
    }
    public void addSucceeds(T node) {
        succeeds.add(node);
    }
    public void addPrecursors(T node) {
        precursors.add(node);
    }
    public void removeSucceeds(T node) {
        succeeds.remove(node);
    }
    public void removePrecursors(T node) {
        succeeds.remove(node);
    }
    public List<T> getSucceeds() {
        return succeeds;
    }
    public List<T> getPrecursors() {
        return precursors;
    }
    public abstract int getIndex();
}
