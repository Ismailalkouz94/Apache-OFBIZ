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

<script>
    <#assign prefUserLogin = (delegator.findOne("UserLogin", {"userLoginId" : userLogin.userLoginId }, true))!>
    ${session.setAttribute('FOLANG',"${(prefUserLogin.lastLocale)!}")}
     <#assign mainData = Static["org.apache.ofbiz.common.CommonEvents"].setFOLANGTopAppBar(request, response) />

</script>
<#if (requestAttributes.externalLoginKey)?exists><#assign externalKeyParam = "?externalLoginKey=" + requestAttributes.externalLoginKey?if_exists></#if>
<#if (externalLoginKey)?exists><#assign externalKeyParam = "?externalLoginKey=" + requestAttributes.externalLoginKey?if_exists></#if>
<#assign ofbizServerName = application.getAttribute("_serverId")?default("default-server")>
<#assign contextPath = request.getContextPath()>
<#assign displayApps = Static["org.apache.ofbiz.base.component.ComponentConfig"].getAppBarWebInfos(ofbizServerName, "main")>
<#assign displaySecondaryApps = Static["org.apache.ofbiz.base.component.ComponentConfig"].getAppBarWebInfos(ofbizServerName, "secondary")>
<#if person?has_content>
    <#assign avatarList = delegator.findByAnd("PartyContent", {"partyId" : person.partyId, "partyContentTypeId" : "LGOIMGURL"}, null, false)>
    <#if avatarList?has_content>
        <#assign avatar = Static["org.apache.ofbiz.entity.util.EntityUtil"].getFirst(avatarList)>
        <#assign avatarDetail = Static["org.apache.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("PartyContentDetail", {"partyId" : person.partyId, "contentId" : avatar.contentId}, null, false))>
    </#if>
</#if>
<#if userLogin?has_content>
<body class="">

    <#assign mainData = Static["org.apache.ofbiz.common.preferences.PreferenceServices"].GetUserPref(request, response) />


    <#list mainData as items >
    <script>
        if ("${items.userPrefTypeId}" == "skin") {
            var theskinis = "${items.userPrefValue}";
        }
        else {
            var theskinis = 0;
        }
        if ("${items.userPrefTypeId}" == "smart-fixed-header") {
            var thefixedheaderis = "${items.userPrefValue}";
        }
        else {
            var thefixedheaderis = 0;
        }
        if ("${items.userPrefTypeId}" == "smart-fixed-navigation") {
            var thefixednavis = "${items.userPrefValue}";
        }
        else {
            var thefixednavis = 0;
        }
        if ("${items.userPrefTypeId}" == "smart-fixed-ribbon") {
            var thefixedribbonis = "${items.userPrefValue}";
        }
        else {
            var thefixedribbonis = 0;
        }
        if ("${items.userPrefTypeId}" == "smart-fixed-container") {
            var thefixedcontaineris = "${items.userPrefValue}";
        }
        else {
            var thefixedcontaineris = 0;
        }
        if ("${items.userPrefTypeId}" == "smart-rtl") {
            var thesmartrtlis = "${items.userPrefValue}";
        }
        else {
            var thesmartrtlis = 0;
        }
        if ("${items.userPrefTypeId}" == "smart-topmenu") {
            var thesmarttopmenuis = "${items.userPrefValue}";
        }
        else {
            var thesmarttopmenuis = 0;
        }
        if ("${items.userPrefTypeId}" == "hidden-menu") {
            var thetogglemenu = "${items.userPrefValue}";
        }
        else {
            var thetogglemenu = 0;
        }

        if (theskinis != 0) {
            document.body.classList.add(theskinis);
        }
        if (thefixedheaderis != 0) {
            document.body.classList.add("fixed-header");
        }
        if (thefixednavis != 0) {
            document.body.classList.add("fixed-navigation");
            document.body.classList.add("fixed-header");
        }
        if (thefixedribbonis != 0) {
            document.body.classList.add("fixed-ribbon");
            document.body.classList.add("fixed-navigation");
            document.body.classList.add("fixed-header");
        }
        if (thefixedcontaineris != 0) {
            document.body.classList.add("container");
            document.documentElement.classList.add("hidden-menu-mobile-lock");
        }
        if (thesmartrtlis != 0) {
            document.body.classList.add("smart-rtl");
        }
        if (thesmarttopmenuis != 0) {
            document.body.classList.add("menu-on-top");
        }
        if (thetogglemenu != 0) {
            document.body.classList.add("hidden-menu");
            document.documentElement.classList.add("hidden-menu-mobile-lock");
        }
    </script>
    </#list>
    <header id="header">
        <div id="logo-group">
            <!-- PLACE YOUR LOGO HERE -->
            <span id="logo" style="margin-top:5px !important"> <img src="/images/BusinessInnovation/images/mylogo1.PNG"
                                                                    alt="Business Innovation"> <span
                    class="just-name"> ${userLogin.userLoginId}</span></span>
            <!-- END LOGO PLACEHOLDER -->

            <span id="activity" class="activity-dropdown"> <i class="fa fa-user"></i> <b id="bme1"></b></span>
            <div class="ajax-dropdown" style="display: none;">
                <div class="btn-group btn-group-justified" style="direction: initial;" data-toggle="buttons">
                    <label class="btn btn-default" id="msgfor">
                        <input name="activity" type="radio" id="ajax/notify/mail.html">
                        My Inbox (<b id="numofrow"></b>) </label>
                </div>
                <div class="ajax-notifications1 custom-scroll" id="adjkl">
                    <div id="myinner" class="alert alert-transparent">
                        <div id="totable">
		    <#assign tasks = Static["org.apache.ofbiz.common.activitiServices.WorkFlowServices"].getTasks(request, response)>
                            <table id="tab101" style="width:100%" class="table table-bordered table-striped">
                                <thead>
                                <tr>
                                    <th>Name</th>
                                    <th> Type</th>
                                    <th> Description</th>
                                    <th>Date</th>
                                </tr>
                                </thead>
                                <tbody id="tab111">
                                    ${session.setAttribute('view','2')}
                                     <#if tasks?has_content>
                                         <#list tasks.tasks as task >
                                    <tr>
                                        <td>
                                            <a href="<@ofbizUrl>ActivitiResponse?action=1&view=2&taskId=${(task.id)!''}&processName=${(task.type)!}&taskName=${(task.taskDefinitionKey)!}&processInstanceId=${(task.executionId)!}</@ofbizUrl>">${(task.fullName)!} </a>
                                        </td>
                                        <td> ${(task.type)!''}</td>
                                        <td> ${(task.description)!''}</td>
                                        <td> ${(task.createTime)!''} </td>
                                    </tr>
                                         </#list>

                                     <#else>
                        <div class="alert alert-info fade in">Has no tasks</div>
                                     </#if>
                                </tbody>
                            </table>

                        </div>
                        <script>
                            $("#msgfor").click(function () {
                                document.getElementById("myinner").innerHTML = document.getElementById("totable").innerHTML;
                            });
                        </script>
                    </div>
                </div>
                <span>
                    <button type="button" data-loading-text="<i class='fa fa-refresh fa-spin'></i> Loading..."
                            class="btn btn-xs btn-default pull-right">
                        <i class="fa fa-refresh"></i>
                        </button> 
                    </span>
            </div>
        </div>
        <div class="pull-right">

            <ul id="mobile-profile-img" class="header-dropdown-list hidden-xs padding-5">
                <li class="">
                    <a href="#" class="dropdown-toggle no-margin userdropdown" data-toggle="dropdown"> 
                                                      <#if person?exists>
                                                          <img src="/images/image_logo/${requestParameters.userTenantId!''}${userLogin.userLoginId}_101.png"
                                                               onerror="this.src='/images/BusinessInnovation/images/default-pic.jpg';"
                                                               alt="Personal Image" class="online"/>
                                                      <#else>
                       <img src="/images/image_logo/${requestParameters.userTenantId!''}${userLogin.userLoginId}_101.png"
                            onerror="this.src='/images/BusinessInnovation/images/default-pic.jpg';" alt="Personal Image"
                            class="online"/>
                                                      </#if>
                    </a>
                    <ul class="dropdown-menu pull-right">
                        <li>
                            <a style="margin-top: 5px;" href="<@ofbizUrl>changepassword</@ofbizUrl>"
                               class="padding-10 padding-top-0 padding-bottom-0"><i class="fa fa-cog"></i> Change
                                Password</a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="<@ofbizUrl>logout</@ofbizUrl>" class="padding-10 padding-top-5 padding-bottom-5"
                               data-action="userLogout"><i class="fa fa-sign-out fa-lg"></i>
                                <strong><u>L</u>ogout</strong></a>
                        </li>
                    </ul>
                </li>
            </ul>
            <div class="btn-header pull-right">
                <span><a href="javascript:void(0);" data-action="toggleMenu" id="toggleMenubd" title="Collapse Menu"><i
                        class="fa fa-reorder"></i></a></span>
            </div>
            <div id="logout" class="btn-header transparent pull-right">
                <span><a id="logout" href="<@ofbizUrl>logout</@ofbizUrl>" data-action="userLogout"
                         data-logout-msg="Are you sure you want to logout ?"><i class="fa fa-sign-out"></i></a></span>
            </div>
            <div id="fullscreen" class="btn-header transparent pull-right">
                <span> <a href="javascript:void(0);" data-action="launchFullscreen" id="fullScreen" title="Full Screen"><i
                        class="fa fa-arrows-alt"></i></a> </span>
            </div>
            <ul class="header-dropdown-list hidden-xs" id="langul">
                <li>
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" id="langflag"> </a>
                    <ul class="dropdown-menu pull-right">
                        <li>
                            <a href="<@ofbizUrl>setSessionLocale</@ofbizUrl>?newLocale=en" onclick="setlang(0);"><img
                                    src="/images/BusinessInnovation/images/us.png" class="flag flag-us"
                                    alt="United States"> English (US)</a>
                        </li>
                        <li>
                            <a href="<@ofbizUrl>setSessionLocale</@ofbizUrl>?newLocale=ar" onclick="setlang(1);"><img
                                    src="/images/BusinessInnovation/images/blank.gif" class="flag flag-jo" alt="Jordan">
                                Arabic (AR) </a>
                        </li>
                    </ul>
                </li>
            </ul>
            <script>
                if (localStorage.getItem("langidis") == 0) {
                    document.getElementById("langflag").innerHTML = '<img src="/images/BusinessInnovation/images/us.png" class="flag flag-us" alt="United States"> <span> English (US) </span> <i class="fa fa-angle-down"></i>';
                }
                if (localStorage.getItem("langidis") === null) {
                    document.getElementById("langflag").innerHTML = '<img src="/images/BusinessInnovation/images/us.png" class="flag flag-us" alt="United States"> <span> English (US) </span> <i class="fa fa-angle-down"></i>';
                }
                if (localStorage.getItem("langidis") == 1) {
                    document.getElementById("langflag").innerHTML = '<img src="/images/BusinessInnovation/images/blank.gif" class="flag flag-jo" alt="Jordan"> <span> Arabic (AR) </span> <i class="fa fa-angle-down"></i>';
                }

                function setlang(langid) {
                    if (langid == 1) {
                        $.ajax({
                            type: "post",
                            url: "setUserPreference",
                            data: {
                                userPrefGroupTypeId: 'GLOBAL_PREFERENCES',
                                userPrefTypeId: 'smart-rtl',
                                userPrefValue: '1'
                            },
                            dataType: 'json',
                        });
                    }
                    else {
                        $.ajax({
                            type: "post",
                            url: "setUserPreference",
                            data: {
                                userPrefGroupTypeId: 'GLOBAL_PREFERENCES',
                                userPrefTypeId: 'smart-rtl',
                                userPrefValue: '0'
                            },
                            dataType: 'json',
                        });
                    }
                    localStorage.setItem('langidis', langid);
                    var ttt = localStorage.getItem('langidis');

                    if (ttt == 0) {
                        document.getElementById("langflag").innerHTML = '<img src="/images/BusinessInnovation/images/us.png" class="flag flag-us" alt="United States"> <span> English (US) </span> <i class="fa fa-angle-down"></i>';

                    }
                    else {
                        document.getElementById("langflag").innerHTML = '<img src="/images/BusinessInnovation/images/jo.jpg" class="flag flag-fr" alt="Jordan"> <span> Arabic (AR) </span> <i class="fa fa-angle-down"></i>';

                    }
                }

                var langyiny = localStorage.getItem('langidis');
                if (langyiny == 0) {
                    $('body').removeClass("smart-rtl");
                }
                if (langyiny == 1) {
                    $('body').addClass("smart-rtl");
                }
            </script>
        </div>
    </header>
<aside id="left-panel">
    <div class="login-info">
     <span>
         <font onclick="changemyproimage()" id="show-shortcut">
	    <#if person?exists>
		<#if person.firstName??>
            <#if avatarDetail??>
		    <img id="myimg" src="/images/image_logo/${requestParameters.userTenantId!''}${userLogin.userLoginId}_101.png"
                 onerror="this.src='/images/BusinessInnovation/images/default-pic.jpg';"
                 alt="Please Insert Personal Image" class="online">
            <#else>
		    <img id="myimg" src="/images/image_logo/${requestParameters.userTenantId!''}${userLogin.userLoginId}_101.png"
                 onerror="this.src='/images/BusinessInnovation/images/default-pic.jpg';"
                 alt="Please Insert Personal Image" class="online">
            </#if>
		    <span id="showfirst">${person.firstName} ${person.lastName?upper_case}</span>
        <#else>
		    <img src="/images/image_logo/${requestParameters.userTenantId!''}${userLogin.userLoginId}_101.png"
                 onerror="this.src='/images/BusinessInnovation/images/default-pic.jpg';"
                 alt="Please Insert Personal Image" class="online">
		    <span id="showfirst">${userLogin.userLoginId}</span>
        </#if>
        <#else>
		    <img src="/images/image_logo/${requestParameters.userTenantId!''}${userLogin.userLoginId}_101.png"
                 onerror="this.src='/images/BusinessInnovation/images/default-pic.jpg';"
                 alt="Please Insert Personal Image" class="online">
		    <span id="showfirst">${userLogin.userLoginId}</span>
        </#if>
         </font>
       </span>
    </div>
    <nav style="">
        <ul id="ifhy">
 <#assign permission = true>
<#assign permissions = displayApps[0].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" class="">
                    <a href="#" title="Accounting"><i class="fa fa-lg fa-fw fa-bar-chart-o"></i><span
                            class="menu-item-parent">${uiLabelMap[displayApps[0].title]}</span></a>
                    <ul style="">
                        <li id="1">
                            <a href="/accounting/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="2">
                            <a href="#">${uiLabelMap.AccountingInvoicesMenu}</a>
                            <ul>
                                <li id="01">
                                    <a href="/accounting/control/findInvoices${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                                </li>
                                <li id="02">
                                    <a href="/accounting/control/CommissionRun${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingCommissionRun}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="3">
                            <a href="/accounting/control/findPayments${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingPaymentsMenu}</a>
                        </li>
                        <li id="4">
                            <a href="/accounting/control/FindPaymentGroup${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingPaymentGroup}</a>
                        </li>
                        <li id="5">
                            <a href="/accounting/control/FindTaxAuthority${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingTaxAuthorities}</a>
                        </li>
                        <li id="6">
                            <a href="#">${uiLabelMap.AccountingTransactions}</a>
                            <ul>
                                <li id="03">
                                    <a href="/accounting/control/CustomFindAcctgTrans${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.GLTransaction}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="7">
                            <a href="#">${uiLabelMap.AccountingFinAccount}</a>
                            <ul>
                                <li id="04">
                                    <a href="/accounting/control/FinAccountMain${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                                </li>
                                <li id="05">
                                    <a href="/accounting/control/FindFinAccount${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleFindFinAccount}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="8">
                            <a href="/accounting/control/FindAgreement${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingAgreements}</a>
                        </li>
                        <li id="9">
                            <a href="/accounting/control/ListFixedAssets${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingFixedAssets}</a>
                        </li>
                        <li id="10">
                            <a href="/accounting/control/ListBudgets${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingBudgets}</a>
                        </li>
                        <li id="11">
                            <a href="/accounting/control/globalGLSettings${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingGlobalGLSettings}</a>
                        </li>
                        <li id="12">
                            <a href="/accounting/control/ListCompanies${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingOrgGlSettings}</a>
                        </li>
                    </ul>
                </li>
</#if>


 <#assign permission = true>
<#assign permissions = displayApps[1].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" class="">
                    <a href="#" title="Ap-Accounting"><i class="fa fa-lg fa-fw fa-arrow-circle-left"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[1].title]}</span></a>
                    <ul>
                        <li id="13">
                            <a href="/ap/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="14">
                            <a href="/ap/control/FindAgreement${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingAgreements}</a>
                        </li>
                        <li id="15">
                            <a href="#">${uiLabelMap.AccountingInvoicesMenu}</a>
                            <ul>
                                <li id="06">
                                    <a href="/ap/control/FindApInvoices${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                                </li>
                                <li id="07">
                                    <a href="/ap/control/FindPurchaseInvoices${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingPurchaseInvoices}</a>
                                </li>
                                <li id="08">
                                    <a href="/ap/control/CommissionRun${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingCommissionRun}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="16">
                            <a href="/ap/control/findPayments${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingPaymentsMenu}</a>
                        </li>
                        <li id="17">
                            <a href="/ap/control/FindApPaymentGroups${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingApPaymentGroupMenu}</a>
                        </li>
                        <li id="18">
                            <a href="/ap/control/findVendors${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingApPageTitleFindVendors}</a>
                        </li>
                        <li id="19">
                            <a href="#">${uiLabelMap.AccountingReports}</a>
                            <ul>
                                <li id="09">
                                    <a href="/ap/control/listReports${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                                </li>
                                <li id="010">
                                    <a href="/ap/control/FindCommissions${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingCommissionReport}</a>
                                </li>
                                <li id="011">
                                    <a href="/ap/control/reportTrialBalance${StringUtil.wrapString(externalKeyParam)}&Report=1">${uiLabelMap.VendorStatement}</a>
                                </li>
                            </ul>
                        </li>

                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[2].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" class="">
                    <a href="#" title="Ar-Accounting"><i class="fa fa-lg fa-fw fa-arrow-circle-right"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[2].title]}</span></a>
                    <ul>
                        <li id="20">
                            <a href="/ar/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="21">
                            <a href="/ar/control/FindAgreement${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingAgreements}</a>
                        </li>
                        <li id="22">
                            <a href="/ar/control/findInvoices${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingInvoicesMenu}</a>
                        </li>
                        <li id="23">
                            <a href="#">${uiLabelMap.AccountingPaymentsMenu}</a>
                            <ul>
                                <li id="012">
                                    <a href="/ar/control/findPayments${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleFindPayment}</a>
                                </li>
                                <li id="013">
                                    <a href="/ar/control/batchPayments${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleBatchPayments}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="24">
                            <a href="/ar/control/FindArPaymentGroups${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingArPaymentGroupMenu}</a>
                        </li>
                        <li id="25">
                            <a href="/ar/control/ListReports${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingReports}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[3].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" class="">
                    <a href="#" title="Asset Maint "><i class="fa fa-lg fa-fw fa-paste"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[3].title]}</span></a>
                    <ul>
                        <li id="26">
                            <a href="/assetmaint/control/mytasks${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortTaskList}</a>
                        </li>
                        <li id="27">
                            <a href="/assetmaint/control/ListFixedAssets${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingFixedAssets}</a>
                        </li>
                        <li id="28">
                            <a href="/assetmaint/control/findFixedAssetMaints${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AccountingFixedAssetMaints}</a>
                        </li>
                    </ul>
                </li>
</#if>
 <#assign permission = true>
<#assign permissions = displayApps[4].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" class="">
                    <a href="#" title="Catalog"><i class="fa fa-lg fa-fw fa-book"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[4].title]}</span></a>
                    <ul>
                        <li id="29">
                            <a href="/catalog/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="30">
                            <a href="/catalog/control/FindCatalog${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductCatalogs}</a>
                        </li>
                        <li id="31">
                            <a href="/catalog/control/FindCategory${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductCategories}</a>
                        </li>
                        <li id="32">
                            <a href="/catalog/control/FindProduct${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductProducts}</a>
                        </li>
                        <li id="33">
                            <a href="/catalog/control/EditFeatureCategories${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductFeatureCats}</a>
                        </li>
                        <li id="34">
                            <a href="/catalog/control/FindProductPromo${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductPromos}</a>
                        </li>
                        <li id="35">
                            <a href="/catalog/control/FindProductPriceRules${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductPriceRules}</a>
                        </li>
                        <li id="36">
                            <a href="/catalog/control/FindProductStore${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductStores}</a>
                        </li>
                        <li id="37">
                            <a href="/catalog/control/ListParentProductStoreGroup${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductProductStoreGroups}</a>
                        </li>
                        <li id="38">
                            <a href="/catalog/control/editKeywordThesaurus${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductThesaurus}</a>
                        </li>
                        <li id="39">
                            <a href="/catalog/control/FindReviews${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductReviews}</a>
                        </li>
                        <li id="40">
                            <a href="/catalog/control/FindProductConfigItems${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductConfigItems}</a>
                        </li>
                        <li id="41">
                            <a href="/catalog/control/FindSubscription${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductSubscriptions}</a>
                        </li>
                        <li id="42">
                            <a href="/catalog/control/ListShipmentMethodTypes${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductShipping}</a>
                        </li>
                        <li id="43">
                            <a href="/catalog/control/Imagemanagement${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ImageManagement}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[5].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" class="">
                    <a href="#" title="Content"><i class="fa fa-lg fa-fw fa-tasks"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[5].title]}</span></a>
                    <ul>
                        <li id="44">
                            <a href="/content/control/FindWebSite${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentWebSite}</a>
                        </li>
                        <li id="45">
                            <a href="/content/control/FindSurvey${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentSurvey}</a>
                        </li>
                        <li id="46">
                            <a href="/content/control/findForumGroups${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentForum}</a>
                        </li>
                        <li id="47">
                            <a href="/content/control/blogMain${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentBlog}</a>
                        </li>
                        <li id="48">
                            <a href="#">${uiLabelMap.ContentContent}</a>
                            <ul>
                                <li id="014">
                                    <a href="/content/control/findContent${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonFind}</a>
                                </li>
                                <li id="015">
                                    <a href="/content/control/navigateContent${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentNavigate}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="49">
                            <a href="#">${uiLabelMap.ContentDataResource}</a>
                            <ul>
                                <li id="016">
                                    <a href="/content/control/findDataResource${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonFind}</a>
                                </li>
                                <li id="017">
                                    <a href="/content/control/navigateDataResource${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentNavigate}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="50">
                            <a href="#">${uiLabelMap.ContentContentSetup}</a>
                            <ul>
                                <li id="018">
                                    <a target="_top"
                                       href="/content/control/EditContentPurposeOperation${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleEditContentPurposeOperation}</a>
                                </li>
                                <li id="019">
                                    <a target="_top"
                                       href="/content/control/EditContentOperation${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleEditContentOperation}</a>
                                </li>
                                <li id="020">
                                    <a target="_top"
                                       href="/content/control/EditContentAssocPredicate${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleEditContentAssocPredicate}</a>
                                </li>
                                <li id="021">
                                    <a target="_top"
                                       href="/content/control/EditContentTypeAttr${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleEditContentTypeAttribute}</a>
                                </li>
                                <li id="022">
                                    <a target="_top"
                                       href="/content/control/EditContentPurposeType${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleEditContentPurposeType}</a>
                                </li>
                                <li id="023">
                                    <a target="_top"
                                       href="/content/control/EditContentAssocType${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleEditContentAssocType}</a>
                                </li>
                                <li id="024">
                                    <a target="_top"
                                       href="/content/control/ContentSetupMenu${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleEditContentType}</a>
                                </li>
                                <li id="025">
                                    <a name="UserPermissions" target="_top"
                                       href="/content/control/UserPermissions${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleEditContentUserPermissions}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="51">
                            <a href="#">${uiLabelMap.ContentDataSetup}</a>
                            <ul>
                                <li id="026">
                                    <a target="_top"
                                       href="/content/control/DataSetupMenu${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonType}</a>
                                </li>
                                <li id="027">
                                    <a target="_top"
                                       href="/content/control/EditCharacterSet${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentCharacterSet}</a>
                                </li>
                                <li id="028">
                                    <a target="_top"
                                       href="/content/control/EditDataCategory${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentCategory}</a>
                                </li>
                                <li id="029">
                                    <a target="_top"
                                       href="/content/control/EditDataResourceTypeAttr${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentTypeAttr}</a>
                                </li>
                                <li id="030">
                                    <a target="_top"
                                       href="/content/control/EditFileExtension${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentFileExt}</a>
                                </li>
                                <li id="031">
                                    <a target="_top"
                                       href="/content/control/EditMetaDataPredicate${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentMetaDataPred}</a>
                                </li>
                                <li id="032">
                                    <a target="_top"
                                       href="/content/control/EditMimeType${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentMimeType}</a>
                                </li>
                                <li id="033">
                                    <a target="_top"
                                       href="/content/control/EditMimeTypeHtmlTemplate${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentMimeTypeHtmlTemplate}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="52">
                            <a href="#">${uiLabelMap.ContentTemplate}</a>
                            <ul>
                                <li id="034">
                                    <a target="_top"
                                       href="/content/control/LayoutMenu${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentListOwnCreatedTemplates}</a>
                                </li>
                                <li id="035">
                                    <a target="_top"
                                       href="/content/control/FindLayout${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonFind}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="53">
                            <a href="#">${uiLabelMap.ContentCMS}</a>
                            <ul>
                                <li id="036">
                                    <a id="contentfind"
                                       href="/content/control/CMSContentFind${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentContent}</a>
                                </li>
                                <li id="037">
                                    <a id="sites"
                                       href="/content/control/CMSSites?${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentSubSites}</a>
                                </li>
                                <li id="038">
                                    <a id="index"
                                       href="/content/control/AdminIndex${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentIndex}</a>
                                </li>
                                <li id="039">
                                    <a id="search"
                                       href="/content/control/AdminSearch${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonFind}</a>
                                </li>
                                <li id="040">
                                    <a id="productSearch"
                                       href="/content/control/ProductSearch${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SearchProducts}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="54">
                            <a href="/content/control/FindCompDoc${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ContentCompDoc}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[6].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" class="">
                    <a href="#" title="Facility"><i class="fa fa-lg fa-fw fa-home"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[6].title]}</span></a>
                    <ul>
                        <li id="55">
                            <a href="/facility/control/FindFacility${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductFacilities}</a>
                        </li>
                        <li id="56">
                            <a href="/facility/control/FindFacilityGroup${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductFacilityGroups}</a>
                        </li>
                        <li id="57">
                            <a href="#">${uiLabelMap.ProductInventoryItemLabels}</a>
                            <ul>
                                <li id="041">
                                    <a href="/facility/control/FindInventoryItemLabels${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductInventoryItemLabels}</a>
                                </li>
                                <li id="042">
                                    <a href="/facility/control/EditInventoryItemLabelTypes${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductInventoryItemLabelTypes}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="58">
                            <a href="#">${uiLabelMap.FacilityShipmentGatewayConfig}</a>
                            <ul>
                                <li id="043">
                                    <a href="/facility/control/FindShipmentGatewayConfig${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.FacilityShipmentGatewayConfig}</a>
                                </li>
                                <li id="044">
                                    <a href="/facility/control/FindShipmentGatewayConfigTypes${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.FacilityShipmentGatewayConfigTypes}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="59">
                            <a href="/facility/control/FindShipment${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductShipments}</a>
                        </li>
                    </ul>
                </li>
</#if>
 <#assign permission = true>
<#assign permissions = displayApps[7].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" class="">
                    <a href="#" title="HR"><i class="fa fa-lg fa-fw fa-user"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[7].title]}</span></a>
                    <ul>
                        <li id="60">
                            <a href="/humanres/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="61">
                            <a href="/humanres/control/findEmployees${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResEmployees}</a>
                        </li>
                        <li id="62">
                            <a href="/humanres/control/FindEmployments${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResEmployment}</a>
                        </li>
                        <li id="63">
                            <a href="/humanres/control/FindEmplPositions${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResEmployeePosition}</a>
                        </li>
                        <li id="64">
                            <a href="/humanres/control/findFulfillments${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResPositionGradation}</a>
                        </li>
                        <li id="65">
                            <a href="/humanres/control/FindPerfReviews${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResPerfReview}</a>
                        </li>
                        <li id="66">
                            <a href="/humanres/control/FindPartySkills${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResSkills}</a>
                        </li>
                        <li id="67">
                            <a href="/humanres/control/FindPartyQuals${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResPartyQualification}</a>
                        </li>
                        <li id="68">
                            <a href="#">${uiLabelMap.HumanResRecruitment}</a>
                            <ul>
                                <li id="045">
                                    <a href="/humanres/control/FindJobRequisitions${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResJobRequisition}</a>
                                </li>
                                <li id="046">
                                    <a href="/humanres/control/FindInternalJobPosting${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResInternalJobPosting}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="69">
                            <a href="/humanres/control/TrainingCalendar${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResTraining}</a>
                        </li>
                        <li id="70">
                            <a href="/humanres/control/FindEmploymentApps${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResEmploymentApp}</a>
                        </li>
                        <li id="71">
                            <a href="/humanres/control/FindPartyResumes${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResPartyResume}
                                <span class="menu-item-parent"></span></a>
                        </li>
                        <li id="72">
                            <a href="#">${uiLabelMap.HumanResEmplLeave}</a>
                            <ul>
                                <li id="047">
                                    <a href="/humanres/control/vacationsView${StringUtil.wrapString(externalKeyParam)}">Vacations</a>
                                </li>
                                <li id="048">
                                    <a href="/humanres/control/EmplLeaves${StringUtil.wrapString(externalKeyParam)}">Leaves</a>
                                </li>
                                <li id="049">
                                    <a href="/humanres/control/PartyVacationContract${StringUtil.wrapString(externalKeyParam)}">Vacation Contract</a>
                                </li>

                                <li id="050">
                                    <a href="/humanres/control/PartyVacationTransaction${StringUtil.wrapString(externalKeyParam)}">Vacation Transaction</a>
                                </li>

                                <li id="051">
                                    <a href="/humanres/control/VacationBalance${StringUtil.wrapString(externalKeyParam)}">Vacation Balance</a>
                                </li>

                            </ul>
                        </li>

                        <li id="73">
                            <a href="/humanres/control/EmployeeInsurance${StringUtil.wrapString(externalKeyParam)}">Insurance</a>
                        </li>
                        <li id="74">
                            <a href="/humanres/control/TimeAttendanceMain${StringUtil.wrapString(externalKeyParam)}">Time
                                Attendance</a>
                        </li>

                        <li id="75">
                            <a href="/humanres/control/EditSkillTypes${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.HumanResGlobalHRSettings}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[8].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" class="">
                    <a href="#" title="Manufacturing"><i class="fa fa-lg fa-fw fa-industry"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[8].title]}</span></a>
                    <ul>
                        <li id="76">
                            <a href="/manufacturing/control/FindProductionRun${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ManufacturingJobShop}
                                <span class="menu-item-parent"></span></a>
                        </li>
                        <li id="78">
                            <a href="/manufacturing/control/FindRouting${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ManufacturingRouting}
                                <span class="menu-item-parent"></span></a>
                        </li>
                        <li id="79">
                            <a href="/manufacturing/control/FindRoutingTask${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ManufacturingRoutingTask}
                                <span class="menu-item-parent"></span></a>
                        </li>
                        <li id="80">
                            <a href="#">${uiLabelMap.ManufacturingCalendar}</a>
                            <ul>
                                <li id="049">
                                    <a href="/manufacturing/control/FindCalendar${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ManufacturingCalendars}</a>
                                </li>
                                <li id="050">
                                    <a href="/manufacturing/control/ListCalendarWeek${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ManufacturingCalendarWeeks}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="81">
                            <a href="/manufacturing/control/EditCostCalcs${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ManufacturingCostCalcs}</a>
                        </li>
                        <li id="82">
                            <a href="#">${uiLabelMap.ManufacturingBillOfMaterials}</a>
                            <ul>
                                <li id="051">
                                    <a href="/manufacturing/control/FindBom${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonFind}</a>
                                </li>
                                <li id="052">
                                    <a href="/manufacturing/control/BomSimulation${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ManufacturingBomSimulation}</a>
                                </li>
                                <li id="053">
                                    <a href="/manufacturing/control/EditProductBom${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ManufacturingEditProductBom}</a>
                                </li>
                                <li id="054">
                                    <a href="/manufacturing/control/EditProductManufacturingRules${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ManufacturingManufacturingRules}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="83">
                            <a href="#">${uiLabelMap.ManufacturingMrp}</a>
                            <ul>
                                <li id="055">
                                    <a href="/manufacturing/control/RunMrp${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ManufacturingRunMrp}</a>
                                </li>
                                <li id="056">
                                    <a href="/manufacturing/control/FindInventoryEventPlan${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ManufacturingMrpLog}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="84">
                            <a href="/manufacturing/control/WorkWithShipmentPlans${StringUtil.wrapString(externalKeyParam)}"> ${uiLabelMap.ManufacturingShipmentPlans}
                                <span class="menu-item-parent"></span></a>
                        </li>
                        <li id="85">
                            <a href="/manufacturing/control/ManufacturingReports${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ManufacturingReports}
                                <span class="menu-item-parent"></span></a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[9].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" id="mar1">
                    <a href="#" title="Marketing"><i class="fa fa-lg fa-fw fa-group"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[9].title]}</span></a>
                    <ul>
                        <li id="86">
                            <a href="/marketing/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="87">
                            <a href="#">${uiLabelMap.DataSource}</a>
                            <ul>
                                <li id="057">
                                    <a href="/marketing/control/FindDataSource${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.DataSource}</a>
                                </li>
                                <li id="058">
                                    <a href="/marketing/control/FindDataSourceType${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.DataSourceType}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="88">
                            <a href="/marketing/control/FindMarketingCampaign${StringUtil.wrapString(externalKeyParam)}"><span
                                    class="menu-item-parent">${uiLabelMap.MarketingCampaign}</span></a>
                        </li>
                        <li id="89">
                            <a href="#">${uiLabelMap.MarketingTracking}</a>
                            <ul>
                                <li id="059">
                                    <a href="/marketing/control/FindTrackingCode${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.MarketingTrackingCode}</a>
                                </li>
                                <li id="060">
                                    <a href="/marketing/control/FindTrackingCodeType${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.MarketingTrackingCodeType}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="90">
                            <a href="/marketing/control/FindSegmentGroup${StringUtil.wrapString(externalKeyParam)}"><span
                                    class="menu-item-parent">${uiLabelMap.MarketingSegment}</span></a>
                        </li>
                        <li id="91">
                            <a href="/marketing/control/FindContactLists${StringUtil.wrapString(externalKeyParam)}"><span
                                    class="menu-item-parent">${uiLabelMap.MarketingContactList}</span></a>
                        </li>
                        <li id="92">
                            <a href="/marketing/control/MarketingReport${StringUtil.wrapString(externalKeyParam)}"><span
                                    class="menu-item-parent">${uiLabelMap.MarketingReports}</span></a>
                        </li>
                    </ul>
                </li>
</#if>

<#assign permission = true>
<#assign permissions = displayApps[10].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>     
	     <li onclick="openthisul(this);" id="port1">
             <a href="#" title="My Portal" id="myPortal"><i class="fa fa-lg fa-fw fa-television"></i> <span
                     class="menu-item-parent">${uiLabelMap[displayApps[10].title]}</span></a>
             <ul>
                 <li id="93">
                     <a href="/myportal/control/Home${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.Home}</a>
                 </li>
             </ul>
         </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[11].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" id="ord1">
                    <a href="#" title="Order"><i class="fa fa-lg fa-fw fa-file-text"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[11].title]}</span></a>
                    <ul>
                        <li id="94">
                            <a href="/ordermgr/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="95">
                            <a href="/ordermgr/control/FindRequest${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderRequests}</a>
                        </li>
                        <li id="96">
                            <a href="/ordermgr/control/FindQuote${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderOrderQuotes}</a>
                        </li>
                        <li id="97">
                            <a href="/ordermgr/control/orderlist${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderOrderList}</a>
                        </li>
                        <li id="98">
                            <a href="/ordermgr/control/findorders${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderFindOrder}</a>
                        </li>
                        <li id="99">
                            <a href="/ordermgr/control/orderentry${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderOrderEntry}</a>
                        </li>
                        <li id="100">
                            <a href="/ordermgr/control/findreturn${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderOrderReturns}</a>
                        </li>
                        <li id="101">
                            <a href="/ordermgr/control/FindRequirements${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderRequirements}</a>
                        </li>
                        <li id="102">
                            <a href="/ordermgr/control/OrderPurchaseReportOptions${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonReports}</a>
                        </li>
                        <li id="103">
                            <a href="/ordermgr/control/orderstats${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonStats}</a>
                        </li>
                    </ul>
                </li>
</#if>

<#assign permission = true>
<#assign permissions = displayApps[12].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" id="par1">
                    <a href="#" title="Party"><i class="fa fa-lg fa-fw fa-credit-card"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[12].title]}</span></a>
                    <ul>
                        <li id="104">
                            <a href="/partymgr/control/findparty${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyParties}</a>
                        </li>
                        <li id="105">
                            <a href="/partymgr/control/MyCommunicationEvents${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyMyCommunications}</a>
                        </li>
                        <li id="106">
                            <a href="#">${uiLabelMap.PartyCommunications}</a>
                            <ul>
                                <li id="061">
                                    <a href="/partymgr/control/FindCommunicationEvents${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonFind}</a>
                                </li>
                                <li id="062">
                                    <a href="/partymgr/control/listUnknownPartyComms${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyEmailFromUnknownParties}</a>
                                </li>
                                <li id="063">
                                    <a href="/partymgr/control/FindCommunicationByOrder${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyFindCommunicationsByOrder}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="107">
                            <a href="/partymgr/control/findVisits${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyVisits}</a>
                        </li>
                        <li id="108">
                            <a href="/partymgr/control/listLoggedInUsers${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyLoggedInUsers}</a>
                        </li>
                        <li id="109">
                            <a href="/partymgr/control/showclassgroups${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyClassifications}</a>
                        </li>
                        <li id="110">
                            <a href="/partymgr/control/FindUserLogin${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonSecurity}</a>
                        </li>
                        <li id="111">
                            <a href="/partymgr/control/addressMatchMap${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleAddressMatchMap}</a>
                        </li>
                        <li id="112">
                            <a href="/partymgr/control/partyInvitation${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyInvitation}</a>
                        </li>
                        <li id="113">
                            <a href="/partymgr/control/ImportExport${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonImportExport}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[13].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" id="payr1">
                    <a href="#" title="Payroll"><i class="fa fa-lg fa-fw fa-money"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[13].title]}</span></a>
                    <ul>
                        <li id="114">
                            <a href="/Payroll/control/References${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.References}</a>
                        </li>
                        <li id="115">
                            <a href="/Payroll/control/Allowences${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.Allowences}</a>
                        </li>
                        <li id="116">
                            <a href="/Payroll/control/AllowencesEmployee${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AllowenceEmployee}</a>
                        </li>
                        <li id="117">
                            <a href="/Payroll/control/SalaryScale${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SalaryScale}</a>
                        </li>

                        <li id="118">
                            <a href="/Payroll/control/SocialSecurityCalculation${StringUtil.wrapString(externalKeyParam)}">Social
                                Security Calculation</a>
                        </li>
                        <li id="119">
                            <a href="/Payroll/control/IncomeTax${StringUtil.wrapString(externalKeyParam)}">Income Tax
                                Calculation</a>
                        </li>
                        <li id="120">
                            <a href="/Payroll/control/SalaryCalculation${StringUtil.wrapString(externalKeyParam)}">Salary
                                Calculation</a>
                        </li>
                        <li id="121">
                            <a href="/Payroll/control/allowencesCompanyExpense${StringUtil.wrapString(externalKeyParam)}">Allowences
                                Company Expenses</a>
                        </li>
                        <li id="172">
                            <a href="#">Employee Expense</a>
                            <ul>
                                <li id="0122">
                                    <a href="/Payroll/control/endOfService${StringUtil.wrapString(externalKeyParam)}">End
                                        of Service </a>
                                </li>
                                <li id="0123">
                                    <a href="/Payroll/control/expense${StringUtil.wrapString(externalKeyParam)}">Expense</a>
                                </li>
                                <li id="0124">
                                    <a href="/Payroll/control/cashExpense${StringUtil.wrapString(externalKeyParam)}">Cash Expense</a>
                                </li>
                            </ul>
                        </li>

                        <li id="122">
                            <a href="/Payroll/control/loans${StringUtil.wrapString(externalKeyParam)}">Loan Managment</a>
                        </li>

                        <li id="123">
                            <a href="/Payroll/control/GlobalPayrollSetting${StringUtil.wrapString(externalKeyParam)}">Global
                                Payroll Setting</a>
                        </li>
                        <li id="124">
                            <a href="#">Reports</a>
                            <ul>
                                <li id="064">
                                    <a href="/Payroll/control/SalarySlipAll">Salary Slip</a>
                                </li>
                                <li id="065">
                                    <a href="/Payroll/control/SalarySlip${StringUtil.wrapString(externalKeyParam)}">Salary
                                        Slip Details</a>
                                </li>
                                <li id="0121">
                                    <a href="/Payroll/control/cashRequests${StringUtil.wrapString(externalKeyParam)}">Cash
                                        Requests</a>
                                </li>
                                <li id="0120">
                                    <a href="/Payroll/control/costCentersPayrollExp${StringUtil.wrapString(externalKeyParam)}">Cost
                                        Centers</a>
                                </li>
                                <li id="0125">
                                    <a href="/Payroll/control/costCentersPayrollExpDetails${StringUtil.wrapString(externalKeyParam)}">Cost
                                        Centers Details</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </li>
</#if>

<#assign permission = true>
<#assign permissions = displayApps[14].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" id="pro1">
                    <a href="#" title="Project"><i class="fa fa-lg fa-fw fa-folder-open"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[14].title]}</span></a>
                    <ul>
                        <li id="124">
                            <a href="/projectmgr/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="125">
                            <a href="/projectmgr/control/MyTasks${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortMyTasks}</a>
                        </li>
                        <li id="126">
                            <a href="/projectmgr/control/MyTimesheet${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortTimesheetMyTime}</a>
                        </li>
                        <li id="127">
                            <a href="/projectmgr/control/FindProject${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortProjects}</a>
                        </li>
                        <li id="128">
                            <a href="/projectmgr/control/FindTask${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProjectMgrTasks}</a>
                        </li>
                        <li id="129">
                            <a href="/projectmgr/control/FindResource${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProjectMgrResources}</a>
                        </li>
                        <li id="130">
                            <a href="/projectmgr/control/FindTimeSheet${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortTimesheet}</a>
                        </li>
                        <li id="131">
                            <a href="/projectmgr/control/findSkillTypes${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProjectMgrSkillType}</a>
                        </li>
                        <li id="132">
                            <a href="/projectmgr/control/requestlist${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortRequestList}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[15].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" id="sfa1">
                    <a href="#" title="Sfa"><i class="fa fa-lg fa-fw fa-gg"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[15].title]}</span></a>
                    <ul>
                        <li id="133">
                            <a href="/sfa/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="134">
                            <a href="/sfa/control/FindAccounts${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SfaAcccounts}</a>
                        </li>
                        <li id="135">
                            <a href="/sfa/control/FindContacts${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SfaContacts}</a>
                        </li>
                        <li id="136">
                            <a href="/sfa/control/FindLeads${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SfaLeads}</a>
                        </li>
                        <li id="137">
                            <a href="/sfa/control/Events${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SfaEvents}</a>
                        </li>
                        <li id="138">
                            <a href="/sfa/control/FindSalesForecast${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SfaForecasts}</a>
                        </li>
                        <li id="139">
                            <a href="/sfa/control/FindSalesOpportunity${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SfaOpportunities}</a>
                        </li>
                        <li id="140">
                            <a href="/sfa/control/ManagePortalPages?parentPortalPageId=SFA_MAIN&sfaPreferences=1${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonPreferences}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[16].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" id="scr1">
                    <a href="#" title="Scrum"><i class="fa fa-lg fa-fw fa-align-justify"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[16].title]}</span></a>
                    <ul>
                        <li id="141">
                            <a href="/scrum/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumProductBacklog}</a>
                        </li>
                        <li id="142">
                            <a href="/scrum/control/Sprints${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumProjectSprint}</a>
                        </li>
                        <li id="143">
                            <a href="/scrum/control/MyWork${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumMyWork}</a>
                        </li>
                        <li id="144">
                            <a href="/scrum/control/FindTask${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumTasks}</a>
                        </li>
                        <li id="145">
                            <a href="/scrum/control/openTest${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumOpenTest}</a>
                        </li>
                        <li id="146">
                            <a href="/scrum/control/FindTaskRevision${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumRevision}</a>
                        </li>
                        <li id="147">
                            <a href="#">${uiLabelMap.ScrumAdmin}</a>
                            <ul>
                                <li id="066">
                                    <a href="/scrum/control/adminScrum${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AllBacklog}</a>
                                </li>
                                <li id="067">
                                    <a href="/scrum/control/FindTimeSheet${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleTimesheet}</a>
                                </li>
                                <li id="068">
                                    <a href="/scrum/control/TotalBilling${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleBilling}</a>
                                </li>
                                <li id="069">
                                    <a href="/scrum/control/defaultTaskScrum${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.DefaultTasks}</a>
                                </li>
                                <li id="070">
                                    <a href="/scrum/control/FindResource${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumMembers}</a>
                                </li>
                                <li id="071">
                                    <a href="/scrum/control/quickAddNewTask${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.QuickAddNewTask}</a>
                                </li>
                                <li id="072">
                                    <a href="/scrum/control/QuickAddBacklog${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.QuickAddBacklog}</a>
                                </li>
                                <li id="073">
                                    <a href="/scrum/control/ProductStatistics${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductStatistics}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="148">
                            <a href="#">${uiLabelMap.ScrumMyProfile}</a>
                            <ul>
                                <li id="074">
                                    <a href="/scrum/control/viewprofile?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Profile</a>
                                </li>
                                <li id="075">
                                    <a href="/scrum/control/Preferences?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Preferences</a>
                                </li>
                                <li id="076">
                                    <a href="/scrum/control/viewroles?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Role(s)</a>
                                </li>
                                <li id="077">
                                    <a href="/scrum/control/listResourcesProject?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Projects</a>
                                </li>
                                <li id="078">
                                    <a href="/scrum/control/listResourcesTask?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Tasks</a>
                                </li>
                                <li id="079">
                                    <a href="/scrum/control/EditPartyRates?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Rates</a>
                                </li>
                                <li id="080">
                                    <a href="/scrum/control/findPartyRevision?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Revisions</a>
                                </li>
                            </ul>
                        </li>

                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[18].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" id="wor1">
                    <a href="#" title="Work Effort"><i class="fa fa-lg fa-fw fa-gavel"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[18].title]}</span></a>
                    <ul>
                        <li id="149">
                            <a href="/workeffort/control/mytasks${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortTaskList}</a>
                        </li>
                        <li id="150">
                            <a href="/workeffort/control/calendar${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortCalendar}</a>
                        </li>
                        <li id="151">
                            <a href="/workeffort/control/MyTimesheets${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortTimesheetMyTime}</a>
                        </li>
                        <li id="152">
                            <a href="/workeffort/control/requestlist${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortRequestList}</a>
                        </li>
                        <li id="153">
                            <a href="/workeffort/control/FindWorkEffort${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortWorkEffort}</a>
                        </li>
                        <li id="154">
                            <a href="/workeffort/control/FindTimesheet${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortTimesheet}</a>
                        </li>
                        <li id="155">
                            <a href="/workeffort/control/UserJobs${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortJobList}</a>
                        </li>
                        <li id="156">
                            <a href="/workeffort/control/FindICalendars${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortICalendar}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displaySecondaryApps[1].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" id="bir1">
                    <a href="#" title="BIRT"><i class="fa fa-lg fa-fw  fa-suitcase"></i> <span
                            class="menu-item-parent">${uiLabelMap[displaySecondaryApps[1].title]}</span></a>
                    <ul>
                        <li id="157">
                            <a href="#">BIRT Main</a>
                            <ul>
                                <li id="081">
                                    <a href="/birt/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                                </li>
                                <li id="082">
                                    <a href="/birt/control/Report${StringUtil.wrapString(externalKeyParam)}">HTML</a>
                                </li>
                                <li id="083">
                                    <a target="_BLANK"
                                       href="/birt/control/ViewHandler${StringUtil.wrapString(externalKeyParam)}">PDF</a>
                                </li>
                                <li id="084">
                                    <a href="/birt/control/Mail${StringUtil.wrapString(externalKeyParam)}">Send any
                                        format through Mail</a>
                                </li>
                                <li id="085">
                                    <a href="/birt/control/chartReport${StringUtil.wrapString(externalKeyParam)}">HTML
                                        chart report</a>
                                </li>
                                <li id="086">
                                    <a target="_BLANK"
                                       href="/birt/control/chartViewHandler${StringUtil.wrapString(externalKeyParam)}">PDF
                                        chart report</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displaySecondaryApps[2].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" id="bi1">
                    <a href="#" title="BI"><i class="fa fa-lg fa-fw fa-lightbulb-o"></i> <span
                            class="menu-item-parent">${uiLabelMap[displaySecondaryApps[2].title]}</span></a>
                    <ul>
                        <li id="158">
                            <a href="/bi/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="159">
                            <a href="/bi/control/ReportBuilderSelectStarSchema${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.BusinessIntelligenceReportBuilder}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displaySecondaryApps[8].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);" id="web1">
                    <a href="#" title="Web Tools"><i class="fa fa-lg fa-fw fa-cog"></i> <span
                            class="menu-item-parent">${uiLabelMap[displaySecondaryApps[8].title]}</span></a>
                    <ul>
                        <li id="160">
                            <a href="/webtools/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="161">
                            <a href="/webtools/control/LogView${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WebtoolsLogging}</a>
                        </li>
                        <li id="162">
                            <a href="/webtools/control/FindUtilCache${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WebtoolsCacheMaintenance}</a>
                        </li>
                        <li id="163">
                            <a href="/webtools/control/ViewComponents${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WebtoolsArtifactInfo}</a>
                        </li>
                        <li id="164">
                            <a href="/webtools/control/entitymaint${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WebtoolsEntityEngine}</a>
                        </li>
                        <li id="165">
                            <a href="#">${uiLabelMap.WebtoolsServiceEngineTools}</a>
                            <ul>
                                <li id="087">
                                    <a href="/webtools/control/ServiceList${StringUtil.wrapString(externalKeyParam)}">Service
                                        Reference</a>
                                </li>
                                <li id="088">
                                    <a href="/webtools/control/FindJob${StringUtil.wrapString(externalKeyParam)}">Job
                                        List</a>
                                </li>
                                <li id="089">
                                    <a href="/webtools/control/threadList${StringUtil.wrapString(externalKeyParam)}">Thread
                                        List</a>
                                </li>
                                <li id="090">
                                    <a href="/webtools/control/FindJobManagerLock${StringUtil.wrapString(externalKeyParam)}">Job
                                        manager locks List</a>
                                </li>
                                <li id="091">
                                    <a href="/webtools/control/scheduleJob${StringUtil.wrapString(externalKeyParam)}">Schedule
                                        Job</a>
                                </li>
                                <li id="092">
                                    <a href="/webtools/control/runService${StringUtil.wrapString(externalKeyParam)}">Run
                                        Service</a>
                                </li>
                            </ul>
                        </li>
                        <li id="166">
                            <a href="#">${uiLabelMap.WebtoolsImportExport}</a>
                            <ul>
                                <li id="093">
                                    <a href="/webtools/control/viewdatafile${StringUtil.wrapString(externalKeyParam)}">Data
                                        File Tools</a>
                                </li>
                                <li id="094">
                                    <a href="/webtools/control/view/ModelInduceFromDb${StringUtil.wrapString(externalKeyParam)}">Induce
                                        Model XML from Database</a>
                                </li>
                                <li id="095">
                                    <a href="/webtools/control/EntityEoModelBundle${StringUtil.wrapString(externalKeyParam)}">Export
                                        Entity EOModelBundle</a>
                                </li>
                                <li id="096">
                                    <a href="/webtools/control/xmldsdump${StringUtil.wrapString(externalKeyParam)}">XML
                                        Data Export</a>
                                </li>
                                <li id="097">
                                    <a href="/webtools/control/EntityExportAll${StringUtil.wrapString(externalKeyParam)}">XML
                                        Data Export All</a>
                                </li>
                                <li id="098">
                                    <a href="/webtools/control/ProgramExport${StringUtil.wrapString(externalKeyParam)}">Programmable
                                        Export</a>
                                </li>
                                <li id="099">
                                    <a href="/webtools/control/EntityImport${StringUtil.wrapString(externalKeyParam)}">XML
                                        Data Import</a>
                                </li>
                                <li id="0100">
                                    <a href="/webtools/control/EntityImportDir${StringUtil.wrapString(externalKeyParam)}">XML
                                        Data Import Dir</a>
                                </li>
                                <li id="0101">
                                    <a href="/webtools/control/EntityImportReaders${StringUtil.wrapString(externalKeyParam)}">XML
                                        Data Import Readers</a>
                                </li>
                            </ul>
                        </li>
                        <li id="167">
                            <a href="#">${uiLabelMap.WebtoolsStatistics}</a>
                            <ul>
                                <li id="0102">
                                    <a href="/webtools/control/StatsSinceStart${StringUtil.wrapString(externalKeyParam)}">Statistics</a>
                                </li>
                                <li id="0103">
                                    <a href="/webtools/control/ViewMetrics${StringUtil.wrapString(externalKeyParam)}">Metrics</a>
                                </li>
                            </ul>
                        </li>
                        <li id="168">
                            <a href="#">${uiLabelMap.WebtoolsConfiguration}</a>
                            <ul>
                                <li id="0104">
                                    <a href="/webtools/control/findTemporalExpression${StringUtil.wrapString(externalKeyParam)}">Temporal
                                        Expression</a>
                                </li>
                                <li id="0105">
                                    <a href="/webtools/control/myCertificates${StringUtil.wrapString(externalKeyParam)}">My
                                        Certificates</a>
                                </li>
                            </ul>
                        </li>
                        <li id="169">
                            <a href="#">${uiLabelMap.WebtoolsGeoManagement}</a>
                            <ul>
                                <li id="0106">
                                    <a href="/webtools/control/FindGeo${StringUtil.wrapString(externalKeyParam)}">Find
                                        Geos</a>
                                </li>
                                <li id="0107">
                                    <a href="/webtools/control/EditGeo?${StringUtil.wrapString(externalKeyParam)}">Create/Edit
                                        Geo</a>
                                </li>
                                <li id="0108">
                                    <a href="/webtools/control/LinkGeos?${StringUtil.wrapString(externalKeyParam)}">Link
                                        Geos</a>
                                </li>
                                <li id="0109">
                                    <a href="/webtools/control/geoPoints${StringUtil.wrapString(externalKeyParam)}">GEO
                                        Points</a>
                                </li>
                            </ul>
                        </li>
                        <li id="170">
                            <a href="#">${uiLabelMap.CommonSecurity}</a>
                            <ul>
                                <li id="0110">
                                    <a href="/webtools/control/security${StringUtil.wrapString(externalKeyParam)}">Find
                                        User Login</a>
                                </li>
                                <li id="0111">
                                    <a href="/webtools/control/FindSecurityGroup${StringUtil.wrapString(externalKeyParam)}">Find
                                        Security Group</a>
                                </li>
                                <li id="0112">
                                    <a href="/webtools/control/EditCertIssuerProvisions${StringUtil.wrapString(externalKeyParam)}">Cert
                                        Issuers</a>
                                </li>
                            </ul>
                        </li>
                        <li id="171">
                            <a href="#">${uiLabelMap.WebtoolsLayoutDemo}</a>
                            <ul>
                                <li id="0113">
                                    <a href="/webtools/control/WebtoolsLayoutDemo?demoParam1=one&demoParam3=three&demoParam2=two${StringUtil.wrapString(externalKeyParam)}">Selected</a>
                                </li>
                                <li id="0114">
                                    <form method="post" action="/webtools/control/WebtoolsLayoutDemo"
                                          onsubmit="javascript:submitFormDisableSubmits(this)"
                                          name="LayoutDemo_Enabled_LF_2_">
                                        <input name="demoParam1" value="one" type="hidden">
                                        <input name="demoParam3" value="three" type="hidden">
                                        <input name="demoParam2" value="two" type="hidden">
                                    </form>
                                    <a href="javascript:document.LayoutDemo_Enabled_LF_2_.submit()">Enabled</a>
                                </li>
                                <li id="0115">
                                    <a href="/webtools/control/WebtoolsLayoutDemoFop${StringUtil.wrapString(externalKeyParam)}">View
                                        as Pdf</a>
                                </li>
                                <li id="0116">
                                    <a target="_blank"
                                       href="/webtools/control/WebtoolsLayoutDemoText${StringUtil.wrapString(externalKeyParam)}">View
                                        as text</a>
                                </li>
                                <li id="0117">
                                    <a target="_blank"
                                       href="/webtools/control/WebtoolsLayoutDemoXml${StringUtil.wrapString(externalKeyParam)}">View
                                        as xml</a>
                                </li>
                                <li id="0118">
                                    <a target="_blank"
                                       href="/webtools/control/WebtoolsLayoutDemoCsv${StringUtil.wrapString(externalKeyParam)}">View
                                        as csv</a>
                                </li>
                                <li id="0119">
                                    <a target="_blank"
                                       href="/webtools/control/WebtoolsLayoutDemoXls${StringUtil.wrapString(externalKeyParam)}">View
                                        as spreadsheet</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </li>

</#if>

<#if permission == true>
                <li id="more" style="display:none">
                    <a href="#"><i class="fa fa-lg fa-fw fa-plus"></i><span class="menu-item-parent">More..</span></a>
                    <ul id="moreInfo">
                    </ul>
                </li>
</#if>
        </ul>
    <#--<span class="minifyme" data-action="minifyMenu" id="minhide">
    <i class="fa fa-arrow-circle-left hit"></i>
    </span>-->
    </nav>
</aside>
<ul id="conn12">
    <#assign permission = true>
<#assign permissions = displayApps[9].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);">
                    <a href="#" title="Marketing"><i class="fa fa-lg fa-fw fa-group"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[9].title]}</span></a>
                    <ul>
                        <li id="86">
                            <a href="/marketing/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="87">
                            <a href="#">${uiLabelMap.DataSource}</a>
                            <ul>
                                <li id="057">
                                    <a href="/marketing/control/FindDataSource${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.DataSource}</a>
                                </li>
                                <li id="058">
                                    <a href="/marketing/control/FindDataSourceType${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.DataSourceType}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="88">
                            <a href="/marketing/control/FindMarketingCampaign${StringUtil.wrapString(externalKeyParam)}"><span
                                    class="menu-item-parent">${uiLabelMap.MarketingCampaign}</span></a>
                        </li>
                        <li id="89">
                            <a href="#">${uiLabelMap.MarketingTracking}</a>
                            <ul>
                                <li id="059">
                                    <a href="/marketing/control/FindTrackingCode${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.MarketingTrackingCode}</a>
                                </li>
                                <li id="060">
                                    <a href="/marketing/control/FindTrackingCodeType${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.MarketingTrackingCodeType}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="90">
                            <a href="/marketing/control/FindSegmentGroup${StringUtil.wrapString(externalKeyParam)}"><span
                                    class="menu-item-parent">${uiLabelMap.MarketingSegment}</span></a>
                        </li>
                        <li id="91">
                            <a href="/marketing/control/FindContactLists${StringUtil.wrapString(externalKeyParam)}"><span
                                    class="menu-item-parent">${uiLabelMap.MarketingContactList}</span></a>
                        </li>
                        <li id="92">
                            <a href="/marketing/control/MarketingReport${StringUtil.wrapString(externalKeyParam)}"><span
                                    class="menu-item-parent">${uiLabelMap.MarketingReports}</span></a>
                        </li>
                    </ul>
                </li>
</#if>

<#assign permission = true>
<#assign permissions = displayApps[10].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>     
	     <li onclick="openthisul(this);" id="port1">
             <a href="#" title="My Portal" id="myPortal"><i class="fa fa-lg fa-fw fa-television"></i> <span
                     class="menu-item-parent">${uiLabelMap[displayApps[10].title]}</span></a>
             <ul>
                 <li id="93">
                     <a href="/myportal/control/Home${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.Home}</a>
                 </li>
             </ul>
         </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[11].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);">
                    <a href="#" title="Order"><i class="fa fa-lg fa-fw fa-file-text"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[11].title]}</span></a>
                    <ul>
                        <li id="94">
                            <a href="/ordermgr/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="95">
                            <a href="/ordermgr/control/FindRequest${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderRequests}</a>
                        </li>
                        <li id="96">
                            <a href="/ordermgr/control/FindQuote${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderOrderQuotes}</a>
                        </li>
                        <li id="97">
                            <a href="/ordermgr/control/orderlist${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderOrderList}</a>
                        </li>
                        <li id="98">
                            <a href="/ordermgr/control/findorders${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderFindOrder}</a>
                        </li>
                        <li id="99">
                            <a href="/ordermgr/control/orderentry${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderOrderEntry}</a>
                        </li>
                        <li id="100">
                            <a href="/ordermgr/control/findreturn${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderOrderReturns}</a>
                        </li>
                        <li id="101">
                            <a href="/ordermgr/control/FindRequirements${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.OrderRequirements}</a>
                        </li>
                        <li id="102">
                            <a href="/ordermgr/control/OrderPurchaseReportOptions${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonReports}</a>
                        </li>
                        <li id="103">
                            <a href="/ordermgr/control/orderstats${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonStats}</a>
                        </li>
                    </ul>
                </li>
</#if>

<#assign permission = true>
<#assign permissions = displayApps[12].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);">
                    <a href="#" title="Party"><i class="fa fa-lg fa-fw fa-credit-card"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[12].title]}</span></a>
                    <ul>
                        <li id="104">
                            <a href="/partymgr/control/findparty${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyParties}</a>
                        </li>
                        <li id="105">
                            <a href="/partymgr/control/MyCommunicationEvents${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyMyCommunications}</a>
                        </li>
                        <li id="106">
                            <a href="#">${uiLabelMap.PartyCommunications}</a>
                            <ul>
                                <li id="061">
                                    <a href="/partymgr/control/FindCommunicationEvents${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonFind}</a>
                                </li>
                                <li id="062">
                                    <a href="/partymgr/control/listUnknownPartyComms${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyEmailFromUnknownParties}</a>
                                </li>
                                <li id="063">
                                    <a href="/partymgr/control/FindCommunicationByOrder${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyFindCommunicationsByOrder}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="107">
                            <a href="/partymgr/control/findVisits${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyVisits}</a>
                        </li>
                        <li id="108">
                            <a href="/partymgr/control/listLoggedInUsers${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyLoggedInUsers}</a>
                        </li>
                        <li id="109">
                            <a href="/partymgr/control/showclassgroups${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyClassifications}</a>
                        </li>
                        <li id="110">
                            <a href="/partymgr/control/FindUserLogin${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonSecurity}</a>
                        </li>
                        <li id="111">
                            <a href="/partymgr/control/addressMatchMap${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleAddressMatchMap}</a>
                        </li>
                        <li id="112">
                            <a href="/partymgr/control/partyInvitation${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PartyInvitation}</a>
                        </li>
                        <li id="113">
                            <a href="/partymgr/control/ImportExport${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonImportExport}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[13].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);">
                    <a href="#" title="Payroll"><i class="fa fa-lg fa-fw fa-money"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[13].title]}</span></a>
                    <ul>
                        <li id="114">
                            <a href="/Payroll/control/References${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.References}</a>
                        </li>
                        <li id="115">
                            <a href="/Payroll/control/Allowences${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.Allowences}</a>
                        </li>
                        <li id="116">
                            <a href="/Payroll/control/AllowencesEmployee${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AllowenceEmployee}</a>
                        </li>
                        <li id="117">
                            <a href="/Payroll/control/SalaryScale${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SalaryScale}</a>
                        </li>

                        <li id="118">
                            <a href="/Payroll/control/SocialSecurityCalculation${StringUtil.wrapString(externalKeyParam)}">Social
                                Security Calculation</a>
                        </li>
                        <li id="119">
                            <a href="/Payroll/control/IncomeTax${StringUtil.wrapString(externalKeyParam)}">Income Tax
                                Calculation</a>
                        </li>
                        <li id="120">
                            <a href="/Payroll/control/SalaryCalculation${StringUtil.wrapString(externalKeyParam)}">Salary
                                Calculation</a>
                        </li>
                        <li id="121">
                            <a href="/Payroll/control/allowencesCompanyExpense${StringUtil.wrapString(externalKeyParam)}">Allowences
                                Company Expenses</a>
                        </li>
                        <li id="122">
                            <a href="/Payroll/control/loans${StringUtil.wrapString(externalKeyParam)}">Loan Managment</a>
                        </li>

                        <li id="123">
                            <a href="/Payroll/control/GlobalPayrollSetting${StringUtil.wrapString(externalKeyParam)}">Global
                                Payroll Setting</a>
                        </li>
                        <li id="124">
                            <a href="#">Reports</a>
                            <ul>
                                <li id="064">
                                    <a href="/Payroll/control/SalarySlipAll">Salary Slip</a>
                                </li>
                                <li id="065">
                                    <a href="/Payroll/control/SalarySlip${StringUtil.wrapString(externalKeyParam)}">Salary
                                        Slip Details</a>
                                </li>
                                <li id="0121">
                                    <a href="/Payroll/control/cashRequests${StringUtil.wrapString(externalKeyParam)}">Cash
                                        Requests</a>
                                </li>
                                <li id="0120">
                                    <a href="/Payroll/control/costCentersPayrollExp${StringUtil.wrapString(externalKeyParam)}">Cost
                                        Centers</a>
                                </li>
                                 <li id="0125">
                                    <a href="/Payroll/control/costCentersPayrollExpDetails${StringUtil.wrapString(externalKeyParam)}">Cost
                                        Centers Details</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </li>
</#if>

<#assign permission = true>
<#assign permissions = displayApps[14].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);">
                    <a href="#" title="Project"><i class="fa fa-lg fa-fw fa-folder-open"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[14].title]}</span></a>
                    <ul>
                        <li id="124">
                            <a href="/projectmgr/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="125">
                            <a href="/projectmgr/control/MyTasks${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortMyTasks}</a>
                        </li>
                        <li id="126">
                            <a href="/projectmgr/control/MyTimesheet${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortTimesheetMyTime}</a>
                        </li>
                        <li id="127">
                            <a href="/projectmgr/control/FindProject${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortProjects}</a>
                        </li>
                        <li id="128">
                            <a href="/projectmgr/control/FindTask${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProjectMgrTasks}</a>
                        </li>
                        <li id="129">
                            <a href="/projectmgr/control/FindResource${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProjectMgrResources}</a>
                        </li>
                        <li id="130">
                            <a href="/projectmgr/control/FindTimeSheet${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortTimesheet}</a>
                        </li>
                        <li id="131">
                            <a href="/projectmgr/control/findSkillTypes${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProjectMgrSkillType}</a>
                        </li>
                        <li id="132">
                            <a href="/projectmgr/control/requestlist${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortRequestList}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[15].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);">
                    <a href="#" title="Sfa"><i class="fa fa-lg fa-fw fa-gg"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[15].title]}</span></a>
                    <ul>
                        <li id="133">
                            <a href="/sfa/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="134">
                            <a href="/sfa/control/FindAccounts${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SfaAcccounts}</a>
                        </li>
                        <li id="135">
                            <a href="/sfa/control/FindContacts${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SfaContacts}</a>
                        </li>
                        <li id="136">
                            <a href="/sfa/control/FindLeads${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SfaLeads}</a>
                        </li>
                        <li id="137">
                            <a href="/sfa/control/Events${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SfaEvents}</a>
                        </li>
                        <li id="138">
                            <a href="/sfa/control/FindSalesForecast${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SfaForecasts}</a>
                        </li>
                        <li id="139">
                            <a href="/sfa/control/FindSalesOpportunity${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.SfaOpportunities}</a>
                        </li>
                        <li id="140">
                            <a href="/sfa/control/ManagePortalPages?parentPortalPageId=SFA_MAIN&sfaPreferences=1${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonPreferences}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[16].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);">
                    <a href="#" title="Scrum"><i class="fa fa-lg fa-fw fa-align-justify"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[16].title]}</span></a>
                    <ul>
                        <li id="141">
                            <a href="/scrum/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumProductBacklog}</a>
                        </li>
                        <li id="142">
                            <a href="/scrum/control/Sprints${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumProjectSprint}</a>
                        </li>
                        <li id="143">
                            <a href="/scrum/control/MyWork${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumMyWork}</a>
                        </li>
                        <li id="144">
                            <a href="/scrum/control/FindTask${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumTasks}</a>
                        </li>
                        <li id="145">
                            <a href="/scrum/control/openTest${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumOpenTest}</a>
                        </li>
                        <li id="146">
                            <a href="/scrum/control/FindTaskRevision${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumRevision}</a>
                        </li>
                        <li id="147">
                            <a href="#">${uiLabelMap.ScrumAdmin}</a>
                            <ul>
                                <li id="066">
                                    <a href="/scrum/control/adminScrum${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.AllBacklog}</a>
                                </li>
                                <li id="067">
                                    <a href="/scrum/control/FindTimeSheet${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleTimesheet}</a>
                                </li>
                                <li id="068">
                                    <a href="/scrum/control/TotalBilling${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.PageTitleBilling}</a>
                                </li>
                                <li id="069">
                                    <a href="/scrum/control/defaultTaskScrum${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.DefaultTasks}</a>
                                </li>
                                <li id="070">
                                    <a href="/scrum/control/FindResource${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ScrumMembers}</a>
                                </li>
                                <li id="071">
                                    <a href="/scrum/control/quickAddNewTask${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.QuickAddNewTask}</a>
                                </li>
                                <li id="072">
                                    <a href="/scrum/control/QuickAddBacklog${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.QuickAddBacklog}</a>
                                </li>
                                <li id="073">
                                    <a href="/scrum/control/ProductStatistics${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.ProductStatistics}</a>
                                </li>
                            </ul>
                        </li>
                        <li id="148">
                            <a href="#">${uiLabelMap.ScrumMyProfile}</a>
                            <ul>
                                <li id="074">
                                    <a href="/scrum/control/viewprofile?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Profile</a>
                                </li>
                                <li id="075">
                                    <a href="/scrum/control/Preferences?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Preferences</a>
                                </li>
                                <li id="076">
                                    <a href="/scrum/control/viewroles?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Role(s)</a>
                                </li>
                                <li id="077">
                                    <a href="/scrum/control/listResourcesProject?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Projects</a>
                                </li>
                                <li id="078">
                                    <a href="/scrum/control/listResourcesTask?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Tasks</a>
                                </li>
                                <li id="079">
                                    <a href="/scrum/control/EditPartyRates?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Rates</a>
                                </li>
                                <li id="080">
                                    <a href="/scrum/control/findPartyRevision?partyId=admin${StringUtil.wrapString(externalKeyParam)}">Revisions</a>
                                </li>
                            </ul>
                        </li>

                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displayApps[18].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);">
                    <a href="#" title="Work Effort"><i class="fa fa-lg fa-fw fa-gavel"></i> <span
                            class="menu-item-parent">${uiLabelMap[displayApps[18].title]}</span></a>
                    <ul>
                        <li id="149">
                            <a href="/workeffort/control/mytasks${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortTaskList}</a>
                        </li>
                        <li id="150">
                            <a href="/workeffort/control/calendar${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortCalendar}</a>
                        </li>
                        <li id="151">
                            <a href="/workeffort/control/MyTimesheets${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortTimesheetMyTime}</a>
                        </li>
                        <li id="152">
                            <a href="/workeffort/control/requestlist${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortRequestList}</a>
                        </li>
                        <li id="153">
                            <a href="/workeffort/control/FindWorkEffort${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortWorkEffort}</a>
                        </li>
                        <li id="154">
                            <a href="/workeffort/control/FindTimesheet${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortTimesheet}</a>
                        </li>
                        <li id="155">
                            <a href="/workeffort/control/UserJobs${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortJobList}</a>
                        </li>
                        <li id="156">
                            <a href="/workeffort/control/FindICalendars${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WorkEffortICalendar}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displaySecondaryApps[1].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);">
                    <a href="#" title="BIRT"><i class="fa fa-lg fa-fw  fa-suitcase"></i> <span
                            class="menu-item-parent">${uiLabelMap[displaySecondaryApps[1].title]}</span></a>
                    <ul>
                        <li id="157">
                            <a href="#">BIRT Main</a>
                            <ul>
                                <li id="081">
                                    <a href="/birt/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                                </li>
                                <li id="082">
                                    <a href="/birt/control/Report${StringUtil.wrapString(externalKeyParam)}">HTML</a>
                                </li>
                                <li id="083">
                                    <a target="_BLANK"
                                       href="/birt/control/ViewHandler${StringUtil.wrapString(externalKeyParam)}">PDF</a>
                                </li>
                                <li id="084">
                                    <a href="/birt/control/Mail${StringUtil.wrapString(externalKeyParam)}">Send any
                                        format through Mail</a>
                                </li>
                                <li id="085">
                                    <a href="/birt/control/chartReport${StringUtil.wrapString(externalKeyParam)}">HTML
                                        chart report</a>
                                </li>
                                <li id="086">
                                    <a target="_BLANK"
                                       href="/birt/control/chartViewHandler${StringUtil.wrapString(externalKeyParam)}">PDF
                                        chart report</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displaySecondaryApps[2].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);">
                    <a href="#" title="BI"><i class="fa fa-lg fa-fw fa-lightbulb-o"></i> <span
                            class="menu-item-parent">${uiLabelMap[displaySecondaryApps[2].title]}</span></a>
                    <ul>
                        <li id="158">
                            <a href="/bi/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="159">
                            <a href="/bi/control/ReportBuilderSelectStarSchema${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.BusinessIntelligenceReportBuilder}</a>
                        </li>
                    </ul>
                </li>
</#if>
<#assign permission = true>
<#assign permissions = displaySecondaryApps[8].getBasePermission()>
<#list permissions as perm>
    <#if (perm != "NONE" && !security.hasEntityPermission(perm, "_VIEW", session))>
        <#assign permission = false>
    </#if>
</#list>
<#if permission == true>
                <li onclick="openthisul(this);">
                    <a href="#" title="Web Tools"><i class="fa fa-lg fa-fw fa-cog"></i> <span
                            class="menu-item-parent">${uiLabelMap[displaySecondaryApps[8].title]}</span></a>
                    <ul>
                        <li id="160">
                            <a href="/webtools/control/main${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.CommonMain}</a>
                        </li>
                        <li id="161">
                            <a href="/webtools/control/LogView${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WebtoolsLogging}</a>
                        </li>
                        <li id="162">
                            <a href="/webtools/control/FindUtilCache${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WebtoolsCacheMaintenance}</a>
                        </li>
                        <li id="163">
                            <a href="/webtools/control/ViewComponents${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WebtoolsArtifactInfo}</a>
                        </li>
                        <li id="164">
                            <a href="/webtools/control/entitymaint${StringUtil.wrapString(externalKeyParam)}">${uiLabelMap.WebtoolsEntityEngine}</a>
                        </li>
                        <li id="165">
                            <a href="#">${uiLabelMap.WebtoolsServiceEngineTools}</a>
                            <ul>
                                <li id="087">
                                    <a href="/webtools/control/ServiceList${StringUtil.wrapString(externalKeyParam)}">Service
                                        Reference</a>
                                </li>
                                <li id="088">
                                    <a href="/webtools/control/FindJob${StringUtil.wrapString(externalKeyParam)}">Job
                                        List</a>
                                </li>
                                <li id="089">
                                    <a href="/webtools/control/threadList${StringUtil.wrapString(externalKeyParam)}">Thread
                                        List</a>
                                </li>
                                <li id="090">
                                    <a href="/webtools/control/FindJobManagerLock${StringUtil.wrapString(externalKeyParam)}">Job
                                        manager locks List</a>
                                </li>
                                <li id="091">
                                    <a href="/webtools/control/scheduleJob${StringUtil.wrapString(externalKeyParam)}">Schedule
                                        Job</a>
                                </li>
                                <li id="092">
                                    <a href="/webtools/control/runService${StringUtil.wrapString(externalKeyParam)}">Run
                                        Service</a>
                                </li>
                            </ul>
                        </li>
                        <li id="166">
                            <a href="#">${uiLabelMap.WebtoolsImportExport}</a>
                            <ul>
                                <li id="093">
                                    <a href="/webtools/control/viewdatafile${StringUtil.wrapString(externalKeyParam)}">Data
                                        File Tools</a>
                                </li>
                                <li id="094">
                                    <a href="/webtools/control/view/ModelInduceFromDb${StringUtil.wrapString(externalKeyParam)}">Induce
                                        Model XML from Database</a>
                                </li>
                                <li id="095">
                                    <a href="/webtools/control/EntityEoModelBundle${StringUtil.wrapString(externalKeyParam)}">Export
                                        Entity EOModelBundle</a>
                                </li>
                                <li id="096">
                                    <a href="/webtools/control/xmldsdump${StringUtil.wrapString(externalKeyParam)}">XML
                                        Data Export</a>
                                </li>
                                <li id="097">
                                    <a href="/webtools/control/EntityExportAll${StringUtil.wrapString(externalKeyParam)}">XML
                                        Data Export All</a>
                                </li>
                                <li id="098">
                                    <a href="/webtools/control/ProgramExport${StringUtil.wrapString(externalKeyParam)}">Programmable
                                        Export</a>
                                </li>
                                <li id="099">
                                    <a href="/webtools/control/EntityImport${StringUtil.wrapString(externalKeyParam)}">XML
                                        Data Import</a>
                                </li>
                                <li id="0100">
                                    <a href="/webtools/control/EntityImportDir${StringUtil.wrapString(externalKeyParam)}">XML
                                        Data Import Dir</a>
                                </li>
                                <li id="0101">
                                    <a href="/webtools/control/EntityImportReaders${StringUtil.wrapString(externalKeyParam)}">XML
                                        Data Import Readers</a>
                                </li>
                            </ul>
                        </li>
                        <li id="167">
                            <a href="#">${uiLabelMap.WebtoolsStatistics}</a>
                            <ul>
                                <li id="0102">
                                    <a href="/webtools/control/StatsSinceStart${StringUtil.wrapString(externalKeyParam)}">Statistics</a>
                                </li>
                                <li id="0103">
                                    <a href="/webtools/control/ViewMetrics${StringUtil.wrapString(externalKeyParam)}">Metrics</a>
                                </li>
                            </ul>
                        </li>
                        <li id="168">
                            <a href="#">${uiLabelMap.WebtoolsConfiguration}</a>
                            <ul>
                                <li id="0104">
                                    <a href="/webtools/control/findTemporalExpression${StringUtil.wrapString(externalKeyParam)}">Temporal
                                        Expression</a>
                                </li>
                                <li id="0105">
                                    <a href="/webtools/control/myCertificates${StringUtil.wrapString(externalKeyParam)}">My
                                        Certificates</a>
                                </li>
                            </ul>
                        </li>
                        <li id="169">
                            <a href="#">${uiLabelMap.WebtoolsGeoManagement}</a>
                            <ul>
                                <li id="0106">
                                    <a href="/webtools/control/FindGeo${StringUtil.wrapString(externalKeyParam)}">Find
                                        Geos</a>
                                </li>
                                <li id="0107">
                                    <a href="/webtools/control/EditGeo?${StringUtil.wrapString(externalKeyParam)}">Create/Edit
                                        Geo</a>
                                </li>
                                <li id="0108">
                                    <a href="/webtools/control/LinkGeos?${StringUtil.wrapString(externalKeyParam)}">Link
                                        Geos</a>
                                </li>
                                <li id="0109">
                                    <a href="/webtools/control/geoPoints${StringUtil.wrapString(externalKeyParam)}">GEO
                                        Points</a>
                                </li>
                            </ul>
                        </li>
                        <li id="170">
                            <a href="#">${uiLabelMap.CommonSecurity}</a>
                            <ul>
                                <li id="0110">
                                    <a href="/webtools/control/security${StringUtil.wrapString(externalKeyParam)}">Find
                                        User Login</a>
                                </li>
                                <li id="0111">
                                    <a href="/webtools/control/FindSecurityGroup${StringUtil.wrapString(externalKeyParam)}">Find
                                        Security Group</a>
                                </li>
                                <li id="0112">
                                    <a href="/webtools/control/EditCertIssuerProvisions${StringUtil.wrapString(externalKeyParam)}">Cert
                                        Issuers</a>
                                </li>
                            </ul>
                        </li>
                        <li id="171">
                            <a href="#">${uiLabelMap.WebtoolsLayoutDemo}</a>
                            <ul>
                                <li id="0113">
                                    <a href="/webtools/control/WebtoolsLayoutDemo?demoParam1=one&demoParam3=three&demoParam2=two${StringUtil.wrapString(externalKeyParam)}">Selected</a>
                                </li>
                                <li id="0114">
                                    <form method="post" action="/webtools/control/WebtoolsLayoutDemo"
                                          onsubmit="javascript:submitFormDisableSubmits(this)"
                                          name="LayoutDemo_Enabled_LF_2_">
                                        <input name="demoParam1" value="one" type="hidden">
                                        <input name="demoParam3" value="three" type="hidden">
                                        <input name="demoParam2" value="two" type="hidden">
                                    </form>
                                    <a href="javascript:document.LayoutDemo_Enabled_LF_2_.submit()">Enabled</a>
                                </li>
                                <li id="0115">
                                    <a href="/webtools/control/WebtoolsLayoutDemoFop${StringUtil.wrapString(externalKeyParam)}">View
                                        as Pdf</a>
                                </li>
                                <li id="0116">
                                    <a target="_blank"
                                       href="/webtools/control/WebtoolsLayoutDemoText${StringUtil.wrapString(externalKeyParam)}">View
                                        as text</a>
                                </li>
                                <li id="0117">
                                    <a target="_blank"
                                       href="/webtools/control/WebtoolsLayoutDemoXml${StringUtil.wrapString(externalKeyParam)}">View
                                        as xml</a>
                                </li>
                                <li id="0118">
                                    <a target="_blank"
                                       href="/webtools/control/WebtoolsLayoutDemoCsv${StringUtil.wrapString(externalKeyParam)}">View
                                        as csv</a>
                                </li>
                                <li id="0119">
                                    <a target="_blank"
                                       href="/webtools/control/WebtoolsLayoutDemoXls${StringUtil.wrapString(externalKeyParam)}">View
                                        as spreadsheet</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </li>

</#if>
</ul>
    <div id="main" role="main">
        <div id="ribbon">
            <span class="ribbon-button-alignment">
                <span id="refresh" class="btn btn-ribbon" data-action="resetWidgets" data-title="refresh" rel="tooltip"
                      data-placement="bottom" data-original-title="<i class='text-warning fa fa-warning'></i>
                      Warning! This will reset all your widget settings." data-html="true">
                    <i class="fa fa-refresh"></i>
                    </span>
                <span id="newribbontext" class="breadcrumb">
                    </span>
                </span>
        </div>
      <div id="content">
        <section id="widget-grid">
<#else>
        <html style="width:100%; height:800px; background:url(/images/BusinessInnovation/images/building.jpg); background-repeat:no-repeat; background-attachment: fixed; background-position: top;background-size:100% ">
        <body style="width:100%; height:800px; background:url(/images/BusinessInnovation/images/building.jpg); background-repeat:no-repeat; background-attachment: fixed; background-position: top;background-size:100% ">
 </#if>

<script>
    // alert(userLogin.TENANETID);
    console.log("${(requestParameters.userTenantId)!}");
</script>
