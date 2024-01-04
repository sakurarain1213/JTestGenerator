package jtg.autoJunit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//读取一个class的必要信息
public class classReader {
    private final String CLASS_PATH;

    private final String CLASS_NAME;

    private String pkgName;


    private List<String> contents;
    public classReader(String classPath,String className)  {
        CLASS_PATH=classPath;
        CLASS_NAME=className;
        contents=new ArrayList<>();

    }

    public void Read() throws IOException {
        System.out.println(CLASS_PATH);
        BufferedReader br = new BufferedReader(new FileReader(CLASS_PATH));
        String line;
        while((line=br.readLine())!=null) {
            contents.add(line);
        }
    }

    public classProperty createProperty(){
        classProperty Property = new classProperty(CLASS_NAME);
        Property.SetClspath(CLASS_PATH);
        for(String line : contents){

            if(line.startsWith("package")){ //获取包名
                String[] buff=line.split(" ");
                buff[buff.length-1]=buff[buff.length-1].replace(";","");
                pkgName=buff[buff.length-1];
                Property.setPkg(pkgName);
            }else if(line.startsWith("import")){
                String[] buff=line.split(" ");
                buff[buff.length-1]=buff[buff.length-1].replace(";","");
                Property.addImport(buff[buff.length-1]);
            }


        }
        try {
            Class<?> clazz=Class.forName(pkgName+"."+CLASS_NAME);
            Method[] methods=clazz.getDeclaredMethods();
            for(Method m : methods){
                Property.addMethod(m);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        return Property;
    }

}
