<section id="widget-grid">
<div class="row">
<article class="col-xs-12 col-sm-12 col-md-12 col-lg-12 sortable-grid ui-sortable"">
<div class="jarviswidget jarviswidget-color-blueDark" id="wid-id-0" data-widget-editbutton="false">
<header>
<h2>${uiLabelMap.AccountingInvoiceHeader}</h2>
</header>
<div role="content">
       <div id="accountingInvoiceHeader" class="screenlet">
     <div class="screenlet-body">

     <#assign invoiceType = delegator.findAll("InvoiceType",true) />
     <#assign statusItem = delegator.findAll("StatusItem",true) />
   
        <table class="basic-table" cellspacing="0"> 

        <tr>

           <td class="headerrow">${uiLabelMap.FormFieldTitle_invoiceTypeId}</td>
           <td> <#list invoiceType as list2>
                     <#if list2.invoiceTypeId = (invoice.invoiceTypeId)! "0"> ${(list2.description)!}</#if>
                </#list>
          </td>         
           <td class="headerrow">${uiLabelMap.CommonStatus}</td>  
           <td><#list statusItem as list2>
                     <#if list2.statusId = (invoice.statusId)! "0"> ${(list2.description)!}</#if>
                </#list>
         </td> 
        </tr>  
           <tr>

           <td class="headerrow">${uiLabelMap.AcctgGlJrnlDescLabel}</td>
           <td>${(invoice.description)!}  </td>
           <td class="headerrow">${uiLabelMap.FormFieldTitle_invoiceMessage}</td>  
           <td>${(invoice.invoiceMessage)!} </td> 
        </tr>  
         <tr>
          
           <td class="headerrow">${uiLabelMap.AccountingFromPartyId}</td>
           <td> <a href="/partymgr/control/viewprofile?partyId=${(invoice.partyIdFrom)!}">${(partyNameResultFrom.fullName)!} [${(invoice.partyIdFrom)!}]</a>
        </td>
           <td class="headerrow">${uiLabelMap.AccountingToPartyId}</td>  
           <td><a href="/partymgr/control/viewprofile?partyId=${(invoice.partyId)!}">${(partyNameResultTo.fullName)!}  [${(invoice.partyId)!}]</a>
          </td> 
        </tr>
       <tr>
          
           <td class="headerrow">${uiLabelMap.AccountingRoleType}</td>
           <td> ${(invoice.roleTypeId)!}</td>
           <td class="headerrow">${uiLabelMap.FormFieldTitle_billingAccountId}</td>  
           <td> ${(invoice.billingAccountId)!}</td> 
        </tr> 
           <tr>
           <td class="headerrow">${uiLabelMap.AccountingInvoiceDate}</td>
           <td> ${(invoice.invoiceDate).toString().substring(0,10)!}</td>
           <td class="headerrow">${uiLabelMap.FormFieldTitle_dueDate}</td>  
           <td> ${(invoice.dueDate)!""}</td> 
        </tr> 
          <tr>
         
           <td class="headerrow">${uiLabelMap.FormFieldTitle_totalAmount}</td>
           <td>${(invoice.currencyUomId)!}${(total)!}</td>
            
           <td class="headerrow">${uiLabelMap.FormFieldTitle_paidDate}</td>  
           <td> ${(invoice.paidDate)!}</td> 
        </tr> 
         <tr>
           <td class="headerrow">${uiLabelMap.FormFieldTitle_referenceNum}</td>
           <td> ${(invoice.recurrenceInfoId)!}</td>

        </tr> 
            <tr>
           <td hidden  class="headerrow"> ${(invoice.invoiceId)!}</td>
           <td hidden> ${(invoice.currencyUomId)!}</td>
        </tr> 
    </table>
    </div>
  </div>

 
</div>
</div>
</article>
</div>
