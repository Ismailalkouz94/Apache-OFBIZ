///*
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements.  See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership.  The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing,
// * software distributed under the License is distributed on an
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// * KIND, either express or implied.  See the License for the
// * specific language governing permissions and limitations
// * under the License.
// 
// 
//import org.apache.ofbiz.party.party.ReportXML
import org.apache.ofbiz.accounting.report.javaClass



list1 = [:]
groovyList=[ ]
groovyItem=[:]
list1=javaClass.getReportXML(request,response) 
list1.each{listItem->
    groovyItem=[:]
    groovyItem.AcctgTransId=listItem.AcctgTransId
    groovyItem.TransactionDate=listItem.TransactionDate
    groovyItem.Description=listItem.Description
    groovyItem.CR=listItem.CR
    groovyItem.DR=listItem.DR
    groovyItem.Balance=listItem.Balance

    groovyList.add(groovyItem)}
println list1
context.map3 = groovyList
//   getAt(1)
//    map12=[:]
//   listOfLists.each{i -> println "iiiiiiii "+i     
//       map12=[id : i.getAt(1)]
//       i.each{ii ->         
//        println "ii "+ii }
//   }
//  println "map12=[id : i.getAt(1)] "+map12
// list1 = []
// list1=ReportXML.getReportXML()
// list1.each{listItem -> println "listItem " +listItem
//}
// 
// map1 =[:]
// map2 =[:]
// map1 =ReportXML.getReportXML()
// map1.each{ key, value -> 
////       switch (key) {
////           case "ACCTGTRANSID" :
////           map2=["ACCTGTRANSID":value]
////           case "TRANSACTION_DATE" :
////           map2=["TRANSACTION_DATE":value]
////       }
////     if(key=="ACCTGTRANSID")
////     {println "true"
////     map2=["ACCTGTRANSID":value]}
////     else if(key=="TRANSACTION_DATE"){
////      println "false"
////      map2=["TRANSACTION_DATE":value]
////  }
//   
//  
//     println "key " + key
//          println "value " + value
////               println "map2 " + map2
////                    println "x " + x
//
//
//}
//map3=[]
//map3=map1
//context.map3=map3
//println "context.map3 "+context.map3

//map =[]
//map.add(map2)



//accountBalance=[listItemACCTGTRANSID:listItemACCTGTRANSID,listItemTRANSACTION_DATE: listItemTRANSACTION_DATE,listItemDR:listItemDR,listItemCR:listItemCR,listItembalance:listItembalance]

////import java.sql.Connection
////import java.sql.DriverManager
////import java.sql.ResultSet
////import java.sql.Statement
////import org.apache.ofbiz.entity.GenericValue
//
//
//import org.apache.ofbiz.entity.condition.EntityCondition
//import org.apache.ofbiz.entity.condition.EntityOperator
//import org.apache.ofbiz.party.party.PartyHelper
//
//println "testd"
//
////exprList2 = []
////exprList2.add(EntityCondition.makeCondition('invoiceId', EntityOperator.IN, '10000'))
////List a=select('description').from('Invoice').where(exprList2).queryList()
////
////println "List a" + a
//
//partyNameList = []
//parties.each { party ->
//    partyName = PartyHelper.getPartyName(party)
//    partyNameList.add(partyName)
//}
////................
////myList = []
////myList.add(resultSql2)
////    
////println "myList "+myList
//// balance=([ACCTGTRANSID : xx.ACCTGTRANSID,TRANSACTION_DATE: xx.TRANSACTION_DATE] )
////myList.each{ xx -> 
////    balance = [:]}
////println "balance.ACCTGTRANSID "+balance.ACCTGTRANSID
////println "balance.TRANSACTION_DATE "+balance.TRANSACTION_DATE
////context.balance = balance
////println "context.balance "+context.balance 
//////................
//
//context.partyNameList = partyNameList
//println "partyNameList "+partyNameList
//if ("6015") {
//    customTimePeriod = from("CustomTimePeriod").where("customTimePeriodId", '6015').cache(true).queryOne()
//    customTimePeriod12 = from("Vendor").where("partyId", '10000').cache(true).queryOne()
//    println "parameters.customTimePeriodId "+ customTimePeriod
//    println "parameters.customTimePeriodId 12"+ customTimePeriod12
//    exprList = []
//    exprList.add(EntityCondition.makeCondition('organizationPartyId', EntityOperator.IN, partyIds))
//    exprList.add(EntityCondition.makeCondition('fromDate', EntityOperator.LESS_THAN, customTimePeriod.getTimestamp('thruDate')))
//    exprList.add(EntityCondition.makeCondition(EntityCondition.makeCondition('thruDate', EntityOperator.GREATER_THAN_EQUAL_TO, customTimePeriod.getTimestamp('fromDate')), EntityOperator.OR, EntityCondition.makeCondition('thruDate', EntityOperator.EQUALS, null)))
//    List organizationGlAccounts = from("GlAccountOrganizationAndClass").where(exprList).orderBy("accountCode").queryList()
//
//   
//   
//    accountBalances = []
//    postedDebitsTotal = 0
//    postedCreditsTotal = 0
//    organizationGlAccounts.each { organizationGlAccount ->
//        accountBalance = [:]
//        accountBalance = runService('computeGlAccountBalanceForTimePeriod', [organizationPartyId: organizationGlAccount.organizationPartyId, customTimePeriodId: customTimePeriod.customTimePeriodId, glAccountId: organizationGlAccount.glAccountId])
//        if (accountBalance.postedDebits != 0 || accountBalance.postedCredits != 0) {
//            accountBalance.glAccountId = organizationGlAccount.glAccountId
//            accountBalance.accountCode = organizationGlAccount.accountCode
//            accountBalance.accountName = organizationGlAccount.accountName
//            postedDebitsTotal = postedDebitsTotal + accountBalance.postedDebits
//            postedCreditsTotal = postedCreditsTotal + accountBalance.postedCredits
//            accountBalances.add(accountBalance)
//        }
//    }
//    context.postedDebitsTotal = postedDebitsTotal
//    context.postedCreditsTotal = postedCreditsTotal
//    context.accountBalances = accountBalances
//}
