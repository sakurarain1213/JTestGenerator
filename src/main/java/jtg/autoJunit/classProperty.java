package jtg.autoJunit;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

//存储一个class的信息
public class classProperty {

    private String pkg;
    private String className;

    private final Set<Method> methods=new LinkedHashSet<>();

    private final Set<String> mtdsName=new LinkedHashSet<>();
    private final Set<String> imports=new LinkedHashSet<>();
    private final Map<String,Set<String>>mtdTcs=new HashMap<>();

    private final Set<IMethod> meths=new LinkedHashSet<>();
    public classProperty(String cname){
        className=cname;

    }

    public String getPkg(){
        return pkg;
    }
    public void setPkg(String str){pkg=str;}
    public String getClassName(){
        return className+"Test";
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
}
