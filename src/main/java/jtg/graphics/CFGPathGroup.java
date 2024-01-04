package jtg.graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CFGPathGroup implements Iterable<CFGPath> {
    private final List<CFGPath> pathList;
    public CFGPathGroup() {
        pathList = new ArrayList<>();
    }
    public void add(CFGPath path) {
        pathList.add(path);
    }
    public void add(CFGPathGroup pathGroup) {
        for (CFGPath path : pathGroup) {
            add(path);
        }
    }
    public void remove(CFGPath path) {
        pathList.remove(path);
    }
    public CFGPathGroup multiply(CFGPathGroup pathGroup) {
        CFGPathGroup newPathGroup = new CFGPathGroup();
        for (CFGPath path1 : this) {
            for (CFGPath path2 : pathGroup) {
                newPathGroup.add(path1.multiply(path2));
            }
        }
        return newPathGroup;
    }
    public CFGPathGroup multiply(CFGPath path) {
        CFGPathGroup newPathGroup = new CFGPathGroup();
        for (CFGPath path1 : this) {
            newPathGroup.add(path1.multiply(path));
        }
        return newPathGroup;
    }
    @Override
    public Iterator<CFGPath> iterator() {
        return pathList.iterator();
    }
}
