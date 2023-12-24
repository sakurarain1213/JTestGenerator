package jtg.generator;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

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
        String methodName = "soloIf";
        MyGraph mg = new MyGraph(clspath, clsName, methodName);
        mg.printBlockGraph(mg.bg);
    }
}
