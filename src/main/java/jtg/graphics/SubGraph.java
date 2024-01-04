package jtg.graphics;

import java.util.HashMap;
import java.util.Map;

public class SubGraph extends IGraph<SubNode> {
    private final Map<CFGNode, SubNode> cfgToSubMap;
    private boolean[] vis;
    private CFGPath currentPath;
    private CFGPathGroup paths;
    public SubGraph() {
        cfgToSubMap = new HashMap<>();
    }
    public SubGraph(ControlFlowGraph cfg) {
        this();
        for (CFGNode node : cfg) {
            addInternalNode(node);
        }
        for (CFGNode from : cfg) {
            for (CFGNode to : from.getSucceeds()) {
                addInternalEdge(from, to);
            }
        }
    }
    public SubNode getSubNode(CFGNode node) {
        return cfgToSubMap.get(node);
    }
    public void addInternalNode(CFGNode node) {
        SubNode subNode = new SubNode(node, cfgToSubMap.size());
        cfgToSubMap.put(node, subNode);
        addNode(subNode);
    }
    public void addInternalEdge(CFGNode from, CFGNode to) {
        SubNode subFrom = cfgToSubMap.get(from);
        SubNode subTo = cfgToSubMap.get(to);
        subFrom.addSucceeds(subTo);
        subTo.addPrecursors(subFrom);
    }
    public void removeInternalEdge(SubNode from, SubNode to) {
        from.getSucceeds().remove(to);
        to.getPrecursors().remove(from);
    }
    public CFGPathGroup getPaths(CFGNode s, CFGNode t) {
        return getPaths(cfgToSubMap.get(s), cfgToSubMap.get(t));
    }

    public CFGPathGroup getPaths(SubNode s, SubNode t) {
        vis = new boolean[size()];
        paths = new CFGPathGroup();
        currentPath = new CFGPath();
        currentPath.push(s.getOriginNode());
        dfs(s, t);
        return paths;
    }

    public CFGPathGroup getInnerPaths(CFGNode s) {
        return getInnerPaths(cfgToSubMap.get(s));
    }

    public CFGPathGroup getInnerPaths(SubNode s) {
        vis = new boolean[size()];
        paths = new CFGPathGroup();
        currentPath = new CFGPath();
        dfs2(s);
        return paths;
    }
    private void dfs(SubNode x,SubNode t) {
        if (x == t && currentPath.size() > 1) {
            paths.add(currentPath.clone());
            return;
        }
        if (vis[x.getIndex()]) {
            return;
        }
        vis[x.getIndex()] = true;
        for (SubNode to : x.getSucceeds()) {
            currentPath.push(to.getOriginNode());
            dfs(to, t);
            currentPath.pop();
        }
        vis[x.getIndex()] = false;
    }

    private void dfs2(SubNode x) {
        vis[x.getIndex()] = true;
        currentPath.push(x.getOriginNode());
        for (SubNode to : x.getSucceeds()) {
            if (vis[to.getIndex()]) {
                if (x.getSucceeds().size() == x.getOriginNode().getSucceeds().size()) {
                    paths.add(currentPath.clone());
                }
            } else {
                dfs2(to);
            }
        }
        currentPath.pop();
        vis[x.getIndex()] = false;
    }
}
