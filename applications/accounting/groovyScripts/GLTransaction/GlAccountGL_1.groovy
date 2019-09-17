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
import org.apache.ofbiz.entity.condition.EntityOperator
import org.apache.ofbiz.entity.util.EntityUtilProperties
import org.apache.ofbiz.party.party.PartyHelper
 

List accountClassIds =["EXPENSE","CASH_EXPENSE","COGS_EXPENSE","INTEREST_EXPENSE","SGA_EXPENSE","NON_CASH_EXPENSE","AMORTIZATION","DEPRECIATION","INVENTORY_ADJUST"]


List accountTypeIds =["ACCOUNTS_PAYABLE" ,"ACCOUNTS_RECEIVABLE","AP_EMPLOYEES"]
List glAccountIds = select("glAccountId").from("GlAccountTypeDefault").where(EntityCondition.makeCondition("glAccountTypeId", EntityOperator.IN, accountTypeIds)).cache(true).queryList()

List exprList = []
exprList = [EntityCondition.makeCondition("glAccountClassId", EntityOperator.NOT_IN, accountClassIds),
            EntityCondition.makeCondition("glAccountId", EntityOperator.NOT_IN, glAccountIds.glAccountId)]
context.andCondition = EntityCondition.makeCondition(exprList , EntityOperator.AND)


println "GlAccountGL_1.groovy"
println "**  "+context.andCondition