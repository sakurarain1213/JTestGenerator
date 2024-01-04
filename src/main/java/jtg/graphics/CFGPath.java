package jtg.graphics;

import fj.P;
import soot.coffi.CFG;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CFGPath implements Iterable<CFGNode> {
    private final List<CFGNode> nodeList;
    public CFGPath() {
        nodeList = new ArrayList<>();
    }
    public void push(CFGNode node) {
        nodeList.add(node);
    }
    public void pop() {
        nodeList.remove(nodeList.size() - 1);
    }
    public CFGNode getNode(int index) {
        return nodeList.get(index);
    }
    public int size() {
        return nodeList.size();
    }
    @Override
    public Iterator<CFGNode> iterator() {
        return nodeList.iterator();
    }
    public CFGPath multiply(CFGPath path) {
        CFGPath newPath = new CFGPath();
        for (CFGNode node : this) {
            newPath.push(node);
        }
        for (CFGNode node : path) {
            newPath.push(node);
        }
        return newPath;
    }
    @Override
    public CFGPath clone() {
        CFGPath clone = new CFGPath();
        for (CFGNode node : this) {
            clone.push(node);
        }
        return clone;
    }
}
