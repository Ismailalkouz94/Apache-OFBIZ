<section id="widget-grid">
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable">

<div  class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>
    <#if !checkAccountData.paymentMethodId??>
      ${uiLabelMap.AccountingAddCheckAccount}
    <#else>
      ${uiLabelMap.PageTitleEditCheckAccount}
    </#if>
</h2>
</header>
<div role="content">
<div class="screenlet">

  <div class="screenlet-body">
    <#if !checkAccountData.paymentMethodId??>
      <form class="smart-form" method="post" action='<@ofbizUrl>createCheckForParty?DONE_PAGE=${donePage}</@ofbizUrl>' name="addcheckform" style='margin: 0;'>
    <#else>
      <form class="smart-form" method="post" action='<@ofbizUrl>updateCheckAccount?DONE_PAGE=${donePage}</@ofbizUrl>' name="addcheckform" style='margin: 0;'>
      <input class="form-control" type="hidden" name='paymentMethodId' value='${paymentMethodData.paymentMethodId}' />
    </#if>
      <input class="form-control" type="hidden" name="partyId" value="${parameters.partyId}"/>
      <table class="basic-table" cellspacing="0">
        <tr>
          <td><label>${uiLabelMap.AccountingNameAccount}</label></td>
          <td>
            <input class="form-control" type="text" class='required' size="30" maxlength="60" name="nameOnAccount" value="${checkAccountData.nameOnAccount!}"/>
            <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
          </td>
        </tr>
        <tr>
          <td><label>${uiLabelMap.AccountingCompanyNameAccount}</label></td>
          <td>
            <input type="text" class="form-control" size="30" maxlength="60" name="companyNameOnAccount" value="${checkAccountData.companyNameOnAccount!}"/>
          </td>
        </tr>
        <tr>
          <td><label>${uiLabelMap.AccountingBankName}</label></td>
          <td>
            <input class="form-control" type="text" class='required' size="30" maxlength="60" name="bankName" value="${checkAccountData.bankName!}" />
            <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
          </td>
        </tr>
        <tr>
          <td><label>${uiLabelMap.CommonPaymentMethodType}</label></td>
          <td>
            <select class="form-control" name="paymentMethodTypeId" class='required'>
              <option>${paymentMethodData.paymentMethodTypeId!}</option>
              <option></option>
              <option>CERTIFIED_CHECK</option>
              <option>COMPANY_CHECK</option>
              <option>PERSONAL_CHECK</option>
            </select>
            <span class="tooltip1">${uiLabelMap.CommonRequired}</span>
          </td>
        </tr>
        <tr>
          <td><label>${uiLabelMap.AccountingRoutingNumber}</label></td>
          <td>
            <input class="form-control" type="text" size="10" maxlength="30" name="routingNumber" value="${checkAccountData.routingNumber!}"/>
          </td>
        </tr>
        <tr>
          <td><label>${uiLabelMap.AccountingAccountType}</label></td>
          <td>
            <select class="form-control" name="accountType">
              <option>${checkAccountData.accountType!}</option>
              <option></option>
              <option>${uiLabelMap.CommonChecking}</option>
              <option>${uiLabelMap.CommonSavings}</option>
            </select>
          </td>
        </tr>
        <tr>
          <td><label>${uiLabelMap.AccountingAccountNumber}</label></td>
          <td>
            <input class="form-control" type="text" size="20" maxlength="40" name="accountNumber" value="${checkAccountData.accountNumber!}"/>
          </td>
        </tr>
        <tr>
          <td><label>${uiLabelMap.CommonDescription}</label></td>
          <td>
            <input class="form-control" type="text" size="30" maxlength="60" name="description" value="${paymentMethodData.description!}"/>
          </td>
        </tr>
        <tr>
          <td><label>${uiLabelMap.PartyBillingAddress}</label></td>
          <td>
            <table cellspacing="0">
              <#if curPostalAddress??>
                <tr>
                  <td class="button-col">
                    <label class="radio radio-inline"><input class="radiobox" type="radio" name="contactMechId" value="${curContactMechId}" checked="checked" /><span></span></label>
                  </td>
                  <td>
                    <p><b>${uiLabelMap.PartyUseCurrentAddress}:</b></p>
                    <#list curPartyContactMechPurposes as curPartyContactMechPurpose>
                    <#assign curContactMechPurposeType = curPartyContactMechPurpose.getRelatedOne("ContactMechPurposeType", true)>
                    <p><b>${curContactMechPurposeType.get("description",locale)!}</b></p>
                    <#if curPartyContactMechPurpose.thruDate??>
                    <p>(${uiLabelMap.CommonExpire}:${curPartyContactMechPurpose.thruDate.toString()})</p>
                    </#if>
                    </#list>
                      <#if curPostalAddress.toName??><p><b>${uiLabelMap.CommonTo}:</b> ${curPostalAddress.toName}</p></#if>
                      <#if curPostalAddress.attnName??><p><b>${uiLabelMap.PartyAddrAttnName}:</b> ${curPostalAddress.attnName}</p></#if>
                      <#if curPostalAddress.address1??><p>${curPostalAddress.address1}</p></#if>
                      <#if curPostalAddress.address2??><p>${curPostalAddress.address2}</p></#if>
                      <p>${curPostalAddress.city}<#if curPostalAddress.stateProvinceGeoId?has_content>,&nbsp;${curPostalAddress.stateProvinceGeoId}</#if>&nbsp;${curPostalAddress.postalCode}</p>
                      <#if curPostalAddress.countryGeoId??><p>${curPostalAddress.countryGeoId}</p></#if>
                      <p>(${uiLabelMap.CommonUpdated}:&nbsp;${(curPartyContactMech.fromDate.toString())!})</p>
                      <#if curPartyContactMech.thruDate??><p><b>${uiLabelMap.CommonDelete}:&nbsp;${curPartyContactMech.thruDate.toString()}</b></p></#if>
                  </td>
                </tr>
              </#if>
                <#list postalAddressInfos as postalAddressInfo>
                  <#assign contactMech = postalAddressInfo.contactMech>
                  <#assign partyContactMechPurposes = postalAddressInfo.partyContactMechPurposes>
                  <#assign postalAddress = postalAddressInfo.postalAddress>
                  <#assign partyContactMech = postalAddressInfo.partyContactMech>
                    <tr>
                      <td class="button-col">
                        <label class="radio radio-inline"><input class="radiobox" type='radio' name='contactMechId' value='${contactMech.contactMechId}' /><span></span></label>
                      </td>
                      <td>
                        <#list partyContactMechPurposes as partyContactMechPurpose>
                          <#assign contactMechPurposeType = partyContactMechPurpose.getRelatedOne("ContactMechPurposeType", true)>
                          <p><b>${contactMechPurposeType.get("description",locale)!}</b></p>
                          <#if partyContactMechPurpose.thruDate??><p>(${uiLabelMap.CommonExpire}:${partyContactMechPurpose.thruDate})</p></#if>
                        </#list>
                        <#if postalAddress.toName??><p><b>${uiLabelMap.CommonTo}:</b> ${postalAddress.toName}</p></#if>
                        <#if postalAddress.attnName??><p><b>${uiLabelMap.PartyAddrAttnName}:</b> ${postalAddress.attnName}</p></#if>
                        <#if postalAddress.address1??><p>${postalAddress.address1}</p></#if>
                        <#if postalAddress.address2??><p>${postalAddress.address2}</p></#if>
                        <p>${postalAddress.city}<#if postalAddress.stateProvinceGeoId?has_content>,&nbsp;${postalAddress.stateProvinceGeoId}</#if>&nbsp;${postalAddress.postalCode}</p>
                        <#if postalAddress.countryGeoId??><p>${postalAddress.countryGeoId}</p></#if>
                        <p>(${uiLabelMap.CommonUpdated}:&nbsp;${(partyContactMech.fromDate.toString())!})</p>
                        <#if partyContactMech.thruDate??><p><b>${uiLabelMap.CommonDelete}:&nbsp;${partyContactMech.thruDate.toString()}</b></p></#if>
                      </td>
                    </tr>
                  </#list>
                  <#if !postalAddressInfos?has_content && !curContactMech??>
                  <tr><td colspan='2'>${uiLabelMap.PartyNoContactInformation}.</td></tr>
                </#if>
              </table>
            </td>
          </tr>
      </table>
    </form>
    <div class="button-bar">
      <a class="btn btn-primary1" href="<@ofbizUrl>${donePage}?partyId=${partyId}</@ofbizUrl>" class="smallSubmit">${uiLabelMap.CommonCancelDone}</a>
      <a class="btn btn-primary1" href="javascript:document.addcheckform.submit()" class="smallSubmit">${uiLabelMap.CommonSave}</a>
    </div>
  </div>
</div>
</div>
</div>
</article>
</div>
</section>