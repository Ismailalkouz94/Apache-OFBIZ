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
<div id="content-main-section" style="leftonly">
<div id="centerdiv" style="no-clear">			
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductAlternateKeyWordThesaurus}</h2>
</header>
<div role="content">
<div class="screenlet">

    <div class="screenlet-body">
        <form method="post" action="<@ofbizUrl>createKeywordThesaurus</@ofbizUrl>">
          <div class="col col-12">
            <span class="col col-6">
                <label>${uiLabelMap.ProductKeyword}
                <input class="form-control input-md" style="width:176px;margin-right:10px;margin-top:10px" type="text" name="enteredKeyword" size="10"/></label>
                <label>${uiLabelMap.ProductAlternate}
                <input class="form-control input-md" style="width:176px;margin-right:10px;margin-top:10px" type="text" name="alternateKeyword" size="10"/></label>
                <label>${uiLabelMap.ProductRelationship}
                <select class="form-control input-md" style="width:176px;margin-right:10px;margin-top:10px" name="relationshipEnumId">
                <#list relationshipEnums as relationshipEnum>
                <option value="${relationshipEnum.enumId}">${relationshipEnum.get("description",locale)}</option></label>
                </#list>
                </select>
               </span>
               <span class="col col-6">
                <input class="btn btn-primary" style="width:130px;height:30px;margin-top:0;" type="submit" value="${uiLabelMap.CommonAdd}"/>
                </span>
        </div>
        </form>
         <br/>
        <div class="btn-group">
            <#list letterList as letter>
              <#if letter == firstLetter><#assign highlight=true><#else><#assign highlight=false></#if>
          <a data-provider="bootstrap-markdown" style="width:40px;background:#333;color:#fff;border-radius:5px" data-handler="bootstrap-markdown-cmdBold" class="btn-default btn"  href="<@ofbizUrl>editKeywordThesaurus?firstLetter=${letter}</@ofbizUrl>" class="buttontext"><#if highlight>[</#if> ${letter} <#if highlight>]</#if></a>
            </#list>
        </div>
        <br /><br /><br />
        <#assign lastkeyword = "">
        <#if keywordThesauruses?has_content>
        <table cellspacing="0" class="basic-table">
            <#assign rowClass = "2">
            <#list keywordThesauruses as keyword>
              <#assign relationship = keyword.getRelatedOne("RelationshipEnumeration", true)>
              <#if keyword.enteredKeyword == lastkeyword><#assign sameRow=true><#else><#assign lastkeyword=keyword.enteredKeyword><#assign sameRow=false></#if>
              <#if sameRow == false>
                <#if (keyword_index > 0)>
                  </td>
                </tr>
                </#if>
                <tr valign="middle"<#if rowClass == "1"> class="alternate-row"</#if>>
                  <td>
                    <form method="post" action="<@ofbizUrl>createKeywordThesaurus</@ofbizUrl>">
                      <div>
                        ${keyword.enteredKeyword}
                        <form method="post" action="<@ofbizUrl>deleteKeywordThesaurus</@ofbizUrl>" name="deleteKeywordThesaurus">
                          <input type="hidden" name="enteredKeyword" value="${keyword.enteredKeyword}" />
                          <input type="hidden" name="alternateKeyword" value="${keyword.alternateKeyword}" />
                          <input type="submit" value="${uiLabelMap.CommonDeleteAll}" />
                        </form>
                      </div>
                      <div>
                        <input type="hidden" name="enteredKeyword" value="${keyword.enteredKeyword}" />
                        <span class="label">${uiLabelMap.ProductAlternate}</span><input type="text" name="alternateKeyword" size="10" />
                        <span class="label">${uiLabelMap.ProductRelationship}</span><select name="relationshipEnumId"><#list relationshipEnums as relationshipEnum><option value="${relationshipEnum.enumId}">${relationshipEnum.get("description",locale)}</option></#list></select>
                        <input type="submit" value="${uiLabelMap.CommonAdd}" />
                      </div>
                    </form>
                  </td>
                  <td>
              </#if>
              <div>
                <form method="post" action="<@ofbizUrl>deleteKeywordThesaurus</@ofbizUrl>" name="deleteKeywordThesaurus">
                  <input type="hidden" name="enteredKeyword" value="${keyword.enteredKeyword}" />
                  <input type="hidden" name="alternateKeyword" value="${keyword.alternateKeyword}" />
                  <input type="submit" value="X" />
                </form>
                ${keyword.alternateKeyword}&nbsp;(${uiLabelMap.ProductRelationship}:${(relationship.get("description",locale))?default(keyword.relationshipEnumId!)})
              </div>
              <#-- toggle the row color -->
              <#if rowClass == "2">
                  <#assign rowClass = "1">
              <#else>
                  <#assign rowClass = "2">
              </#if>
            </#list>
              </td>
            </tr>
        </table>
        </#if>
    </div>
</div>
</div>
</article>
</div>
</div>
</div>