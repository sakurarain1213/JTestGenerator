package ${pkg};

import org.junit.jupiter.api.Test;


<#assign mapmtdTcs=mtdTcs/>      <#-- 声明method到Tcs的map -->




public class ${className}{

<#list meths as meth>

<#assign tcslist=mapmtdTcs[meth.name]/>

    @Test
    void ${meth.name}(){

    <#list tcslist as tc>

    <#if meth.returnType != "void" >${meth.returnType} temp = </#if>${meth.name}(<#list meth.paramTypes as para>${para},</#list>${tc});
    </#list>
    }
</#list>


}