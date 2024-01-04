package jtg.autoJunit;

import jtg.generator.BranchGenerator;
import jtg.generator.PrimePathGenerator;
import jtg.generator.StateGenerator;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

//存储一个class的信息
public class classProperty {

    private String clspath;
    private String pkg;
    private String className;

    private String coverType;
    private final Set<Method> methods=new LinkedHashSet<>();

    private final Set<String> mtdsName=new LinkedHashSet<>();
    private final Set<String> imports=new LinkedHashSet<>();
    private final Map<String,Set<String>>mtdTcs=new HashMap<>();

    private final Set<IMethod> meths=new LinkedHashSet<>();
    public classProperty(String cname,String ct){
        coverType = ct;
        className=cname;

    }

    public String getPkg(){
        return pkg;
    }
    public void setPkg(String str){pkg=str;}
    public String getClassName(){
        return className;
    }

    public Set<Method>getMethods(){
        return methods;
    }
    public Set<String>getmtdsName(){return mtdsName;}
    public Set<String>getImports(){
        return imports;
    }

    public Set<IMethod> getMeths() {
        return meths;
    }

    public Map<String,Set<String>>getMtdTcs(){
        return mtdTcs;
    }


    public void addMethod(Method method){

        methods.add(method);
        String mName=method.getName();
        mtdsName.add(mName);

        Set<String> tempstr=new LinkedHashSet<>();
        tempstr.add("test1");
        tempstr.add("test2");
        tempstr.add("test3");
        mtdTcs.put(mName,tempstr);
        //IMethod
        String tmp=method.getReturnType().toString();
        String[] tb=tmp.split("\\.");           //处理returnType

        IMethod tempImtd=new IMethod(method,mName,tb[tb.length-1]);

        Type[] paraTypes = method.getParameterTypes();

        for(Type para : paraTypes){
            String str=para.getTypeName();

            String[] buff=str.split("\\.");

            tempImtd.AddParam(buff[buff.length-1]);
        }

        String cp = System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes";

        if(coverType.equals("StateCover")) {
            StateGenerator sg = new StateGenerator(cp, pkg + "." + className, mName);
            sg.generate();
            tempImtd.SetTC(sg.myTestData);
            System.out.println(sg.myTestData + "list格式的set结果");
        }else if(coverType.equals("BranchCover")) {


            BranchGenerator bg = new BranchGenerator(cp, pkg + "." + className, mName);
            //bg.init();generate自动执行
            bg.generate();
            tempImtd.SetTC(bg.myTestData);
            System.out.println(bg.myTestData);
        }else if(coverType.equals("PrimePathCover")) {


            PrimePathGenerator pg = new PrimePathGenerator(cp, pkg + "." + className, mName);
            pg.generate();
            tempImtd.SetTC(pg.myTestData);
            System.out.println(pg.myTestData + "list格式的set结果");
        }else{
            StateGenerator sg = new StateGenerator(cp, pkg + "." + className, mName);
            sg.generate();
            tempImtd.SetTC(sg.myTestData);
            System.out.println(sg.myTestData + "list格式的set结果");
        }

        meths.add(tempImtd);

    }


    public void addImport(String str){
        imports.add(str);
    }

    public void show(){
        System.out.println(pkg);
        System.out.println(imports);
        System.out.println(className);
        System.out.println(mtdsName);

    }

    public void SetClspath(String path){
        clspath=path;
    }
}
