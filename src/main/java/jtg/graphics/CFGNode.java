package jtg.graphics;

import soot.coffi.CFG;
import soot.toolkits.graph.Block;

import java.util.List;

public class CFGNode extends INode<CFGNode> {
    private Block block;
    public CFGNode(Block block) {
        this.block = block;
    }
    @Override
    public int getIndex() {
        return block.getIndexInMethod();
    }
}
