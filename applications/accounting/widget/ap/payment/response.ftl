<#ftl>
<style>
h1 {
/*    border: 1px solid #305A72;*/
    margin-top: 100px;
    margin-bottom: 100px;
    margin-right: 150px;
    margin-left: 80px;
 font-size: 30px;
}
</style>
<#if "${session.getAttribute('view')}" =='1'>
<h1 align="center"> Your Request send succssfuly and request id : ${session.getAttribute('processInstanceId')}  </h1>
 
    <#else>
<h1 align="center"> Your Response send succssfuly  </h1>

</#if>