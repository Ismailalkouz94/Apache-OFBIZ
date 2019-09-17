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

<#include "component://common/template/includes/GoogleGeoLocation.ftl"/>

<body onload="load()">
  <section id="widget-grid">
     <div class="row">
       <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
	<header>
	<span class="widget-icon"> <i class="fa fa-map-marker"></i> </span>
	<h2>Night Vision</h2>
	</header>
        <div role="content">
        <div class="widget-body no-padding">
        <div align="center" id="map" class="google_maps"></div>
        </div>
         <form action="#" onsubmit="showAddress(this.address.value); return false">
            <input class="form-control" type="text" size="50" name="address"/>
            <input class="btn btn-primary" type="submit" value="${uiLabelMap.CommonSearch}"/>
        </form>
        <br/><br/>
        <form id="updateMapForm" method="post" action="<@ofbizUrl>editGeoLocation</@ofbizUrl>">
            <input class="form-control" type="hidden" name="partyId" value="${partyId!}"/>
            <input class="form-control" type="hidden" name="geoPointId" value="${geoPointId!}"/>
            <input class="form-control" type="hidden" name="lat" id="lat"/>
            <input class="form-control" type="hidden" name="lng" id="lng"/>
            <input class="btn btn-primary1" type="submit" id="createMapButton" class="smallSubmit" value="${uiLabelMap.CommonSubmit}">
        </form>
        <br/><br/><br/>
</div>
</div>
</article>
</div>
</section>
</body>
