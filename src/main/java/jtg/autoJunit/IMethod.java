package jtg.autoJunit;

import jtg.generator.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IMethod {

    private Method method;
    private String name;

    private String returnType;
    private int paraNum;
    public List<String> paramTypes=new ArrayList<>();

    public Set<List<String>> TC=new HashSet<>();

    public IMethod(Method tm,String nam, String rt){
        method=tm;
        name=nam;
        returnType=rt;
        paraNum=0;
    }
    public void SetMethod(Method mtd){
        method=mtd;
    }

    public void SetName(String nam){
        name=nam;
    }

    public void SetReturnType(String type){
        returnType=type;
    }

    public String getName(){
        return name;
    }
    public String getReturnType(){
        return returnType;
    }
    public int getParaNum(){
        return paraNum;
    }
    public List<String> getParamTypes(){
        return paramTypes;
    }
    void AddParam(String parat){
        paramTypes.add(parat);
        paraNum++;
    }

    public void SetTC(Set<List<String>> tc){

        TC=tc;
        System.out.println("------------");
        for(List<String> t : TC){
            System.out.println(t);
            for(String str : t){
                System.out.println(str);
            }
        }
        System.out.println("------------");
    }

    public Set<List<String>>getTC(){
        return TC;
    }


}
