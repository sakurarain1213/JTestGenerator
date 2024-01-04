package jtg.autoJunit;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;

//根据classProperty自动生成Junit测试代码
public class autoJenerator {

    private String root;
    private String outRootPath;
    private String templatePath;
    private String templateName;
    private classProperty property;
    private String javaSuff=".java";
    private Template template;
    private Configuration configuration;
    public autoJenerator(String relativePath) throws IOException {




        //设置路径
        root=System.getProperty("user.dir")+"/";
        outRootPath=root+"src/test/java/";
        templatePath= root+"src/main/java/jtg/autoJunit";
        templateName="test.ftl";
        //设置freemarker
        configuration=new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setDirectoryForTemplateLoading(new File(templatePath));
        template=configuration.getTemplate(templateName);

        //设置
        getProperty(relativePath);


    }

    public void TestJenerate() throws IOException, TemplateException {//根据property信息和outPut作为输出路径

        //处理输出路径
        String javaName=property.getClassName()+"Test"+javaSuff;
        String packageName=property.getPkg().replaceAll("\\.","/");
        String out = outRootPath+packageName+"/"+javaName;

        OutputStreamWriter outPut = new OutputStreamWriter(new FileOutputStream(out));
        template.process(property,outPut);
    }

    public String solveClassName(String pth){
        String[] buff=pth.split("/");
        String last = buff[buff.length-1];
        String ans = last.substring(0,last.length()-5);
        return ans;
    }

    private void getProperty(String relativePath) throws IOException {
        classReader reader = new classReader(root+relativePath,solveClassName(relativePath));
        reader.Read();
        property=reader.createProperty();
    }
}
