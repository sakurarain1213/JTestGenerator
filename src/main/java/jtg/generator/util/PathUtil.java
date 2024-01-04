package jtg.generator.util;

import jtg.graphics.SootCFG;
import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.*;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.UnitGraph;

import java.util.*;

public class PathUtil {
    //一条Block转为一条Unit
    public static List<Unit> transferBp2Up(List<Block> blocks) {
        List<Unit> result = new ArrayList<>(); //每一条Block转换为unit
        for (Block block : blocks) {
            result.addAll(transferB2U(block));
        }
        return result;
    }

    public static List<Unit> transferB2U(Block block) {
        List<Unit> result = new ArrayList<>(); //每一个Block转换为unit
        Iterator<Unit> basicBlockIt = block.getBody().getUnits().iterator(block.getHead(), block.getTail());
        while (basicBlockIt.hasNext()) {
            Unit someUnit = (Unit) basicBlockIt.next();
            result.add(someUnit);
        }
        return result;
    }

    //处理调用方法，让他成为更大的cfg
    //JInvokeStmt如果是纯调用，最后就不处理返回值
    //JAssignStmt如果调用有返回值，要将返回值赋值给发起调用的位置
    public static void extendCFG(Body originBody, Set<Unit> caller) { //TODO 目前是只调用了一次
        //TODO 变量名重复的问题未解决
        UnitPatchingChain units = originBody.getUnits();
        List<Unit> unitList = new ArrayList<>(units);//循环要更改列表，不能用foreach
        for (int i = 0; i < unitList.size(); i++) { //当被调用的加入到里面时，不会增加list长度
            Unit unit = unitList.get(i);

            //直接调用，返回值没有接收的变量，可能是returnVoid，也有可能return有值但是没有人接受
            //soot库 检查 JInvokeStmt是Unit的一个特定子类，用于表示方法调用语句。
            if (unit instanceof JInvokeStmt) {
                caller.add(unit);
                JInvokeStmt jInvokeStmt = (JInvokeStmt) unit; //纯invoke语句
                InvokeExpr invokeExpr = jInvokeStmt.getInvokeExpr(); //invoke表达式 specialinvoke $r1.<jtg.Person: void <init>()>()
                //被调用者的body
                Body invokedBody = SootCFG.getMethodBody(invokeExpr.getMethod().getDeclaringClass().getName(), invokeExpr.getMethod().getSignature());
                Value callerVal = getCaller(jInvokeStmt.getInvokeExprBox().getValue());//是哪个主体调用的这个，方便重命名
                for (Local local : invokedBody.getLocals()) {
                    local.setName(callerVal + "X" + local.getName()); //区分变量名称
                }
                List<Value> args = invokeExpr.getArgs();/*调用的实参*/
                for (int j = 0; j < args.size(); j++)
                    invokedBody.getParameterLocal(j).setName(args.get(j).toString()); //原本是添加Assignment语句，结果只用处理变量名即可
                UnitPatchingChain invokedChain = invokedBody.getUnits(); //被调用函数的语句
                //处理返回调度
                Iterator<Unit> invokedIt = invokedChain.snapshotIterator();
                while (invokedIt.hasNext()) {
                    Unit invokedUnit = invokedIt.next();
                    if (invokedUnit instanceof JIdentityStmt) {
                        JIdentityStmt jIdentityStmt = (JIdentityStmt) invokedUnit;
                        if (jIdentityStmt.toString().contains("@this")) {
                            Value leftOp = jIdentityStmt.getLeftOp();
                            Local left = (Local) leftOp;
                            left.setName(callerVal.toString());
                        }
                    }
                    if (invokedUnit instanceof JReturnStmt || invokedUnit instanceof JReturnVoidStmt) {
                        //最关键，bug找了好久，要在函数调用最后加GOTO！！否则图是错的
                        JGotoStmt gotoStmt = new JGotoStmt(unitList.get(i + 1));
                        invokedChain.insertBefore(gotoStmt, invokedUnit);
                        invokedChain.remove(invokedUnit); //移除返回语句，否则图不对，直接终结了
                    }
                }
                units.insertOnEdge(invokedChain, unit, null); //将被调用的语句拼接到大图中
//                units.remove(unit);//移除调用语句
            }


            //最后的返回值要保存
            if (unit instanceof JAssignStmt && ((JAssignStmt) unit).containsInvokeExpr()) {
                caller.add(unit);
                Value callerVal = getCaller(((JAssignStmt) unit).getInvokeExprBox().getValue());
                InvokeExpr invokeExpr = ((JAssignStmt) unit).getInvokeExpr();  //r2.<cut.LogicStructure: int crazyFun(int,int)>(i9, i1)
                Value home = ((JAssignStmt) unit).getLeftOp(); //赋值语句的左侧，用于存储返回值
                Body invokedBody = SootCFG.getMethodBody(invokeExpr.getMethod().getDeclaringClass().getName(), invokeExpr.getMethod().getSignature());
                for (Local local : invokedBody.getLocals())
                    local.setName(callerVal + "X" + local.getName());

                List<Value> args = invokeExpr.getArgs();
                for (int j = 0; j < args.size(); j++)
                    invokedBody.getParameterLocal(j).setName(args.get(j).toString());
                //调用将实参赋值
                UnitPatchingChain invokedChain = invokedBody.getUnits(); //被调用函数的语句
                Iterator<Unit> invokedIt = invokedChain.snapshotIterator();
                while (invokedIt.hasNext()) {
                    Unit invokedUnit = invokedIt.next();
                    if (invokedUnit instanceof JIdentityStmt) {
                        JIdentityStmt jIdentityStmt = (JIdentityStmt) invokedUnit;
                        if (jIdentityStmt.toString().contains("@this")) {
                            Value leftOp = jIdentityStmt.getLeftOp();
                            Local left = (Local) leftOp;
                            left.setName(callerVal.toString());
                        }
                    }
                    if (invokedUnit instanceof JReturnStmt) {
                        JReturnStmt retStmt = (JReturnStmt) invokedUnit;
                        Value retVal = retStmt.getOp();
                        //最后将返回值赋值给调用的地方
                        invokedChain.insertBefore(new JAssignStmt(home, retVal), retStmt);
                        JGotoStmt gotoStmt = new JGotoStmt(unitList.get(i + 1));
                        invokedChain.insertBefore(gotoStmt, retStmt);
                        invokedChain.remove(retStmt); //移除返回语句
                    }
                }
                units.insertOnEdge(invokedChain, unit, null);
//                units.remove(unit);//移除调用语句
            }
        }
    }

    private static Value getCaller(Value jInvokeValue) {
        if (jInvokeValue instanceof JSpecialInvokeExpr) {
            JSpecialInvokeExpr jSpecialInvokeExpr = (JSpecialInvokeExpr) jInvokeValue;
            return jSpecialInvokeExpr.getBaseBox().getValue();
        } else if (jInvokeValue instanceof JVirtualInvokeExpr) {
            JVirtualInvokeExpr jVirtualInvokeExpr = (JVirtualInvokeExpr) jInvokeValue;
            return jVirtualInvokeExpr.getBaseBox().getValue();
        }
        return null; //TODO 目前只处理了两种invoke
    }


    private Set<List<Unit>> result = new HashSet<>();

    public Set<List<Unit>> getSearchPathResult() {
        return result;
    }

    //    private Map<Unit, Integer> visitRecord = new HashMap<>();
    //向上或向下扩招到目的点

//新版本核心函数   尝试   从目标点开始  搜索到全图起点或者终点结束
    public void findPath(UnitGraph ug, Unit startU, Unit targetU, List<Unit> path,
                         boolean back, Map<Unit, Integer> visitRecord) {

        int threshold = back ? StaticsUtil.MOST_BACKWARD_FIND : StaticsUtil.MOST_FORWARD_FIND;
        List<Unit> nextUnits = back ? ug.getPredsOf(startU) : ug.getSuccsOf(startU);
        if (result.size() > threshold) // 已经找到一定数量了，不用再找了
            return;
        // 避免重复访问
        if (visitRecord.containsKey(startU) && visitRecord.get(startU) > StaticsUtil.MOST_LOOP) {
            return; // 达到一定循环次数，不再找寻
        }

        // 特殊情况处理：如果startU就是目标    test debug
        if (startU.equals(targetU)) {
            path.add(startU);
            result.add(new ArrayList<>(path));
            path.remove(path.size() - 1); // 移除添加的节点，保持状态
            return;
        }


        visitRecord.put(startU, visitRecord.getOrDefault(startU, 0) + 1);
        ArrayList<Unit> pathClone = new ArrayList<>(path);
        pathClone.add(startU);
        // 找到了目标
        if (startU.equals(targetU)) {
            result.add(pathClone);
            return;
        }
        for (Unit next : nextUnits) {
            // 检查是否正在重复访问路径中的 Unit
            if (!pathClone.contains(next)) {
                findPath(ug, next, targetU, pathClone, back, new HashMap<>(visitRecord));
            }
        }
    }



    //核心函数  但是需要在有循环的时候debug
    public void findPath2(UnitGraph ug, Unit startU, Unit targetU, List<Unit> path, boolean back/*back true forward false*/, Map<Unit, Integer> visitRecord) {
        int threshold;
        List<Unit> nextUnits;
        if (back) {
            threshold = StaticsUtil.MOST_BACKWARD_FIND;
            nextUnits = ug.getPredsOf(startU);
        } else {
            threshold = StaticsUtil.MOST_FORWARD_FIND;
            nextUnits = ug.getSuccsOf(startU);
        }
        if (result.size() > threshold) //已经找到一定数量了，不用再找了
            return;
        Map<Unit, Integer> visitRecordCopy = new HashMap<>(visitRecord);
        visitRecordCopy.put(startU, Optional.ofNullable(visitRecordCopy.get(startU)).orElse(0)+1);
        if (visitRecordCopy.get(startU) > StaticsUtil.MOST_LOOP) { //达到一定循环次数，不再找寻
            return;
        }
        ArrayList<Unit> pathClone = new ArrayList<>(path); //克隆
        pathClone.add(startU);
        //找到了目标
        if (startU.equals(targetU)) {
            result.add(pathClone);
//            System.out.println("加入了一条path" + path);
            return;
        }
        for (Unit next : nextUnits) {
            findPath(ug, next, targetU, pathClone, back, visitRecordCopy);
        }
    }
}