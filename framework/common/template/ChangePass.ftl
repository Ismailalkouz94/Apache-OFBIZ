<#--
     <#assign username = Static["org.apache.ofbiz.humanres.PayrollServices"].getPartyId(request, response)/>-->

<div id="loginBar"><span>${uiLabelMap.CommonForgotYourPassword}?</span><div id="company-logo"></div></div>
<section id="widget-grid">
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.CommonPasswordChange}</h2>
</header>
<div role="content">
<div class="screenlet login-screenlet">
<div class="login-screenlet">
  <div class="screenlet-body">
    <form method="post" action="<@ofbizUrl>PasswordChanged</@ofbizUrl>" name="loginform">
      <table width="100%" cellspacing="0">
         <tr>
          <td><label>${uiLabelMap.CommonNewPassword}</label></td>
          <td><input class="form-control" type="password" name="newPassword" value="" size="20" required="true"/></td>
        </tr>
        <tr>
          <td><label>${uiLabelMap.CommonNewPasswordVerify}</label></td>
          <td><input class="form-control" type="password" name="newPasswordVerify" value="" size="20" required="true"/></td>
        </tr>
      
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
