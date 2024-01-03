package jtg.generator;

import jtg.generator.util.PathUtil;
import soot.Unit;

import java.util.*;

/**
 * @program: JTestGenerator
 * @description:
 * @author: 作者
 * @create: 2024-01-02 23:20
 */
//语句覆盖   statement  coverage generator

public class StateGenerator extends IGenerator {

    public StateGenerator(String classPath, String className, String methodName) {
        super(classPath, className, methodName);
    }

    @Override
    void init() {
        initSet = new HashSet<>(body.getUnits());
        solvableSet = new HashSet<>();
        unsolvableSet = new HashSet<>(initSet);
        testData = new HashSet<>();
    }

    @Override
    Set<List<Unit>> calAllFullCandidate(Object o) {  //计算经过某一个特定点的所有完整路径
        Unit stmtUnit = (Unit) o;   //某一个特定节点
        Set<List<Unit>> backwardPaths = new HashSet<>();
        Set<List<Unit>> forwardPaths = new HashSet<>();
        for (Unit head : heads) {
            PathUtil pathUtil = new PathUtil();
            pathUtil.findPath(ug, stmtUnit, head, new ArrayList<>(), true, new HashMap<>());
            backwardPaths.addAll(pathUtil.getSearchPathResult());
        }
        for (Unit tail : tails) {
            PathUtil pathUtil = new PathUtil();
            pathUtil.findPath(ug, stmtUnit, tail, new ArrayList<>(), false, new HashMap<>());
            forwardPaths.addAll(pathUtil.getSearchPathResult());
        }
        Set<List<Unit>> result = new HashSet<>();
        if (backwardPaths.isEmpty() || forwardPaths.isEmpty()) {
            return result; //没有完整路径，返回空
        }

        /*两个路径集合都不为空，则对每个向后路径和向前路径进行组合来完成的，
        向后路径在组合之前要先进行反转（因为它们是从 stmtUnit 到开始节点的）。
        在组合时，移除重复的 stmtUnit （因为它会在向后和向前路径中各出现一次）。
        最后，所有组合形成的完整路径都被添加到 result 集合中，并返回。*/
        for (List<Unit> backwardPath : backwardPaths) {
            Collections.reverse(backwardPath); //这个地方注意，每次得复制一下
            for (List<Unit> forwardPath : forwardPaths) {
                List<Unit> backwardPathCopy = new ArrayList<>(backwardPath);
                backwardPathCopy.remove(backwardPathCopy.size()-1); //首尾重合了
                backwardPathCopy.addAll(forwardPath);
                result.add(backwardPathCopy);
            }
        }

        return result;
    }

    @Override
    void checkCov(List<Unit> fullPath) {
        solvableSet.addAll(fullPath); //这条路径覆盖的所有语句节点都加入可解集
        fullPath.forEach(unsolvableSet::remove); //在不可解集中的也减去
    }


}
