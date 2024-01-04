package jtg.graphics;

import com.google.common.graph.Graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IGraph<T> implements Iterable<T> {
    private List<T> nodes;
    public IGraph() {
        nodes = new ArrayList<>();
    }
    public void setNodes(List<T> nodes) {
        this.nodes = nodes;
    }
    public void setNode(int index,T node) {
        nodes.set(index, node);
    }
    public void addNode(T node) {
        nodes.add(node);
    }
    public T getNode(int index) {
        return nodes.get(index);
    }
    public int size() {
        return nodes.size();
    }
    @Override
    public Iterator<T> iterator() {
        return nodes.iterator();
    }
}
