<#--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->
<#--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->
<style>

a, a:hover {
  color: black;
}
.container3 {
  max-width: 350px;
  padding: 15px;
  margin: 0 auto;
  border: 2px solid gray;
  -moz-border-radius: 15px;
  -webkit-border-radius: 15px;
  border-radius: 15px;
margin-top:70px;
}

.overlay {
  padding: 15px;
  background: rgba(255, 255, 255, 0.4);
  -moz-border-radius: 10px;
  -webkit-border-radius: 10px;
  border-radius: 10px;
  /*-webkit-filter: blur(5px);*/
}
h1 { color: black; }
.form-signin {
  color: black;
  font-family: 'Maven Pro', sans-serif;
}
.form-signin .form-signin-heading,
.form-signin .checkbox {
  margin-bottom: 10px;
}
.form-signin .checkbox { font-weight: normal; }
.form-signin .form-control {
  position: relative;
  height: auto;
  -webkit-box-sizing: border-box;
     -moz-box-sizing: border-box;
          box-sizing: border-box;
  font-size: 1.25em;
}
.form-signin .form-control:focus { z-index: 2; }

.rotate-45-left {
  -moz-transform: rotate(-45deg);
  -o-transform: rotate(-45deg);
  -webkit-transform: rotate(-45deg);
  transform: rotate(-45deg);
}
.rotate-45-right {
  -moz-transform: rotate(45deg);
  -o-transform: rotate(45deg);
  -webkit-transform: rotate(45deg);
  transform: rotate(45deg);
}
.footer3 {
  font-size: .75em;
  margin-top: 20px;
}
.header3 {
  font-family: 'Maven Pro', sans-serif;
  margin-bottom: 30px;
  text-align: center;
}
.loading {
  color: black;
  padding: 45px 15px;
  /*background: #eee;*/
  font-family: 'Maven Pro', sans-serif;
  text-align: center;
}

</style>
<#if requestAttributes.uiLabelMap??><#assign uiLabelMap = requestAttributes.uiLabelMap></#if>
<#assign useMultitenant = Static["org.apache.ofbiz.base.util.UtilProperties"].getPropertyValue("general.properties", "multitenant")>

<#assign username = requestParameters.USERNAME?default((sessionAttributes.autoUserLogin.userLoginId)?default(""))>
<#if username != "">
  <#assign focusName = false>
<#else>
  <#assign focusName = true>
</#if>
<div>
<div id="loginBar"><span>${uiLabelMap.CommonBeLogged}</span><div id="company-logo"></div></div>

 <div class="container" style="display: none;">
      <div class="overlay loading">
        <i class="fa fa-spinner fa-5x fa-spin"></i><br/>
        <h1>Loading...</h1>
      </div>
    </div>
 <div class="container3">
      <div class="overlay">
        <div class="screenlet-body">
        <h1 class="header3">
          <span>
        <img src="/images/BusinessInnovation/images/profile-img.png"/>
          </span></h1>
       <h1>   <span style="padding: 15px 0;font-size: 25px;color: #fff;font-weight: bold;">  LOGIN </span></h1>
        
    
      <form method="post" action="<@ofbizUrl>login</@ofbizUrl>" class="form-signin" name="loginform">
        <table class="basic-table" cellspacing="0">
          <tr class="input-group"  style="margin-bottom: 3px;">     
            <td><span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span></td>
            <td><input class="form-control" style="margin:0 !important;height: 30px !important; width: 250px;" type="text" placeholder="Username" name="USERNAME" value="${username}" size="20"/></td>
          </tr>
          <tr class="input-group"  style="margin-bottom: 3px;">         
            <td> <span class="input-group-addon"><i class="fa fa-unlock-alt fa-fw"></i></span></td>
            <td><input class="form-control" style="margin:0 !important;height: 30px !important;width: 250px;" placeholder="Password" type="password" name="PASSWORD" value="" size="20"/></td>
          </tr>
          <#if ("Y" == useMultitenant) >
              <#if !requestAttributes.userTenantId??>
                  <tr class="input-group"  style="margin-bottom: 3px;">  
                      <td><span class="input-group-addon"><i class="fa fa-copy fa-fw"></i></span></td>
                      <td><input class="form-control" placeholder="Tenant ID" style="margin:0 !important;height: 30px !important; width: 250px;" type="text" name="userTenantId" value="${parameters.userTenantId!}" size="20"/></td>
                  </tr>
              <#else>
                  <input type="hidden" class="form-control" placeholder="Tenant ID" style="margin:0 !important;height: 30px !important; width: 250px;" name="userTenantId" value="${requestAttributes.userTenantId!}"/>
              </#if>
          </#if>
          <tr>
            <td colspan="2" align="center">
              <input type="submit" class="btn btn-lg btn-primary btn-block" value="${uiLabelMap.CommonLogin}"/>
            </td>
          </tr>
          <tr><br/>
            <td colspan="2" align="center">
                <input type="hidden" name="JavaScriptEnabled" value="N"/>
               <a class="btn btn-lg btn-primary btn-block" href="<@ofbizUrl>forgotPassword_step1</@ofbizUrl>">${uiLabelMap.CommonForgotYourPassword}?</a>
            </td>
          </tr>
        </table>

      </form>
    </div>
    </div>
    </div>
    
    </div> <!-- /container -->
</div>


<script language="JavaScript" type="text/javascript">



  document.loginform.JavaScriptEnabled.value = "Y";
  <#if focusName>
    document.loginform.USERNAME.focus();
  <#else>
    document.loginform.PASSWORD.focus();
  </#if>
</script>



<#--

<div class="container3">
      <div class="overlay">
        <h1 class="header3">
          <span>
        <img src="/BusinessInnovation/images/profile-img.png"/>
          </span></h1>
       <h1>   <span style="padding: 15px 0;font-size: 25px;color: #fff;font-weight: bold;">  LOGIN </span></h1>
        
    
        <form class="form-signin" role="form"  method="post" action="<@ofbizUrl>login</@ofbizUrl>" name="loginform">
    
          <div class="input-group"  style="margin-bottom: 3px;">
            <span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>

            <input class="form-control" style="margin:0 !important;height: 30px !important;" name="USERNAME" value="${username}" size="20" type="text" placeholder="Username" required autofocus>
          </div>
          <div class="input-group" style="margin-bottom: 3px;">
            <span class="input-group-addon"><i class="fa fa-unlock-alt fa-fw"></i></span>
            <input class="form-control" style="margin:0 !important;height: 30px !important;" type="password" name="PASSWORD" value="" size="20" placeholder="Password" required/>
          </div>
          <div class="input-group" style="margin-bottom: 3px;">
           <#if ("Y" == useMultitenant) >
              <#if !requestAttributes.userTenantId??>
                  <span class="input-group-addon"><i class="fa fa-copy fa-fw"></i></span>
                 <input class="form-control" placeholder="Tenant ID" style="margin:0 !important;height: 30px !important;" type="text" name="userTenantId" value="${parameters.userTenantId!}" size="20"/></td>   
              <#else>
                   <span class="input-group-addon"><i class="fa fa-copy fa-fw"></i></span>
                  <input class="form-control" placeholder="Tenant ID" style="margin:0 !important ;height: 30px !important;" type="hidden" name="userTenantId" value="${requestAttributes.userTenantId!}"/>
              </#if>
          </#if>
          </div>

          <span><button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button><button class="btn btn-lg btn-primary btn-block"><a href="<@ofbizUrl>forgotPassword_step1</@ofbizUrl>" style="color:#fff">${uiLabelMap.CommonForgotYourPassword}?</a></button></span>
        </form>

      </div>-->