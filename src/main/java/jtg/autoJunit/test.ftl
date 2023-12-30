package ${pkg};

import org.junit.jupiter.api.Test;

<#list imports as impt>

    import ${impt};
</#list>

public class ${className}{

<#list mtdsName as mtd>
    @Test
    void ${mtd}(){

    }
</#list>


}