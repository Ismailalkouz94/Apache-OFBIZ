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
<div class="screenlet-body">
  <form action="<@ofbizUrl>SearchLabels</@ofbizUrl>" method="post">
    <table class="basic-table">
      <tr><td colspan="6"><div class="alert alert-info fade in">${uiLabelMap.WebtoolsLabelManagerTemporarySearchTitle}</div></td></tr>    
      <tr>
        <td><label>
          ${uiLabelMap.WebtoolsLabelManagerKey}
          </label>
        </td>
        <td>
          <input type="text" name="labelKey" size="30" maxlength="70" value="${parameters.labelKey!}" />
        </td>
         <td><label>
          ${uiLabelMap.WebtoolsLabelManagerComponentName}
            </label>
        </td>
        <td>
          <select name="labelComponentName">
            <option value="">${uiLabelMap.WebtoolsLabelManagerAllComponents}</option>
            <#list componentNamesFound as componentNameFound>
              <option <#if parameters.labelComponentName?? && parameters.labelComponentName == componentNameFound>selected="selected"</#if> value="${componentNameFound}">${componentNameFound}</option>
            </#list>
          </select>
        </td>
      </tr>
      <tr>
         <td><label>
          ${uiLabelMap.WebtoolsLabelManagerFileName}
            </label>
        </td>
        <td>
          <select name="labelFileName">
            <option value="">${uiLabelMap.WebtoolsLabelManagerAllFiles}</option>
            <#list filesFound as fileInfo>
              <#assign fileName = fileInfo.getFileName()/>
              <option <#if parameters.labelFileName?? && parameters.labelFileName == fileName>selected="selected"</#if> value="${fileName}">${fileName}</option>
            </#list>
          </select>
        </td>
         <td><label>
          ${uiLabelMap.WebtoolsLabelManagerLocale}
            </label>
        </td>
        <td>
          <select name="labelLocaleName">
            <option value="">${uiLabelMap.WebtoolsLabelManagerAllLocales}</option>
            <#list localesFound as localeFound>
              <#assign locale = Static["org.apache.ofbiz.base.util.UtilMisc"].parseLocale(localeFound)!/>
              <#assign langAttr = localeFound.toString()?replace("_", "-")>
              <#assign langDir = "ltr">
              <#if 1 < langAttr?length>
                <#if "ar.iw"?contains(langAttr?substring(0, 2))>
                  <#assign langDir = "rtl">
                </#if>
              </#if>
              <option <#if parameters.labelLocaleName?? && parameters.labelLocaleName == localeFound>selected="selected"</#if> value="${localeFound}" lang="${langAttr}" dir="${langDir}"><#if locale?? && locale?has_content>${locale.getDisplayName(locale)}<#else>${localeFound}</#if></option>
            </#list>
          </select>
        </td>
      </tr>
      <tr>
        <td>
          <label for="onlyNotUsedLabels">${uiLabelMap.WebtoolsLabelManagerOnlyNotUsedLabels}</label>
        </td>
        <td>
          <label class="checkbox-inline" style="padding-left: 0;">
          <input class="checkbox style-0" type="checkbox" id="onlyNotUsedLabels" name="onlyNotUsedLabels" value="Y" <#if parameters.onlyNotUsedLabels?? && parameters.onlyNotUsedLabels == "Y">checked="checked"</#if> /><span></span>
          </label>
         </td>
         <td>
          <label for="onlyMissingTranslations">${uiLabelMap.WebtoolsLabelManagerOnlyMissingTranslations}</label>
        </td>
        <td>
         <label class="checkbox-inline" style="padding-left: 0;">
          <input type="checkbox" class="checkbox style-0" id="onlyMissingTranslations" name="onlyMissingTranslations" value="Y" <#if parameters.onlyMissingTranslations?? && parameters.onlyMissingTranslations == "Y">checked="checked"</#if> /><span></span>
         </label>
        </td>
      </tr>
      <tr>
        <td colspan="4">
          <#if (duplicatedLocalesLabels > 0)>
            <br />
            <b>${uiLabelMap.WebtoolsLabelManagerWarningMessage} (${duplicatedLocalesLabels})</b>
            <br />
            <#list duplicatedLocalesLabelsList as duplicatedLocalesLabel>
                <br/>${duplicatedLocalesLabel.labelKey}
            </#list>
            <br /><br />${uiLabelMap.WebtoolsLabelManagerClearCacheAfterFixingDuplicateLabels}
          <#else>
            <input type="submit" class="btn btn-primary1" name="searchLabels" value="${uiLabelMap.CommonFind}"/>
          </#if>
        </td>
      </tr>
    </table>
  </form>
</div>
