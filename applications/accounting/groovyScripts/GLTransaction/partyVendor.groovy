/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */



import org.apache.ofbiz.base.util.Debug
import org.apache.ofbiz.entity.condition.EntityCondition
import org.apache.ofbiz.entity.condition.EntityFunction
import org.apache.ofbiz.entity.condition.EntityOperator
import org.apache.ofbiz.entity.util.EntityUtilProperties
import org.apache.ofbiz.party.party.PartyHelper
import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate

println "partyVendor.groovy"
println parameters.partyId
// c=0
//List partyIds =select("firstName", "lastName" ).from("PartyRolePersonVendor").queryList()
//
////println partyIds +"  vendor "+ listItem.manifestCompanyName
//partyIds.each{listItem->
//println " party "+listItem.firstName 
//c= c+1
//}
//println "c "+c 


List partyIdNotIns = select("partyId").from("Vendor").cache(true).queryList()
List Exprs = []
Exprs.add(EntityCondition.makeCondition("partyId", EntityOperator.NOT_IN, partyIdNotIns.partyId))
Exprs.add(EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "BILL_FROM_VENDOR"))
if(parameters.partyId != null && UtilValidate.isNotEmpty(parameters.partyId)){
        Exprs.add(EntityCondition.makeCondition("partyId", EntityOperator.LIKE, "%"+parameters.partyId+"%"))
}
if(parameters.firstName != null && UtilValidate.isNotEmpty(parameters.firstName)){
    Exprs.add(EntityCondition.makeCondition([EntityCondition.makeCondition("firstName", EntityOperator.LIKE, "%"+(parameters.firstName).toUpperCase()+"%"),
                                             EntityCondition.makeCondition("firstName", EntityOperator.LIKE, "%"+(parameters.firstName).toLowerCase()+"%")]
                                             , EntityOperator.OR))
}
if(parameters.lastName != null && UtilValidate.isNotEmpty(parameters.lastName)){
        Exprs.add(EntityCondition.makeCondition("lastName", EntityOperator.LIKE, "%"+parameters.lastName+"%"))
}

List Exprs2 = []
if(parameters.partyId != null && UtilValidate.isNotEmpty(parameters.partyId)){
        Exprs2.add(EntityCondition.makeCondition("partyId", EntityOperator.LIKE, "%"+parameters.partyId+"%"))
}
if(parameters.firstName != null && UtilValidate.isNotEmpty(parameters.firstName)){
     Exprs2.add(EntityCondition.makeCondition([EntityCondition.makeCondition("manifestCompanyName", EntityOperator.LIKE, "%"+(parameters.firstName).toUpperCase()+"%"),
                                             EntityCondition.makeCondition("manifestCompanyName", EntityOperator.LIKE, "%"+(parameters.firstName).toLowerCase()+"%")]
                                             , EntityOperator.OR))
}
if(parameters.lastName != null && UtilValidate.isNotEmpty(parameters.lastName)){
    Exprs2.add(EntityCondition.makeCondition("manifestCompanyTitle", EntityOperator.LIKE, "%"+parameters.lastName+"%"))
}
println Exprs
println Exprs2
List partyRolePersons = from("PartyRolePerson").where(Exprs).cache(true).queryList()


List partyIdIns = select("partyId","manifestCompanyName","manifestCompanyTitle").from("Vendor").where(Exprs2).queryList()




list1 = [:]
groovyList=[ ]
groovyItem=[:]
partyIdIns.each{listItem->
        groovyItem=[:]
        groovyItem.partyId=listItem.partyId
        groovyItem.firstName=listItem.manifestCompanyName
        groovyItem.lastName=listItem.manifestCompanyTitle
    groovyList.add(groovyItem)
}
partyRolePersons.each{listItem->
        groovyItem=[:]
        groovyItem.partyId=listItem.partyId
        groovyItem.firstName=listItem.firstName
        groovyItem.lastName=listItem.lastName
    groovyList.add(groovyItem)
}
//println groovyList


context.listIt = groovyList
