package jtg.generator.util;



//等价于全局变量的定义
public class StaticsUtil {
    public static int MOST_RECUR = 0;
    public static int MOST_BACKWARD_FIND = 10;
    public static int MOST_FORWARD_FIND = 10;

    public static int MOST_LOOP = 1;

    private static int RAND = 0;

    public static int genMark(){
        return RAND++;
    }
}