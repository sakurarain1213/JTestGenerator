package jtg.autoJunit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IMethod {

    private Method method;
    private String name;

    private String returnType;
    private int paraNum;
    public List<String> paramTypes=new ArrayList<>();


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



}
