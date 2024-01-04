package jtg.generator;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import soot.Body;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class RandomUtilTest {

    @Test
    void nextObject() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        System.out.println("1");




        System.out.println("基本类型随机生成----");
        short tshort=RandomUtil.nextObject(short.class);
        System.out.println("short:"+tshort);
        byte tbyte=RandomUtil.nextObject(byte.class);
        System.out.println("byte:"+tbyte);
        int tint=RandomUtil.nextObject(int.class);
        System.out.println("int:"+tint);
        char tchar=RandomUtil.nextObject(char.class);
        System.out.println("char:"+tchar);
        String tstring=RandomUtil.nextObject(String.class);
        System.out.println("string:"+tstring);
        long tlong=RandomUtil.nextObject(long.class);
        System.out.println("long:"+tlong);
        float tfloat=RandomUtil.nextObject(float.class);
        System.out.println("float:"+tfloat);
        double tdouble=RandomUtil.nextObject(double.class);
        System.out.println("double:"+tdouble);
        boolean tboolean=RandomUtil.nextObject(boolean.class);
        System.out.println("boolean:"+tboolean);


        System.out.println("测试输入类名，产生随机对象-----");
        Object clasNm=RandomUtil.nextObject("jtg.generator.TestClass");
        Method method = clasNm.getClass().getMethod("Show");
        method.invoke(clasNm);


        System.out.println("产生随机对象------");
        TestClass t2=RandomUtil.nextObject(TestClass.class);
        System.out.println(t2);

        t2.Show(1,2);

        System.out.println("产生有继承关系的随机对象----");
        Son t3=RandomUtil.nextObject(Son.class);
        t3.Show(1,2);

        assertTrue(clasNm!=null);
        assertTrue(t2!=null);
        assertTrue(t3!=null);
    }
}