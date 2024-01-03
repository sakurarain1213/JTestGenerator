package jtg.generator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TestClass {

    private String name;
    private int age;
    private  String phone;
    private  long id;
    private List<TestClass> classes;


    public void Show(int a,int b){
        if(a>b){

        }else {
//            System.out.println(name);
//            System.out.println(age);
//            System.out.println(phone);
//            System.out.println(id);
//            System.out.println(classes);
        }
    }

    public String paraString(int str){
            if(str>0) {
                return "abc";
            }else{
                return "asddd";
            }
    }
    public int paraint(int a,int b){
        if(a>b) {
            return a - b;
        }else{
            return a + b;
        }
    }
}
