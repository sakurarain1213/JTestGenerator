package jtg.generator;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import soot.Body;

import static org.junit.jupiter.api.Assertions.*;

class RandomUtilTest {

    @Test
    void nextObject() {
        System.out.println("1");
        EasyRandom easyRandom=new EasyRandom();
        TestClass tc = easyRandom.nextObject(TestClass.class);
        TestClass t2=RandomUtil.nextObject(TestClass.class);
        Son t3=RandomUtil.nextObject(Son.class);
        int tint=RandomUtil.nextObject(int.class);

        System.out.println("2");
        System.out.println(tc);



        System.out.println(t2);
        tc.Show();
        t2.Show();
        System.out.println(tint);
        System.out.println("t3----");
        t3.Show();
    }
}