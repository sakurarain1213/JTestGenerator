package jtg.graphics;

import jtg.generator.MyTrapUnitGraph;
import jtg.generator.MyUnitGraph;
import soot.*;
import soot.jimple.JimpleBody;
import soot.options.Options;
import soot.toolkits.graph.ClassicCompleteUnitGraph;
import soot.toolkits.graph.TrapUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.io.File;

public class SootCFG {


    public static UnitGraph getMethodCFG(String sourceDirectory, String clsName, String methodName){
        Body body = getMethodBody(sourceDirectory,clsName,methodName);
        UnitGraph ug = new ClassicCompleteUnitGraph(body);
        return ug;
    }
    //补一个针对MyUnitGraph的方法
    public static MyUnitGraph MygetMethodCFG(String sourceDirectory, String clsName, String methodName){
        Body body = getMethodBody(sourceDirectory,clsName,methodName);
        MyUnitGraph ug = new MyTrapUnitGraph(body);
        return ug;
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

}
