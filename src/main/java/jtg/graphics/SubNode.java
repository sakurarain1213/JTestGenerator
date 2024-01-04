package jtg.graphics;

import soot.toolkits.graph.Block;

import java.util.List;

public class SubNode extends INode<SubNode> {
    private final CFGNode originNode;
    private final int subIndex;
    public SubNode(CFGNode node,int index) {
        this.originNode = node;
        this.subIndex = index;
    }
    public CFGNode getOriginNode() {
        return originNode;
    }
    @Override
    public int getIndex() {
        return subIndex;
    }
}
