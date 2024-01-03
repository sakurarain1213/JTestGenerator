package jtg.generator;

import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @program: JTestGenerator
 * @description:
 * @author: 作者
 * @create: 2024-01-02 20:36
 */
public class IGeneratorTest {


    @Test
    void generatorTest() {
        String clspath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "test-classes";
        String clsName = "cut.LogicStructure";
        String methodName = "soloIf";

        BranchGenerator bg = new BranchGenerator(clspath, clsName, methodName);
        //bg.init();generate自动执行
        bg.generate();
       // System.out.println(bg.testData);

        /*
        StateGenerator sg = new StateGenerator(clspath, clsName, methodName);
        sg.generate();
        System.out.println(sg.testData);
        */
        //函数体有循环的时候有下标越界问题

    }

}
