 <script src="/images/jssh/jquery.min.js" type="text/javascript"></script>
    <link href="/images/csssh/jquery.akordeon.css" rel="stylesheet" type="text/css" />
    <link href="/images/csssh/demo.css" rel="stylesheet" type="text/css" />
    <script src="/images/jssh/jquery.akordeon.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#buttons').akordeon();
            $('#button-less').akordeon({ buttons: false, toggle: true, itemsOrder: [2, 0, 1] });
        });
    </script>
    <style type="text/css">
        .demobar
        {
            height: 90px;
            line-height: 90px;
        }
        .demobar .fleft
        {
            float: left;
            margin-left: 10px;
        }
        .demobar .fright
        {
            float: right;
            margin-top: 14px;
            margin-right: 10px;
        }
        .akordeon-item-content ul li a{
             color: #89a8ad;
             font-weight: bold;
        }
       
    </style>
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.WebtoolsMainPage}</h2>
</header>
<div role="content">
<div class="screenlet">
  <div class="screenlet-body">
<#if !userLogin?has_content>
  <div>${uiLabelMap.WebtoolsForSomethingInteresting}.</div>
  <br />
  <div>${uiLabelMap.WebtoolsNoteAntRunInstall}</div>
  <br />
  <div><a href="<@ofbizUrl>checkLogin</@ofbizUrl>">${uiLabelMap.CommonLogin}</a></div>
</#if>
<#if userLogin?has_content>
    <div id="demo-wrapper">
        <div class="akordeon" id="buttons">
            <div class="akordeon-item expanded">
                <div class="akordeon-item-head">
                    <div class="akordeon-item-head-container">
                        <div class="akordeon-heading">
                            ${uiLabelMap.WebtoolsCacheDebugTools}
                        </div>
                    </div>
                </div>
                <div class="akordeon-item-body">
                    <div class="akordeon-item-content">                    
                            <ul style="list-style: circle;">
				<li><a href="<@ofbizUrl>FindUtilCache</@ofbizUrl>">${uiLabelMap.WebtoolsCacheMaintenance}</a></li>
				<li><a href="<@ofbizUrl>LogConfiguration</@ofbizUrl>">${uiLabelMap.WebtoolsAdjustDebuggingLevels}</a></li>
				<li><a href="<@ofbizUrl>LogView</@ofbizUrl>">${uiLabelMap.WebtoolsViewLog}</a></li>
				<li><a href="<@ofbizUrl>ViewComponents</@ofbizUrl>">${uiLabelMap.WebtoolsViewComponents}</a></li>
                            </ul>
                    </div>
                </div>
            </div>
             <#if security.hasPermission("ARTIFACT_INFO_VIEW", session)>
            <div class="akordeon-item">
                <div class="akordeon-item-head">
                    <div class="akordeon-item-head-container">
                        <div class="akordeon-heading">
                          ${uiLabelMap.WebtoolsGeneralArtifactInfoTools}
                        </div>
                    </div>
                </div>
                <div class="akordeon-item-body">
                    <div class="akordeon-item-content">
                             <ul style="list-style: circle;">   
				<li><a href="<@ofbizUrl>ViewComponents</@ofbizUrl>" target="_blank">${uiLabelMap.WebtoolsArtifactInfo}</a></li>
				<li><a href="<@ofbizUrl>entityref</@ofbizUrl>" target="_blank">${uiLabelMap.WebtoolsEntityReference} - ${uiLabelMap.WebtoolsEntityReferenceInteractiveVersion}</a></li>
				<li><a href="<@ofbizUrl>ServiceList</@ofbizUrl>">${uiLabelMap.WebtoolsServiceReference}</a></li>			     
                             </ul>
                     </div>
                </div>
            </div>
             </#if>
             <#if security.hasPermission("LABEL_MANAGER_VIEW", session)>
            <div class="akordeon-item">
                <div class="akordeon-item-head">
                    <div class="akordeon-item-head-container">
                        <div class="akordeon-heading">
                            ${uiLabelMap.WebtoolsLabelManager}
                        </div>
                    </div>
                </div>
                <div class="akordeon-item-body">
                    <div class="akordeon-item-content">
                             <ul style="list-style: circle;">   
				<li><a href="<@ofbizUrl>SearchLabels</@ofbizUrl>">${uiLabelMap.WebtoolsLabelManager}</a></li>	
                             </ul>		     
                    </div>
                </div>
            </div>
            </#if>
        <#if security.hasPermission("ENTITY_MAINT", session)>
            <div class="akordeon-item">
                <div class="akordeon-item-head">
                    <div class="akordeon-item-head-container">
                        <div class="akordeon-heading">
                            ${uiLabelMap.WebtoolsEntityEngineTools}
                        </div>
                    </div>
                </div>
                <div class="akordeon-item-body">
                    <div class="akordeon-item-content">
                       <ul style="list-style: circle;">   
			<li><a href="<@ofbizUrl>entitymaint</@ofbizUrl>">${uiLabelMap.WebtoolsEntityDataMaintenance}</a></li>
			<li><a href="<@ofbizUrl>entityref</@ofbizUrl>" target="_blank">${uiLabelMap.WebtoolsEntityReference} - ${uiLabelMap.WebtoolsEntityReferenceInteractiveVersion}</a></li>
			<li><a href="<@ofbizUrl>entityref?forstatic=true</@ofbizUrl>" target="_blank">${uiLabelMap.WebtoolsEntityReference} - ${uiLabelMap.WebtoolsEntityReferenceStaticVersion}</a></li>
			<li><a href="<@ofbizUrl>entityrefReport</@ofbizUrl>" target="_blank">${uiLabelMap.WebtoolsEntityReferencePdf}</a></li>
			<li><a href="<@ofbizUrl>EntitySQLProcessor</@ofbizUrl>">${uiLabelMap.PageTitleEntitySQLProcessor}</a></li>
			<li><a href="<@ofbizUrl>EntitySyncStatus</@ofbizUrl>">${uiLabelMap.WebtoolsEntitySyncStatus}</a></li>
			<li><a href="<@ofbizUrl>view/ModelInduceFromDb</@ofbizUrl>" target="_blank">${uiLabelMap.WebtoolsInduceModelXMLFromDatabase}</a></li>
			<li><a href="<@ofbizUrl>EntityEoModelBundle</@ofbizUrl>">${uiLabelMap.WebtoolsExportEntityEoModelBundle}</a></li>
			<li><a href="<@ofbizUrl>view/checkdb</@ofbizUrl>">${uiLabelMap.WebtoolsCheckUpdateDatabase}</a></li>
			<li><a href="<@ofbizUrl>ConnectionPoolStatus</@ofbizUrl>">${uiLabelMap.ConnectionPoolStatus}</a></li>
                      </ul>
                   </div>
                </div>
            </div>
            <div class="akordeon-item">
                <div class="akordeon-item-head">
                    <div class="akordeon-item-head-container">
                        <div class="akordeon-heading">
                            ${uiLabelMap.WebtoolsEntityXMLTools}
                        </div>
                    </div>
                </div>
                <div class="akordeon-item-body">
                    <div class="akordeon-item-content">
                      <ul style="list-style: circle;">   
			<li><a href="<@ofbizUrl>xmldsdump</@ofbizUrl>">${uiLabelMap.PageTitleEntityExport}</a></li>
			<li><a href="<@ofbizUrl>EntityExportAll</@ofbizUrl>">${uiLabelMap.PageTitleEntityExportAll}</a></li>
			<li><a href="<@ofbizUrl>EntityImport</@ofbizUrl>">${uiLabelMap.PageTitleEntityImport}</a></li>
			<li><a href="<@ofbizUrl>EntityImportDir</@ofbizUrl>">${uiLabelMap.PageTitleEntityImportDir}</a></li>
			<li><a href="<@ofbizUrl>EntityImportReaders</@ofbizUrl>">${uiLabelMap.PageTitleEntityImportReaders}</a></li>
                      </ul>
                   </div>
                </div>
            </div>
            </#if>
	    <#if security.hasPermission("SERVICE_MAINT", session)>
		<div class="akordeon-item">
		    <div class="akordeon-item-head">
			<div class="akordeon-item-head-container">
			    <div class="akordeon-heading">
				${uiLabelMap.WebtoolsServiceEngineTools}
			    </div>
			</div>
		    </div>
		    <div class="akordeon-item-body">
			<div class="akordeon-item-content">
                          <ul style="list-style: circle;">   
			    <li><a href="<@ofbizUrl>ServiceList</@ofbizUrl>">${uiLabelMap.WebtoolsServiceReference}</a></li>
			    <li><a href="<@ofbizUrl>scheduleJob</@ofbizUrl>">${uiLabelMap.PageTitleScheduleJob}</a></li>
			    <li><a href="<@ofbizUrl>runService</@ofbizUrl>">${uiLabelMap.PageTitleRunService}</a></li>
			    <li><a href="<@ofbizUrl>FindJob</@ofbizUrl>">${uiLabelMap.PageTitleJobList}</a></li>
			    <li><a href="<@ofbizUrl>threadList</@ofbizUrl>">${uiLabelMap.PageTitleThreadList}</a></li>
			    <li><a href="<@ofbizUrl>FindJobManagerLock</@ofbizUrl>">${uiLabelMap.PageTitleJobManagerLockList}</a></li>
			    <li><a href="<@ofbizUrl>ServiceLog</@ofbizUrl>">${uiLabelMap.WebtoolsServiceLog}</a></li>
                          </ul>
		       </div>
		    </div>
		</div>
		</#if>
		<#if security.hasPermission("DATAFILE_MAINT", session)>
			<div class="akordeon-item">
			    <div class="akordeon-item-head">
				<div class="akordeon-item-head-container">
				    <div class="akordeon-heading">
					${uiLabelMap.WebtoolsDataFileTools}
				    </div>
				</div>
			    </div>
			    <div class="akordeon-item-body">
				<div class="akordeon-item-content">
                                  <ul style="list-style: circle;">   
				    <li><a href="<@ofbizUrl>viewdatafile</@ofbizUrl>">${uiLabelMap.WebtoolsWorkWithDataFiles}</a></li>
                                  </ul>
			       </div>
			    </div>
			</div>
		</#if>
			<div class="akordeon-item">
			    <div class="akordeon-item-head">
				<div class="akordeon-item-head-container">
				    <div class="akordeon-heading">
					${uiLabelMap.WebtoolsMiscSetupTools}
				    </div>
				</div>
			    </div>
			    <div class="akordeon-item-body">
				<div class="akordeon-item-content">
				    <#if security.hasPermission("PORTALPAGE_ADMIN", session)>
                                         <ul style="list-style: circle;">   
				              <li><a href="<@ofbizUrl>FindGeo</@ofbizUrl>">${uiLabelMap.WebtoolsGeoManagement}</a></li>
					      <li><a href="<@ofbizUrl>WebtoolsLayoutDemo</@ofbizUrl>">${uiLabelMap.WebtoolsLayoutDemo}</a></li>
                                         </ul>
                                    </#if>
			       </div>
			    </div>
			</div>
			<div class="akordeon-item">
			    <div class="akordeon-item-head">
				<div class="akordeon-item-head-container">
				    <div class="akordeon-heading">
					${uiLabelMap.WebtoolsPerformanceTests}
				    </div>
				</div>
			    </div>
			    <div class="akordeon-item-body">
				<div class="akordeon-item-content">
                                  <ul style="list-style: circle;">   
				    <li><a href="<@ofbizUrl>EntityPerformanceTest</@ofbizUrl>">${uiLabelMap.WebtoolsEntityEngine}</a></li>
                                  </ul>
			       </div>
			    </div>
			</div>
                <#if security.hasPermission("SERVER_STATS_VIEW", session)>
			<div class="akordeon-item">
			    <div class="akordeon-item-head">
				<div class="akordeon-item-head-container">
				    <div class="akordeon-heading">
					${uiLabelMap.WebtoolsServerHitStatisticsTools}
				    </div>
				</div>
			    </div>
			    <div class="akordeon-item-body">
				<div class="akordeon-item-content">
                                  <ul style="list-style: circle;">   
				    <li><a href="<@ofbizUrl>StatsSinceStart</@ofbizUrl>">${uiLabelMap.WebtoolsStatsSinceServerStart}</a></li>
                                  </ul>
			       </div>
			    </div>
			</div>
                 </#if>
			<div class="akordeon-item">
			    <div class="akordeon-item-head">
				<div class="akordeon-item-head-container">
				    <div class="akordeon-heading">
					${uiLabelMap.WebtoolsCertsX509}
				    </div>
				</div>
			    </div>
			    <div class="akordeon-item-body">
				<div class="akordeon-item-content">
                                  <ul style="list-style: circle;">   
				    <li><a href="<@ofbizUrl>myCertificates</@ofbizUrl>">${uiLabelMap.WebtoolsMyCertificates}</a></li>
                                  </ul>
			       </div>
			    </div>
			</div>
        </div>
    
    </div>
    </#if>
</div>
</div>
</article>
</div>
