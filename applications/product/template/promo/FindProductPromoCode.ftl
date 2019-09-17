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
<#if productPromoId??>
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-3" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductPromotionUploadSetOfPromotionCodes}</h2>
</header>
<div role="content">
    <div class="screenlet">
        <div class="screenlet-body">
            <form method="post" class="smart-form" action="<@ofbizUrl>createBulkProductPromoCode</@ofbizUrl>" enctype="multipart/form-data">
              <input type="hidden" name="productPromoId" value="${productPromoId}"/>
	      <table class="basic-table">
		  <tr>
		       <td width="15%"><label>${uiLabelMap.ProductPromoUserEntered} </label></td>
		       <td><select name="userEntered" class="form-control">
			    <option value="Y">${uiLabelMap.CommonY}</option>
			    <option value="N">${uiLabelMap.CommonN}</option>
			</select></td>
		  </tr>
                  <tr>
		       <td><label>${uiLabelMap.ProductPromotionReqEmailOrParty}</label></td>
		       <td><select name="requireEmailOrParty" class="form-control">
			      <option value="N">${uiLabelMap.CommonN}</option>
			      <option value="Y">${uiLabelMap.CommonY}</option>
		           </select>
		       </td>
		  </tr>
                  <tr>
		       <td><label>${uiLabelMap.ProductPromotionUseLimits}: ${uiLabelMap.ProductPromotionPerCode}</label></td>
		       <td><input class="form-control" type="text" size="5" name="useLimitPerCode" /></td>
		  </tr>
                  <tr>
		      <td><label>${uiLabelMap.ProductPromotionPerCustomer}</label></td>
		      <td><input class="form-control" type="text" size="5" name="useLimitPerCustomer" /></td>
		  </tr>
                <tr>
                  <td><input type="file" size="40" class="btn btn-default" name="uploadedFile" /></td>
	        </tr>
		<tr>
                  <td colspan="2"><input type="submit" class="btn btn-primary1" value="${uiLabelMap.CommonUpload}" /></td>
                </tr>
	       </table>
            </form>
        </div>
    </div>
    <br />
</div>
</div>
</article>
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-4" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.ProductPromotionAddSetOfPromotionCodes}</h2>
</header>
<div role="content">
    <div class="screenlet">
        <div class="screenlet-title-bar">
            <h3></h3>
        </div>
        <div class="screenlet-body">
            <form method="post" action="<@ofbizUrl>createProductPromoCodeSet</@ofbizUrl>">
		<input type="hidden" name="productPromoId" value="${productPromoId}"/>
	        <table class="basic-table">
		    <tr>
		       <td width="25%"><label>${uiLabelMap.CommonQuantity}:</label></td>
		       <td><input class="form-control" type="text" size="5" name="quantity" /></td>
		    </tr>
		    <tr>
		       <td><label>${uiLabelMap.ProductPromoCodeLength}:</label></td>
		       <td><input class="form-control" type="text" size="12" name="codeLength" /></td>
		    </tr>
		    <tr>
		       <td><label>${uiLabelMap.ProductPromoCodeLayout}:</label></td>
		       <td><select class="form-control" name="promoCodeLayout">
			    <option value="smart">${uiLabelMap.ProductPromoLayoutSmart}</option>
			    <option value="normal">${uiLabelMap.ProductPromoLayoutNormal}</option>
			    <option value="sequence">${uiLabelMap.ProductPromoLayoutSeqNum}</option>
			</select><span class="tooltip1">${uiLabelMap.ProductPromoCodeLayoutTooltip}</span>
		       </td>
		    </tr>
		    <tr>
		       <td><label>${uiLabelMap.ProductPromoUserEntered}:</label></td>
		       <td><select class="form-control" name="userEntered">
			    <option value="Y">${uiLabelMap.CommonY}</option>
			    <option value="N">${uiLabelMap.CommonN}</option>
			   </select>
		       </td>
		    </tr>
		    <tr>
		       <td><label>${uiLabelMap.ProductPromotionReqEmailOrParty}:</label></td>
		       <td> <select class="form-control" name="requireEmailOrParty">
			       <option value="N">${uiLabelMap.CommonN}</option>
			       <option value="Y">${uiLabelMap.CommonY}</option>
			   </select>
		       </td>
		    </tr>
	            <tr>
		       <td><label>${uiLabelMap.ProductPromotionUseLimits}:${uiLabelMap.ProductPromotionPerCode}</label></td>
		       <td><input class="form-control" type="text" size="5" name="useLimitPerCode" /></td>
		    </tr>
	            <tr>
		       <td><label>${uiLabelMap.ProductPromotionPerCustomer}</label></td>
		       <td><input class="form-control" type="text" size="5" name="useLimitPerCustomer" /></td>
		    </tr>
                    <tr><td colspan="3"><input type="submit" class="btn btn-primary1" value="${uiLabelMap.CommonAdd}" /></td></tr>
		 </table>
            </form>
        </div>
    </div>
</div>
</div>
</article>
</div>
</#if>