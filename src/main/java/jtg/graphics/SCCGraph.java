package jtg.graphics;

import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;

public class SCCGraph extends IGraph<SCCNode> {
    private SubGraph originGraph;
    private int dfnTop;
    private int[] dfn;
    private int[] low;
    private SubNode[] stk;
    private boolean[] ins;
    private int[] bel;
    private int sccCount;
    private int stkTop;
    public SCCGraph(SubGraph originGraph) {
        this.originGraph = originGraph;
        buildSccGraph();
    }
    private void addExternalEdge(SCCNode fromSccNode, SCCNode toSccNode, CFGNode fromCFGNode, CFGNode toCFGNode) {
        fromSccNode.addSucceeds(Triple.of(toSccNode, fromCFGNode, toCFGNode));
        toSccNode.addPrecursors(Triple.of(fromSccNode, fromCFGNode, toCFGNode));
    }
    private void buildSccGraph() {
        dfn = new int[originGraph.size()];
        low = new int[originGraph.size()];
        stk = new SubNode[originGraph.size()];
        ins = new boolean[originGraph.size()];
        bel = new int[originGraph.size()];
        sccCount = 0;
        stkTop = 0;
        dfnTop = 0;
        for (SubNode node : originGraph) {
            if (dfn[node.getIndex()] == 0) {
                tarjan(node);
            }
        }
        for (int i=0; i<sccCount; ++i) {
            addNode(new SCCNode(i));
        }
        for (SubNode node : originGraph) {
            getNode(bel[node.getIndex()]).addInternalNode(node);
        }
        for (SubNode from : originGraph) {
            for (SubNode to : from.getSucceeds()) {
                int fromBel = bel[from.getIndex()];
                int toBel = bel[to.getIndex()];
                if (fromBel == toBel) {
                    getNode(fromBel).addInternalEdge(from, to);
                } else {
                    addExternalEdge(getNode(fromBel), getNode(toBel), from.getOriginNode(), to.getOriginNode());
                }
            }
        }
    }
    private void tarjan(SubNode node) {
        int index = node.getIndex();
        low[index] = dfn[index] = ++dfnTop;
        stk[stkTop++] = node;
        ins[index] = true;
        for (SubNode t : node.getSucceeds()) {
            if (dfn[t.getIndex()] == 0) {
                tarjan(t);
                low[index] = Math.min(low[index], low[t.getIndex()]);
            }
            if (ins[t.getIndex()]) {
                low[index] = Math.min(low[index], dfn[t.getIndex()]);
            }
        }
        if (low[index] >= dfn[index]) {
            SubNode cur;
            do {
                cur = stk[stkTop - 1];
                ins[cur.getIndex()] = false;
                stkTop--;
                bel[cur.getIndex()] = sccCount;
            } while (cur != node);
            sccCount++;
        }
    }
}
