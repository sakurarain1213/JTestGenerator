package jtg.generator;

import jtg.generator.prime.PrimePathCal;
import jtg.generator.util.CommonUtil;
import jtg.generator.util.PathUtil;
import soot.Unit;

import java.util.*;

/**
 * @program: JTestGenerator
 * @description:
 * @author: 作者
 * @create: 2024-01-04 16:06
 */
public class PrimePathGenerator extends IGenerator{

    public PrimePathGenerator(String classPath, String className, String methodName) {
        super(classPath, className, methodName);
    }

    @Override
    void init() {
        PrimePathCal primePathCal = new PrimePathCal(body);
        initSet = new HashSet<>(primePathCal.generatePrimePathUnit()); //初始集合是所有的基路径
        solvableSet = new HashSet<>();
        unsolvableSet = new HashSet<>(initSet);
        testData = new HashSet<>();
    }

    @Override
    Set<List<Unit>> calAllFullCandidate(Object o) {
        List<Unit> primePath = (List)o;
        Unit headOfPrimePath = primePath.get(0);
        Unit tailOfPrimePath = primePath.get(primePath.size()-1);
        Set<List<Unit>> backwardPaths = new HashSet<>();
        Set<List<Unit>> forwardPaths = new HashSet<>();
        for (Unit head : heads) {
            PathUtil pathUtil = new PathUtil();
            pathUtil.findPath(ug, headOfPrimePath, head, new ArrayList<>(), true, new HashMap<>());
            backwardPaths.addAll(pathUtil.getSearchPathResult());
        }
        for (Unit tail : tails) {
            PathUtil pathUtil = new PathUtil();
            pathUtil.findPath(ug, tailOfPrimePath, tail, new ArrayList<>(), false, new HashMap<>());
            forwardPaths.addAll(pathUtil.getSearchPathResult());
        }
        Set<List<Unit>> result = new HashSet<>();
        if (backwardPaths.isEmpty() || forwardPaths.isEmpty()) {
            return result; //没有完整路径，返回空
        }
        for (List<Unit> backwardPath : backwardPaths) {
            Collections.reverse(backwardPath); //这个地方注意，每次得复制一下
            for (List<Unit> forwardPath : forwardPaths) {
                List<Unit> backwardPathCopy = new ArrayList<>(backwardPath);
                List<Unit> forwardPathCopy = new ArrayList<>(forwardPath); //注意不能直接操作
                backwardPathCopy.remove(backwardPathCopy.size()-1); //首尾重合了
                backwardPathCopy.addAll(primePath);
                forwardPathCopy.remove(0);
                backwardPathCopy.addAll(forwardPathCopy);//拼接到一起
                result.add(backwardPathCopy);
            }
        }
        return result;
    }

    @Override
    void checkCov(List<Unit> fullPath) {
        for (Object o : unsolvableSet) {
            List<Unit> primePath = (List<Unit>) o;
            if (CommonUtil.leftIsSubList(primePath, fullPath)) {
                solvableSet.add(primePath);
            }
        }
        unsolvableSet.removeAll(solvableSet);
    }
}