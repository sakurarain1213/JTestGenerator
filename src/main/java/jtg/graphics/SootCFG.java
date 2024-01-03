package jtg.graphics;

import soot.*;
import soot.options.Options;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.toolkits.graph.ClassicCompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.io.File;

public class SootCFG {

    public static UnitGraph getMethodCFG(String sourceDirectory, String clsName, String methodName){
        Body body = getMethodBody(sourceDirectory,clsName,methodName);
        UnitGraph ug = new ClassicCompleteUnitGraph(body);
        return ug;
    }

    //补一个生成block图的方法   nope
    public static BlockGraph getSimpleCFG(Body body){
        return new BriefBlockGraph(body);
    }
    public static Body getMethodBody(String sourceDirectory,String clsName,String methodName){
        G.reset();
        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_soot_classpath(sourceDirectory);
        SootClass sc = Scene.v().loadClassAndSupport(clsName);
        sc.setApplicationClass();
        Scene.v().loadNecessaryClasses();
        SootClass mainClass = Scene.v().getSootClass(clsName);
        SootMethod sm = mainClass.getMethodByName(methodName);
        Body body = sm.retrieveActiveBody();
        return body;
    }

    //补充版本
    public static Body getMethodBody(String clsName,String signature){
        G.reset();
        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_soot_classpath(System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes");
        SootClass sc = Scene.v().loadClassAndSupport(clsName);
        sc.setApplicationClass();
        Scene.v().loadNecessaryClasses();
        SootMethod sm = Scene.v().getMethod(signature);
        Body body = sm.retrieveActiveBody();
        return body;
    }



}
