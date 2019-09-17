<#assign listPayments = (delegator.findList("PaymentAndTypeAndCreditCard", null, null, null, null, true))!>

<div class="row">
  <article class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
    <div  class="jarviswidget jarviswidget-color-darken jarviswidget-sortable"  id="wid-id-0" data-widget-editbutton="false" role="widget">			
      <div role="content">
        <table id="paymentList"></table>
        <div id="paymentListDiv"></div>
      </div>
    </div>
  </article>
</div>
<script>
function rowSelected(){
}
function fillGradeData() {
    var jqgrid_data = [];
    <#list listPayments as list>
      <#if list.partyIdFrom.equals(userLogin.partyId) || list.partyIdTo.equals(userLogin.partyId)>
	    jqgrid_data.push({
		PaymentId: "${list.paymentId!}",
		PaymentType: "${list.paymentTypeId!}",
		Status: "${list.statusId!}",
		Comments: "${list.comments!}",
		FromParty: "${list.partyIdFrom!}",
		ToParty: "${list.partyIdTo!}",
		EffectiveDate: "${list.effectiveDate!}",
		Amount: "${list.amount!}",
		OutstandingAmount: "${list.amount!}"
	    });
      </#if>
    </#list>
    colNames = ['Payment Id','Payment Type','Status','Comments', 'From Party', 'To Party','Effective Date','Amount','Outstanding amount'],
            colModel = [{
                    name: 'PaymentId',
                    index: 'PaymentId'
                },{
                    name: 'PaymentType',
                    index: 'PaymentType'
                },{
                    name: 'Status',
                    index: 'Status'
                },{
                    name: 'Comments',
                    index: 'Comments'
                },{
                    name: 'ToParty',
                    index: 'ToParty'
                },{
                    name: 'FromParty',
                    index: 'FromParty'
                },{
                    name: 'EffectiveDate',
                    index: 'EffectiveDate'
                },{
                    name: 'Amount',
                    index: 'Amount'
                },{
                    name: 'OutstandingAmount',
                    index: 'OutstandingAmount'
                }];

    getGridData(colNames,colModel,jqgrid_data,"paymentList","paymentListDiv","Payment List","date","desc"); 
}

$(window).load(function () {
    fillGradeData();
});
</script>    
 