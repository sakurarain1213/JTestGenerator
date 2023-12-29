package jtg.autoJunit;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

//存储一个class的信息
public class classProperty {

    private String pkg;
    private String className;

    private final Set<Method> methods=new LinkedHashSet<>();

    private final Set<String> mtdsName=new LinkedHashSet<>();
    private final Set<String> imports=new LinkedHashSet<>();

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

    public Set<String>getMtdsName(){return mtdsName;}
    public Set<String>getImports(){
        return imports;
    }

    public void addMethod(Method method){

        methods.add(method);
        mtdsName.add(method.getName());
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
