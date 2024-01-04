package jtg.autoJunit;

import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class autoJeneratorTest {

    @Test
    void testJenerate() throws IOException, TemplateException {


        autoJenerator jeneratorS=new autoJenerator("src/main/java/jtg/generator/TestClass.java","StateCover");
        autoJenerator jeneratorB=new autoJenerator("src/main/java/jtg/generator/TestClass.java","BranchCover");
        autoJenerator jeneratorP=new autoJenerator("src/main/java/jtg/generator/TestClass.java","PrimePathCover");
        jeneratorP.TestJenerate();
    }
    @Test
    void path(){
        String root=System.getProperty("user.dir");
        System.out.println(root);
    }
}