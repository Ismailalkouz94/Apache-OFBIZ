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

<div class="page-title"><span>${uiLabelMap.WebtoolsImportToDataSource}</span></div>
<div class="alert alert-info fade in">${uiLabelMap.WebtoolsXMLImportInfo}</div>
<hr />

  <form class="smart-form" method="post" action="<@ofbizUrl>entityImport</@ofbizUrl>">
    <label>${uiLabelMap.WebtoolsAbsoluteFileNameOrUrl} :
    <input class="form-control" style="width:50%" type="text" size="60" name="filename" value="${filename!}"/></label>
<br /><br />
    <label>${uiLabelMap.WebtoolsAbsoluteFTLFilename} :
    <input class="form-control" style="width:50%" type="text" size="40" name="fmfilename" value="${fmfilename!}"/></label>
<br /><br />
    <div class="form-group">
    <label><input class="checkbox style-0" type="checkbox" name="isUrl" <#if isUrl??>checked="checked"</#if>/><span>${uiLabelMap.WebtoolsIsURL}</span></label><br />
    <label><input class="checkbox style-0" type="checkbox" name="mostlyInserts" <#if mostlyInserts??>checked="checked"</#if>/><span>${uiLabelMap.WebtoolsMostlyInserts}</span></label><br />
    <label><input class="checkbox style-0" type="checkbox" name="maintainTimeStamps" <#if keepStamps??>checked="checked"</#if>/><span>${uiLabelMap.WebtoolsMaintainTimestamps}</span></label><br />
    <label><input class="checkbox style-0" type="checkbox" name="createDummyFks" <#if createDummyFks??>checked="checked"</#if>/><span>${uiLabelMap.WebtoolsCreateDummyFks}</span></label><br />
    <label><input class="checkbox style-0" type="checkbox" name="checkDataOnly" <#if checkDataOnly??>checked="checked"</#if>/><span>${uiLabelMap.WebtoolsCheckDataOnly}</span></label><br />
     </div><br/>
    ${uiLabelMap.WebtoolsTimeoutSeconds}:<input class="form-control" style="width:6%" type="text" size="6" value="${txTimeoutStr?default("7200")}" name="txTimeout"/><br />
    <div><input class="btn btn-primary" style="padding:6px 12px" type="submit" value="${uiLabelMap.WebtoolsImportFile}"/></div>
  </form><br />
  <form method="post" action="<@ofbizUrl>entityImport</@ofbizUrl>">
    <label>${uiLabelMap.WebtoolsCompleteXMLDocument}: </label><br />
    <textarea style="height:400px !important;width:800px;" class="form-control" rows="20" cols="85" name="fulltext">${fulltext?default("<entity-engine-xml>\n</entity-engine-xml>")}</textarea>
    <div><input class="btn btn-primary" type="submit" value="${uiLabelMap.WebtoolsImportText}"/></div>
  </form>
  <#if messages??>
      <hr />
      <h3>${uiLabelMap.WebtoolsResults}:</h3>
      <#list messages as message>
          <p>${message}</p>
      </#list>
  </#if>
