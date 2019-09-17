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

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta property="og:type"          content="website" />
    <meta property="og:title"         content="Your Website Title" />
    <meta property="og:description"   content="Your description" />


    <script data-pace-options='{ "restartOnRequestAfter": true }' src="/images/BusinessInnovation/js/plugin/pace/pace.min.js"></script>

    <script src="/images/BusinessInnovation/js/libs/jquery-2.1.1.min.js"></script>
    <script src="/images/BusinessInnovation/js/libs/jquery-ui-1.10.3.min.js"></script>
    <script src="/images/BusinessInnovation/js/plugin/bootstrap-slider/bootstrap-slider.min.js"></script>

<link rel="shortcut icon" href="/images/BusinessInnovation/images/shortcut-logo.ico" type="image/x-icon">

    <title><#--${layoutSettings.companyName}:-->TOPAZ  <#if (titleProperty)?has_content>- ${uiLabelMap[titleProperty]}<#else>${title!}</#if></title>
<#--<#if layoutSettings.shortcutIcon?has_content>
    <#assign shortcutIcon = layoutSettings.shortcutIcon/>
<#elseif layoutSettings.VT_SHORTCUT_ICON?has_content>
    <#assign shortcutIcon = layoutSettings.VT_SHORTCUT_ICON.get(0)/>
</#if>
<#if shortcutIcon?has_content>
    <link rel="shortcut icon" href="<@ofbizContentUrl>${StringUtil.wrapString(shortcutIcon)}</@ofbizContentUrl>" />
</#if>-->

    <link rel="stylesheet" type="text/css" media="screen" href="/images/BusinessInnovation/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="/images/BusinessInnovation/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="/images/BusinessInnovation/css/smartadmin-production-plugins.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="/images/BusinessInnovation/css/smartadmin-production.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="/images/BusinessInnovation/css/smartadmin-skins.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="/images/BusinessInnovation/css/smartadmin-rtl.min.css"> 
    <link rel="stylesheet" type="text/css" media="screen" href="/images/BusinessInnovation/css/demo.min.css">
    <link href="/images/BusinessInnovation/the-portal-theme/custom.css" rel="stylesheet" />


<#if layoutSettings.VT_HDR_JAVASCRIPT?has_content>
    <#list layoutSettings.VT_HDR_JAVASCRIPT as javaScript>
    <script src="<@ofbizContentUrl>${StringUtil.wrapString(javaScript)}</@ofbizContentUrl>" type="text/javascript"></script>
    </#list>
</#if>
<#if layoutSettings.javaScripts?has_content>
<#--layoutSettings.javaScripts is a list of java scripts. -->
<#-- use a Set to make sure each javascript is declared only once, but iterate the list to maintain the correct order -->
    <#assign javaScriptsSet = Static["org.apache.ofbiz.base.util.UtilMisc"].toSet(layoutSettings.javaScripts)/>
    <#list layoutSettings.javaScripts as javaScript>
        <#if javaScriptsSet.contains(javaScript)>
            <#assign nothing = javaScriptsSet.remove(javaScript)/>
    <script src="<@ofbizContentUrl>${StringUtil.wrapString(javaScript)}</@ofbizContentUrl>" type="text/javascript"></script>
        </#if>
    </#list>
</#if>

<#if layoutSettings.VT_EXTRA_HEAD?has_content>
    <#list layoutSettings.VT_EXTRA_HEAD as extraHead>
    ${extraHead}
    </#list>
</#if>
<#if lastParameters??><#assign parametersURL = "&amp;" + lastParameters></#if>
<#if layoutSettings.WEB_ANALYTICS?has_content>
    <script language="JavaScript" type="text/javascript">
        <#list layoutSettings.WEB_ANALYTICS as webAnalyticsConfig>
    ${StringUtil.wrapString(webAnalyticsConfig.webAnalyticsCode!)}
    </#list>
        </script>
</#if>

    <script src="/images/BusinessInnovation/js/jquery.contextMenu.js" type="text/javascript"></script>


    <link rel="stylesheet" href="/images/BusinessInnovation/css/vend/jquery.orgchart.css">
    <link rel="stylesheet" href="/images/BusinessInnovation/css/vend/style1.css">
    <link rel="stylesheet" href="/images/BusinessInnovation/css/vend/style.css">

    <link rel="stylesheet" type="text/css" href="/images/BusinessInnovation/css/vend/style9.css" />
    <link rel="stylesheet" type="text/css" href="/images/BusinessInnovation/css/vend2/btn-bar.css" />
    
    <link rel="stylesheet" href="/images/BusinessInnovation/for-popup/example.css">
    <script src="/images/notifications/notifications.js"></script>
 </head>
