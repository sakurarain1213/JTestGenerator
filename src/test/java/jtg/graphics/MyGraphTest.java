package jtg.graphics;

import fj.P;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @program: JTestGenerator
 * @description:
 * @author: 作者
 * @create: 2023-12-24 20:15
 */
public class MyGraphTest {


    @Test
    void solo_if_correct() {
        String clspath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.LogicStructure";
        String methodName = "multipleWhile";
        ControlFlowGraph mg = new ControlFlowGraph(clspath, clsName, methodName);
        for (CFGNode node : mg) {
            System.out.print("Node " + node.getIndex() + ": ");
            for (CFGNode to : node.getSucceeds()) {
                System.out.print(to.getIndex() + " ");
            }
            System.out.println();
        }
        SubGraph subGraph = new SubGraph(mg);
        SCCGraph sccGraph = new SCCGraph(subGraph);
        for (SCCNode sccNode : sccGraph) {
            System.out.print("SCCNode " + sccNode.getIndex() + ": ");
            System.out.print("(");
            for (SubNode innerNode : sccNode.getSubGraph()) {
                System.out.print(innerNode.getOriginNode().getIndex() + " ");
            }
            System.out.print(")");
            for (Triple<SCCNode, CFGNode, CFGNode> edge : sccNode.getSucceeds()) {
                System.out.print("[" + edge.getLeft().getIndex() + "," + edge.getMiddle().getIndex() + "," + edge.getRight().getIndex() + "]");
            }
            System.out.println();
        }
        CFGPathGroup primePaths = mg.getPrimePaths();
        for (CFGPath path : primePaths) {
            for (CFGNode node : path) {
                System.out.print(node.getIndex() + " -> ");
            }
            System.out.println();
        }
    }
}
