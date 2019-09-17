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

<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<span class="jarviswidget-loader"><i class="fa fa-refresh fa-spin"></i></span>
</header>
<div role="content">
<#if wikiContent?has_content>
    ${StringUtil.wrapString(wikiContent)}
<#else>
    <div class="alert alert-info fade in">${uiLabelMap.CommonHelpNotFound}</div>
</#if>
</div>
</div>
</article>
</div>
</section>
