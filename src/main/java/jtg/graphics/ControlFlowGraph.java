package jtg.graphics;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
// 因为UnitGraph只读   现在反编译class文件  重构其中的方法   主要是添加一个基路径计算方法
//

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.graph.Graph;
import org.apache.commons.lang3.tuple.Triple;
import soot.Unit;
import soot.jimple.toolkits.thread.mhp.SCC;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.Body;


//利用blockGraph 构建块图
public class ControlFlowGraph extends IGraph<CFGNode> {

    private String classPath;
    private String className;
    private String methodName;

    private CFGPathGroup primePaths;

    public ControlFlowGraph(String classPath, String className, String methodName) {
        this.classPath = classPath;
        this.className = className;
        this.methodName = methodName;
        Body body = SootCFG.getMethodBody(classPath, className, methodName);
        BlockGraph blockGraph = SootCFG.getSimpleCFG(body);
        analyzeBlockGraph(blockGraph);
    }

    private void analyzeBlockGraph(BlockGraph blockGraph) {
        System.out.println(blockGraph.size());
        List<CFGNode> nodes = new ArrayList<>(blockGraph.size());
        for (int i = 0; i < blockGraph.size(); ++i) {
            nodes.add(null);
        }
        for (Block block : blockGraph) {
            nodes.set(block.getIndexInMethod(),new CFGNode(block));
        }
        for (Block block : blockGraph) {
            List<Block> succeeds = block.getSuccs();
            List<CFGNode> nodeSucceeds = new ArrayList<>();
            for (Block b : succeeds) {
                nodeSucceeds.add(nodes.get(b.getIndexInMethod()));
            }
            nodes.get(block.getIndexInMethod()).setSucceeds(nodeSucceeds);
        }
        for (Block block : blockGraph) {
            List<Block> precursors = block.getPreds();
            List<CFGNode> nodePrecursors = new ArrayList<>();
            for (Block b : precursors) {
                nodePrecursors.add(nodes.get(b.getIndexInMethod()));
            }
            nodes.get(block.getIndexInMethod()).setPrecursors(nodePrecursors);
        }
        setNodes(nodes);
    }

    private void dfs(SCCNode sccNode, CFGNode startNode, CFGPathGroup prevPaths, CFGPathGroup savedPaths) {
        if (sccNode.getSucceeds().size() == 0) {
            CFGPath path = new CFGPath();
            path.push(startNode);
            savedPaths.add(prevPaths.multiply(path));
        } else {
            for (Triple<SCCNode, CFGNode, CFGNode> edge : sccNode.getSucceeds()) {
                SCCNode nextScc = edge.getLeft();
                CFGNode edgeFrom = edge.getMiddle();
                CFGNode edgeTo = edge.getRight();
                CFGPathGroup nextPaths = new CFGPathGroup();
                if (startNode != null) {
                    CFGPathGroup sccPaths;
                    if (startNode == edgeFrom) {
                        sccPaths = new CFGPathGroup();
                        CFGPath path = new CFGPath();
                        path.push(startNode);
                        sccPaths.add(path);
                    } else {
                        sccPaths = sccNode.getSubGraph().getPaths(startNode, edgeFrom);
                    }
                    nextPaths.add(prevPaths.multiply(sccPaths));
                    savedPaths.add(prevPaths.multiply(sccNode.getSubGraph().getInnerPaths(startNode)));
                } else {
                    if (sccNode.getSubGraph().size() == 1) {
                        CFGPath path = new CFGPath();
                        path.push(sccNode.getSubGraph().getNode(0).getOriginNode());
                        nextPaths.add(path);
                    }
                }
                if (sccNode.getSubGraph().size() > 1) {
                    SubGraph subGraph = sccNode.getSubGraph();
                    SubNode exitNodeSucceed = subGraph.getSubNode(edgeFrom).getSucceeds().get(0);
                    subGraph.removeInternalEdge(subGraph.getSubNode(edgeFrom), exitNodeSucceed);
                    nextPaths.add(getDirectPrimePaths(new SCCGraph(subGraph)));
                    subGraph.addInternalEdge(edgeFrom, exitNodeSucceed.getOriginNode());
                }
                dfs(nextScc, edgeTo, nextPaths, savedPaths);
            }
        }
    }
    private CFGPathGroup getDirectPrimePaths(SCCGraph graph) {
        CFGPathGroup paths = new CFGPathGroup();
        for (SCCNode node : graph) {
            if (node.getPrecursors().size() == 0) {
                dfs(node, null, new CFGPathGroup(), paths);
            }
        }
        return paths;
    }
    private CFGPathGroup getCirclePrimePaths(SubGraph subGraph, SubNode startNode) {
        return subGraph.getPaths(startNode, startNode);
    }
    public CFGPathGroup getPrimePaths() {
        SubGraph subGraph = new SubGraph(this);
        SCCGraph sccGraph = new SCCGraph(subGraph);
        primePaths = new CFGPathGroup();
        for (SCCNode sccNode : sccGraph) {
            if (sccNode.getSubGraph().size() > 1) {
                for (SubNode startNode : sccNode.getSubGraph()) {
                    primePaths.add(getCirclePrimePaths(subGraph, startNode));
                }
            }
        }
        primePaths.add(getDirectPrimePaths(sccGraph));
        return primePaths;
    }
}
