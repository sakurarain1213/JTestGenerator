package ${pkg};

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

<#assign mapmtdTcs=mtdTcs/>      <#-- 声明method到Tcs的map -->




public class ${className}{

<#list meths as meth>

<#assign tcslist=mapmtdTcs[meth.name]/>

    @Test
    void ${meth.name}(){

    <#list tcslist as tc>

    <#if meth.returnType != "void" >${meth.returnType} temp${tc_index} = </#if>${meth.name}(<#list meth.paramTypes as para>${para}<#if para_has_next>,</#if></#list>${tc});
        <#if meth.returnType != "void" >assertTrue(!temp${tc_index}.isEmpty());</#if>
    </#list>
    }
</#list>


}