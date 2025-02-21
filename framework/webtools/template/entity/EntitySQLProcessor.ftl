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

<section id="widget-grid"><div class="row"><article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">

<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-2" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.WebtoolsResults}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
    <#if resultMessage?has_content>
      ${resultMessage}
      <br />
    </#if>

    <#if columns?has_content>
        <table class="basic-table hover-bar" cellspacing="0">
            <tr class="header-row">
            <#list columns as column>
                <td>${column}</td>
            </#list>
            </tr>
            <#if records?has_content>
            <#assign alt_row = false>
            <#list records as record>
                <tr <#if alt_row> class="alternate-row"</#if> >
                <#list record as field>
                    <td>${field!}</td>
                </#list>
                </tr>
                <#assign alt_row = !alt_row>
            </#list>
            </#if>
        </table>
    </#if>
  </div>
</div>
</div>
</div>
</article>
</div>
</section>
