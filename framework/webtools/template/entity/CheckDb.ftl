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
<h2>${uiLabelMap.WebtoolsCheckUpdateDatabase}</h2>
</header>
<div role="content">

    <form method="post" action="${encodeURLCheckDb}">
        <input type="hidden" name="option" value="checkupdatetables"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="40"/>
        <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="checkPks" value="true" checked="checked"/><span>${uiLabelMap.WebtoolsPks}</span></label>
        <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="checkFks" value="true"/><span>${uiLabelMap.WebtoolsFks}</span></label>
        <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="checkFkIdx" value="true"/><span>${uiLabelMap.WebtoolsFkIdx}</span></label>
        <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="addMissing" value="true"/><span>${uiLabelMap.WebtoolsAddMissing}</span></label>
        <label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" name="repair" value="true"/><span>${uiLabelMap.WebtoolsRepairColumnSizes}</span></label>
        <input type="submit" class="btn btn-primary1" value="${uiLabelMap.WebtoolsCheckUpdateDatabase}"/>
    </form><br/>
    ${uiLabelMap.WebtoolsNoteUseAtYourOwnRisk}
    <script language="JavaScript" type="text/javascript">
         function enableTablesRemove() {
             document.forms["TablesRemoveForm"].elements["TablesRemoveButton"].disabled=false;
         }
    </script>
    <h3>${uiLabelMap.WebtoolsRemoveAllTables}</h3>
    <form method="post" action="${encodeURLCheckDb}" name="TablesRemoveForm">
        <input type="hidden" name="option" value="removetables"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="40"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonRemove}" name="TablesRemoveButton" disabled="disabled"/>
        <input type="button" class="btn btn-primary" value="${uiLabelMap.WebtoolsEnable}" onclick="enableTablesRemove();"/>
    </form>
    <form method="post" action="${encodeURLCheckDb}" name="TableRemoveForm">
        <input type="hidden" name="option" value="removetable"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="20"/>
        <label>${uiLabelMap.WebtoolsEntityName}:</label> <input type="text" name="entityName" value="${entityName}" size="20"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonRemove}" name="TablesRemoveButton"/>
    </form>
    <h3>${uiLabelMap.WebtoolsCreateRemoveAllPrimaryKeys}</h3>
    <form method="post" action="${encodeURLCheckDb}">
        <input type="hidden" name="option" value="createpks"/>
       <label> ${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="40"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonCreate}"/>
    </form>
    <form method="post" action="${encodeURLCheckDb}">
        <input type="hidden" name="option" value="removepks"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="40"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonRemove}"/>
    </form>
    <h3>${uiLabelMap.WebtoolsCreateRemovePrimaryKey}</h3>
    <form method="post" action="${encodeURLCheckDb}">
        <input type="hidden" name="option" value="createpk"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="20"/>
       <label> ${uiLabelMap.WebtoolsEntityName}:</label> <input type="text" name="entityName" value="${entityName}" size="20"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonCreate}"/>
    </form>
    <form method="post" action="${encodeURLCheckDb}">
        <input type="hidden" name="option" value="removepk"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="20"/>
        <label>${uiLabelMap.WebtoolsEntityName}:</label> <input type="text" name="entityName" value="${entityName}" size="20"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonRemove}"/>
    </form>
    <h3>${uiLabelMap.WebtoolsCreateRemoveAllDeclaredIndices}</h3>
    <form method="post" action="${encodeURLCheckDb}">
        <input type="hidden" name="option" value="createidx"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="40"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonCreate}"/>
    </form>
    <form method="post" action="${encodeURLCheckDb}">
        <input type="hidden" name="option" value="removeidx"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="40"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonRemove}"/>
    </form>
    <h3>${uiLabelMap.WebtoolsCreateRemoveAllForeignKeyIndices}</h3>
    <form method="post" action="${encodeURLCheckDb}">
        <input type="hidden" name="option" value="createfkidxs"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="40"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonCreate}"/>
    </form>
    <form method="post" action="${encodeURLCheckDb}">
        <input type="hidden" name="option" value="removefkidxs"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="40"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonRemove}"/>
    </form>
    <h3>${uiLabelMap.WebtoolsCreateRemoveAllForeignKeys}</h3>
    <p>${uiLabelMap.WebtoolsNoteForeighKeysMayAlsoBeCreated}</p>
    <form method="post" action="${encodeURLCheckDb}">
        <input type="hidden" name="option" value="createfks"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="40"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonCreate}"/>
    </form>
    <form method="post" action="${encodeURLCheckDb}">
        <input type="hidden" name="option" value="removefks"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="40"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonRemove}"/>
    </form>
    <h3>${uiLabelMap.WebtoolsUpdateCharacterSetAndCollate}</h3>
    <form method="post" action="${encodeURLCheckDb}">
        <input type="hidden" name="option" value="updateCharsetCollate"/>
        <label>${uiLabelMap.WebtoolsGroupName}:</label> <input type="text" name="groupName" value="${groupName}" size="40"/>
        <input type="submit" class="btn btn-primary" value="${uiLabelMap.CommonUpdate}"/>
    </form>
<#if miters?has_content>
    <hr />
    <ul>
        <#list miters as miter>
            <li>${miter}</li>
        </#list>
    </ul>
</#if>
</div>
</div>
</article>
</div>

