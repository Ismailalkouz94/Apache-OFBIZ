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

<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">

<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PageTitleAddressMatchMap}</h2>
</header>
<div role="content">
<div id="address-match-map" class="screenlet">
  <div class="div-button">
      <li><a class="button1 facebook" href="<@ofbizUrl>findAddressMatch</@ofbizUrl>">${uiLabelMap.PageTitleFindMatches}</a></li>
  </div>
  <div class="screenlet-body">
    <form name="addaddrmap" method="post" action="<@ofbizUrl>createAddressMatchMap</@ofbizUrl>">
    <table class="basic-table" cellspacing="0">
        <tr>
          <td><label>${uiLabelMap.PartyAddressMatchKey}</label></td>
          <td><input type="text" name="mapKey"/></td>
        </tr>
        <tr>
          <td><label>${uiLabelMap.PartyAddressMatchValue}</label></td>
          <td><input type="text" name="mapValue"/></td>
        </tr>
        <tr>
          <td><label>${uiLabelMap.CommonSequence}</label></td>
          <td><input type="text" size="5" name="sequenceNum" value="0"/></td>
        </tr>
        <tr>
          <td></td>
          <td><a href="javascript:document.addaddrmap.submit()" class="btn btn-primary1">${uiLabelMap.CommonCreate}</a></td>
        </tr>
    </table>
    </form>
    <table class="basic-table" cellspacing="0">
      <tr><td colspan="2">&nbsp;</td></tr>
        <tr>
          <td></td>
          <td>
            <form name="importaddrmap" method="post" enctype="multipart/form-data" action="<@ofbizUrl>importAddressMatchMapCsv</@ofbizUrl>">
            <input type="file" class="btn btn-default" name="uploadedFile" size="14"/>
            <input type="submit" class="btn btn-default" value="${uiLabelMap.CommonUpload} CSV"/>
             <br/> <br/>
            <p><label>${uiLabelMap.PartyAddressMatchMessage1}</label></p>
            </form>
          </td>
        </tr>
    </table>
  </div>
</div>
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.PageTitleAddressMatchMap}</h2>
</header>
<div role="content">
<div class="screenlet">

  <div class="screenlet-body">
      <#if addressMatchMaps?has_content>
        <table class="table table-bordered table-striped" cellspacing="0">
         <thead>
          <tr>
            <td>${uiLabelMap.PartyAddressMatchKey}</td>
            <td>=></td>
            <td>${uiLabelMap.PartyAddressMatchValue}</td>
            <td>${uiLabelMap.CommonSequence}</td>
            <td class="button-col"><a href="<@ofbizUrl>clearAddressMatchMap</@ofbizUrl>">${uiLabelMap.CommonClear} ${uiLabelMap.CommonAll}</a></td>
          </tr>
         </thead>
          <#assign alt_row = false>
          <#list addressMatchMaps as map>
            <tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
              <td>${map.mapKey}</td>
              <td>=></td>
              <td>${map.mapValue}</td>
              <td>${map.sequenceNum!}</td>
              <td class="button-col">
                <form name="removeAddressMatchMap_${map_index}" method="post" action="<@ofbizUrl>removeAddressMatchMap</@ofbizUrl>">
	              <input type="hidden" name="mapKey" value="${map.mapKey}" />
	              <input type="hidden" name="mapValue" value="${map.mapValue}" />
	              <input type="submit" value="${uiLabelMap.CommonDelete}" />
	            </form>
	          </td>
            </tr>
            <#-- toggle the row color -->

            <#assign alt_row = !alt_row>
          </#list>
        </table>
      </#if>
  </div>
</div>
</div>
<!-- end addressMatchMap.ftl -->
</div>
</article>
</div>
