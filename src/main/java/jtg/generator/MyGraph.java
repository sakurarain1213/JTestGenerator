package jtg.generator;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
// 因为UnitGraph只读   现在反编译class文件  重构其中的方法   主要是添加一个基路径计算方法
//

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jtg.graphics.SootCFG;
import soot.Unit;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.toolkits.graph.UnitGraph;
import soot.Body;


//利用blockGraph 构建块图
public class MyGraph {

    protected String clsPath;
    protected String clsName;
    protected String mtdName;
    protected BlockGraph bg;
    protected UnitGraph ug;

    public MyGraph(Body body) {
        bg = SootCFG.getSimpleCFG(body);
    }
    public MyGraph(String classPath, String className, String methodName) {
        clsPath = classPath;
        clsName = className;
        mtdName = methodName;
        ug = SootCFG.getMethodCFG(clsPath, clsName, mtdName);
        bg = SootCFG.getSimpleCFG(ug.getBody());
    }

    //分析BlockGraph  如何存 块和边
    public void printBlockGraph(BlockGraph bg) {
        for (Block block : bg) {
            System.out.println("Block: " + block.toShortString());

            for (Unit unit : block) {
                System.out.println("  Unit: " + unit.toString());
                // 块里的每个指令/语句
            }
        }
        // 获取和处理前驱和后继基本块
       // List<Block> predecessors = block.getPreds();
       // List<Block> successors = block.getSuccs();
     /*遍历所有基本块
        System.out.println("Predecessors: " + blocksToString(predecessors));
        System.out.println("Successors: " + blocksToString(successors));
        // ... 其他处理
        }
        */
    }




    //参照 UnitGraph   添加基路径计算算法     函数体待修改
    // public List<List<Block>>  calPrimePath(Unit from, Unit to) ;

    // private void dfsMyGraph(, List<Block> onePath, List<List<Block>> result)


}