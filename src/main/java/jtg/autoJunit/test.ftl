package ${pkg};

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

<#assign mapmtdTcs=mtdTcs/>      <#-- 声明method到Tcs的map -->




public class ${className}Test{

<#list meths as meth>

<#assign tcslist=mapmtdTcs[meth.name]/>
<#assign set_of_tc=meth.TC/><#-- 声明tc的set -->

    @Test
    void ${meth.name}(){
    ${className} test = RandomUtil.nextObject(${className}.class);
    <#list set_of_tc as tc><#-- tc是Stringlist -->
    <#if meth.returnType != "void" >${meth.returnType} temp${tc_index} = </#if>test.${meth.name}(<#list tc as t>${t}<#if t_has_next>,</#if></#list>);
    <#-- <#if meth.returnType != "void" >assertTrue(!temp${tc_index}.isEmpty());</#if> -->
    </#list>
    }
</#list>


}