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

<#assign username = requestParameters.USERNAME?default((sessionAttributes.autoUserLogin.userLoginId)?default(""))>
<#assign tenantId = requestParameters.userTenantId!>
<div id="loginBar"><span>${uiLabelMap.CommonForgotYourPassword}?</span><div id="company-logo"></div></div>

<div class="screenlet login-screenlet">
<#assign forgotPwdFlag = parameters.forgotPwdFlag?has_content />
<section id="widget-grid">
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable"">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.CommonPasswordChange}</h2>
</header>
<div role="content">
<div class="login-screenlet">
  <div class="screenlet-body">
    <form method="post" action="<@ofbizUrl>login</@ofbizUrl>" name="loginform">
      <input type="hidden" name="requirePasswordChange" value="Y"/>
      <input type="hidden" name="USERNAME" value="${username}"/>
      <input type="hidden" name="userTenantId" value="${tenantId}"/>
      <input type="hidden" name="forgotPwdFlag" value="${parameters.forgotPwdFlag!}" />
      <table width="100%" cellspacing="0">
        <tr>
          <td><label>${uiLabelMap.CommonUsername}</label></td>
          <td>${username}</td>
        </tr>
        <#if forgotPwdFlag?has_content && forgotPwdFlag?string == "true">
          <tr>
            <td><input class="form-control" type="hidden" name="PASSWORD" value="${parameters.password!}" size="20"/></td>
          </tr>
        <#else>
          <tr>
            <td><label>${uiLabelMap.CommonCurrentPassword}</label></td>
            <td><input class="form-control" type="password" name="PASSWORD" value="" size="20" /></td>
          </tr>
        </#if>
        <tr>
          <td><label>${uiLabelMap.CommonNewPassword}</label></td>
          <td><input class="form-control" type="password" name="newPassword" value="" size="20"/></td>
        </tr>
        <tr>
          <td><label>${uiLabelMap.CommonNewPasswordVerify}</label></td>
          <td><input class="form-control" type="password" name="newPasswordVerify" value="" size="20"/></td>
        </tr>
        <#if securityQuestion?has_content>
          <tr>
            <td><label>${uiLabelMap.SecurityQuestiom}</label></td>
            <td>
              <input class="form-control" type="hidden" name="securityQuestion" value="${securityQuestion.enumId!}" />
                ${securityQuestion.description!}
            </td>
          </tr>
          <tr>
            <td><label>${uiLabelMap.SecurityAnswer}</label></td>
            <td><input class="form-control" type="text" class='inputBox' name="securityAnswer" id="SECURITY_ANSWER" value="" maxlength="100" /></td>
          </tr>
        </#if>
        <tr>
          <td colspan="2" align="center">
            <input class="btn btn-primary1" type="submit" value="${uiLabelMap.CommonSubmit}"/>
          </td>
        </tr>
      </table>
    </form>
  </div>
</div>
</div>
</div>
</article>
</div>
</section>

<script language="JavaScript" type="text/javascript">
  document.loginform.PASSWORD.focus();
</script>
