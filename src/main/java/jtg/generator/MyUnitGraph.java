package jtg.generator;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
// 因为UnitGraph只读   现在反编译class文件  重构其中的方法   主要是添加一个基路径计算方法
//

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.*;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.util.Chain;


public class MyUnitGraph implements DirectedGraph<Unit> {
    private static final Logger logger = LoggerFactory.getLogger(soot.toolkits.graph.UnitGraph.class);
    protected List<Unit> heads;
    protected List<Unit> tails;
    protected Map<Unit, List<Unit>> unitToSuccs;
    protected Map<Unit, List<Unit>> unitToPreds;
    protected SootMethod method;
    protected Body body;
    protected Chain<Unit> unitChain;

    protected MyUnitGraph(Body body) {
        this.body = body;
        this.unitChain = body.getUnits();
        this.method = body.getMethod();
        if (Options.v().verbose()) {
            logger.debug("[" + this.method.getName() + "]     Constructing " + this.getClass().getName() + "...");
        }
    }



    protected void buildUnexceptionalEdges(Map<Unit, List<Unit>> unitToSuccs, Map<Unit, List<Unit>> unitToPreds) {
        Iterator<Unit> unitIt = this.unitChain.iterator();
        Unit nextUnit = unitIt.hasNext() ? (Unit)unitIt.next() : null;

        while(nextUnit != null) {
            Unit currentUnit = nextUnit;
            nextUnit = unitIt.hasNext() ? (Unit)unitIt.next() : null;
            ArrayList<Unit> successors = new ArrayList();
            if (currentUnit.fallsThrough() && nextUnit != null) {
                successors.add(nextUnit);
                List<Unit> preds = (List)unitToPreds.get(nextUnit);
                if (preds == null) {
                    preds = new ArrayList();
                    unitToPreds.put(nextUnit, preds);
                }

                ((List)preds).add(currentUnit);
            }

            if (currentUnit.branches()) {
                Iterator var11 = currentUnit.getUnitBoxes().iterator();

                while(var11.hasNext()) {
                    UnitBox targetBox = (UnitBox)var11.next();
                    Unit target = targetBox.getUnit();
                    if (!successors.contains(target)) {
                        successors.add(target);
                        List<Unit> preds = (List)unitToPreds.get(target);
                        if (preds == null) {
                            preds = new ArrayList();
                            unitToPreds.put(target, preds);
                        }

                        ((List)preds).add(currentUnit);
                    }
                }
            }

            if (!successors.isEmpty()) {
                successors.trimToSize();
                unitToSuccs.put(currentUnit, successors);
            }
        }

    }

    protected void buildHeadsAndTails() {
        this.tails = new ArrayList();
        this.heads = new ArrayList();
        Iterator var1 = this.unitChain.iterator();

        while(true) {
            Unit s;
            List preds;
            do {
                if (!var1.hasNext()) {
                    if (!this.unitChain.isEmpty()) {
                        Unit entryPoint = (Unit)this.unitChain.getFirst();
                        if (!this.heads.contains(entryPoint)) {
                            this.heads.add(entryPoint);
                        }
                    }

                    return;
                }

                s = (Unit)var1.next();
                List<Unit> succs = (List)this.unitToSuccs.get(s);
                if (succs == null || succs.isEmpty()) {
                    this.tails.add(s);
                }

                preds = (List)this.unitToPreds.get(s);
            } while(preds != null && !preds.isEmpty());

            this.heads.add(s);
        }
    }

    protected Map<Unit, List<Unit>> combineMapValues(Map<Unit, List<Unit>> mapA, Map<Unit, List<Unit>> mapB) {
        Map<Unit, List<Unit>> result = new LinkedHashMap(mapA.size() * 2 + 1, 0.7F);
        Iterator var4 = this.unitChain.iterator();

        while(true) {
            while(var4.hasNext()) {
                Unit unit = (Unit)var4.next();
                List<Unit> listA = (List)mapA.get(unit);
                if (listA == null) {
                    listA = Collections.emptyList();
                }

                List<Unit> listB = (List)mapB.get(unit);
                if (listB == null) {
                    listB = Collections.emptyList();
                }

                int resultSize = listA.size() + listB.size();
                if (resultSize == 0) {
                    result.put(unit, Collections.emptyList());
                } else {
                    List<Unit> resultList = new ArrayList(resultSize);
                    List<Unit> list = null;
                    if (listA.size() >= listB.size()) {
                        resultList.addAll(listA);
                        list = listB;
                    } else {
                        resultList.addAll(listB);
                        list = listA;
                    }

                    Iterator var11 = list.iterator();

                    while(var11.hasNext()) {
                        Unit element = (Unit)var11.next();
                        if (!resultList.contains(element)) {
                            resultList.add(element);
                        }
                    }

                    result.put(unit, resultList);
                }
            }

            return result;
        }
    }

    protected void addEdge(Map<Unit, List<Unit>> unitToSuccs, Map<Unit, List<Unit>> unitToPreds, Unit head, Unit tail) {
        List<Unit> headsSuccs = (List)unitToSuccs.get(head);
        if (headsSuccs == null) {
            headsSuccs = new ArrayList(3);
            unitToSuccs.put(head, headsSuccs);
        }

        if (!((List)headsSuccs).contains(tail)) {
            ((List)headsSuccs).add(tail);
            List<Unit> tailsPreds = (List)unitToPreds.get(tail);
            if (tailsPreds == null) {
                tailsPreds = new ArrayList();
                unitToPreds.put(tail, tailsPreds);
            }

            ((List)tailsPreds).add(head);
        }

    }

    public Body getBody() {
        return this.body;
    }

    public List<Unit> getExtendedBasicBlockPathBetween(Unit from, Unit to) {
        MyUnitGraph g = this;
        if (this.getPredsOf(to).size() > 1) {
            return null;
        } else {
            LinkedList<Unit> pathStack = new LinkedList();
            LinkedList<Integer> pathStackIndex = new LinkedList();
            pathStack.add(from);
            pathStackIndex.add(0);
            int psiMax = this.getSuccsOf((Unit)pathStack.get(0)).size();
            int level = 0;

            while((Integer)pathStackIndex.get(0) != psiMax) {
                int p = (Integer)pathStackIndex.get(level);
                List<Unit> succs = g.getSuccsOf((Unit)pathStack.get(level));
                if (p >= succs.size()) {
                    pathStack.remove(level);
                    pathStackIndex.remove(level);
                    --level;
                    int q = (Integer)pathStackIndex.get(level);
                    pathStackIndex.set(level, q + 1);
                } else {
                    Unit betweenUnit = (Unit)succs.get(p);
                    if (betweenUnit == to) {
                        pathStack.add(to);
                        return pathStack;
                    }

                    if (g.getPredsOf(betweenUnit).size() > 1) {
                        pathStackIndex.set(level, p + 1);
                    } else {
                        ++level;
                        pathStackIndex.add(0);
                        pathStack.add(betweenUnit);
                    }
                }
            }

            return null;
        }
    }

    public List<Unit> getHeads() {
        return this.heads;
    }

    public List<Unit> getTails() {
        return this.tails;
    }

    public List<Unit> getPredsOf(Unit u) {
        List<Unit> l = (List)this.unitToPreds.get(u);
        return l == null ? Collections.emptyList() : l;
    }

    public List<Unit> getSuccsOf(Unit u) {
        List<Unit> l = (List)this.unitToSuccs.get(u);
        return l == null ? Collections.emptyList() : l;
    }

    public int size() {
        return this.unitChain.size();
    }

    public Iterator<Unit> iterator() {
        return this.unitChain.iterator();
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        Iterator var2 = this.unitChain.iterator();

        while(var2.hasNext()) {
            Unit u = (Unit)var2.next();
            buf.append("// preds: ").append(this.getPredsOf(u)).append('\n');
            buf.append(u).append('\n');
            buf.append("// succs ").append(this.getSuccsOf(u)).append('\n');
        }

        return buf.toString();
    }
}
