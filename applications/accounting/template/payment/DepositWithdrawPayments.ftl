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

<script language="JavaScript" type="text/javascript">
function togglePaymentId(master) {
    var form = document.depositWithdrawPaymentsForm;
    var payments = form.elements.length;
    for (var i = 0; i < payments; i++) {
        var element = form.elements[i];
        if (element.name == "paymentIds") {
            element.checked = master.checked;
        }
    }
    getPaymentRunningTotal();
}
function getPaymentRunningTotal() {
    var form = document.depositWithdrawPaymentsForm;
    var payments = form.elements.length;
    var isSingle = true;
    var isAllSelected = true;
    for (var i = 0; i < payments; i++) {
        var element = form.elements[i];
        if (element.name == "paymentIds") {
            if (element.checked) {
                isSingle = false;
            } else {
                isAllSelected = false;
            }
        }
    }

    if (isAllSelected) {
        jQuery('#checkAllPayments').attr('checked', true);
    } else {
        jQuery('#checkAllPayments').attr('checked', false);
    }
    if (!isSingle) {
        jQuery('#submitButton').removeAttr('disabled');
        jQuery.ajax({
            url: 'getPaymentRunningTotal',
            async: false,
            type: 'POST',
            data: jQuery('#depositWithdrawPaymentsForm').serialize(),
            success: function(data) {
                jQuery('#showPaymentRunningTotal').html(data.paymentRunningTotal);
            }
        });
    } else {
        jQuery('#showPaymentRunningTotal').html("");
        jQuery('#submitButton').attr('disabled', true);
    }
}


</script>
<div class="screenlet">
    <div class="screenlet-body">
        <form id="depositWithdrawPaymentsForm" name="depositWithdrawPaymentsForm" method="post" action="<@ofbizUrl>depositWithdrawPayments</@ofbizUrl>">
            <#if paymentList?has_content>
                <input class="form-control" type="hidden" name='organizationPartyId' value="${organizationPartyId!}" />
                <input class="form-control" type="hidden" name='finAccountId' value="${finAccountId!}" />
                <input  class="form-control" type="hidden" name='paymentMethodTypeId' value="${paymentMethodTypeId!}" />
                <input class="form-control" type="hidden" name='cardType' value="${cardType!}" />
                <input class="form-control" type="hidden" name='partyIdFrom' value="${partyIdFrom!}" />
                <input class="form-control" type="hidden" name='fromDate' value="${fromDate!}" />
                <input class="form-control" type="hidden" name='thruDate' value="${thruDate!}" />
                <input class="form-control" type="hidden" name='paymentGroupTypeId' value="BATCH_PAYMENT" />
                <div>
                    <label>${uiLabelMap.AccountingRunningTotal} :</label>
                    <label id="showPaymentRunningTotal"></label>
                </div>
                <table class="table table-bordered table-striped">
                 <thead>
                    <tr>
                        <th>${uiLabelMap.FormFieldTitle_paymentId}</th>
                        <th>${uiLabelMap.AccountingPaymentType}</th>
                        <th>${uiLabelMap.AccountingFromParty}</th>
                        <th>${uiLabelMap.AccountingToParty}</th>
                        <th>${uiLabelMap.CommonAmount}</th>
                        <th>${uiLabelMap.CommonDate}</th>
                        <th align="right"><label class="checkbox-inline"><input type="checkbox" id="checkAllPayments" class="checkbox style-0" name="checkAllPayments" onchange="javascript:togglePaymentId(this);"/><span>${uiLabelMap.CommonSelectAll}</span></label></th>
                    </tr>
                   </thead>
                    <#assign alt_row = false>
                    <#list paymentList as payment>
                        <tr <#if alt_row> class="alternate-row"</#if>>
                            <td><a href="<@ofbizUrl>paymentOverview?paymentId=${payment.paymentId}</@ofbizUrl>">${payment.paymentId}</a></td>
                            <td>${payment.paymentTypeDesc!}</td>
                            <td>${(payment.partyFromFirstName)!} ${(payment.partyFromLastName)!} ${(payment.partyFromGroupName)!}</td>
                            <td>${(payment.partyToFirstName)!} ${(payment.partyToLastName)!} ${(payment.partyToGroupName)!}</td>
                            <td><@ofbizCurrency amount=payment.amount isoCode=payment.currencyUomId/></td>
                            <td>${payment.effectiveDate!}</td>
                            <td align="right"><label class="checkbox-inline"><input class="checkbox style-0" type="checkbox" id="paymentId_${payment_index}" name="paymentIds" value="${payment.paymentId}" onclick="javascript:getPaymentRunningTotal();"/><span>${uiLabelMap.AccountingDeposit}</span></label></td>
                        </tr>
                        <#assign alt_row = !alt_row>
                    </#list>
                    <div align="right">
                        <label>${uiLabelMap.AccountingPayment} ${uiLabelMap.PartyPartyGroupName}</label>
                        <input class="form-control" type="text" size='25' id="paymentGroupName" name='paymentGroupName' />
                        <label>${uiLabelMap.AccountingGroupInOneTransaction}</label>
                        <input type="checkbox" class="checkbox style-0" name="groupInOneTransaction" value="Y" checked="checked" /></label>
                        <input id="submitButton" class="btn btn-primary" type="button"  onclick="javascript:document.depositWithdrawPaymentsForm.submit();" value="${uiLabelMap.AccountingDepositWithdraw}" disabled="disabled"/>
                    </div>
                </table>
            <#else>
                <div class="alert alert-info fade in">${uiLabelMap.CommonNoRecordFound}</div>
            </#if>
        </form>
    </div>
</div>
