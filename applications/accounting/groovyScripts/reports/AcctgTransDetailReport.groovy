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

import org.apache.ofbiz.entity.condition.EntityCondition
import org.apache.ofbiz.entity.condition.EntityOperator
import org.apache.ofbiz.party.party.PartyHelper

//println "ahmad12345"
println acctgTransEntries

debitsTotal = 0
creditsTotal = 0
acctgTransEntries.each { acctgTransEntrie ->
   if ("D".equals(acctgTransEntrie.debitCreditFlag) ) {
       debitsTotal = debitsTotal + acctgTransEntrie.amount
   }
   if ("C".equals(acctgTransEntrie.debitCreditFlag) ) {
       creditsTotal = creditsTotal + acctgTransEntrie.amount
   }
}
println "debitsTotal : " +debitsTotal
println "creditsTotal : " +creditsTotal

context.debitsTotal=debitsTotal
context.creditsTotal=creditsTotal