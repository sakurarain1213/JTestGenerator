package jtg.graphics;

import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;

public class SCCNode extends INode<Triple<SCCNode, CFGNode, CFGNode>>{
    private int sccIndex;
    private SubGraph subGraph;
    public SCCNode(int index) {
        this.sccIndex = index;
        subGraph = new SubGraph();
    }
    public SubGraph getSubGraph() {
        return subGraph;
    }
    public void addInternalNode(SubNode node) {
        subGraph.addInternalNode(node.getOriginNode());
    }
    public void addInternalEdge(SubNode from, SubNode to) {
        subGraph.addInternalEdge(from.getOriginNode(), to.getOriginNode());
    }
    @Override
    public int getIndex() {
        return sccIndex;
    }
}
