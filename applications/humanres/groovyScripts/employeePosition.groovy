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
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.condition.EntityCondition
import org.apache.ofbiz.entity.condition.EntityOperator

println "*****" +parameters.emplPositionId

GenericValue emplPositionFulfillment = from("EmplPositionFulfillment").where("emplPositionId", parameters.emplPositionId).cache(true).queryOne()
println  emplPositionFulfillment
context.emplPositionFulfillment = emplPositionFulfillment
//List condition = []
//condition.add(EntityCondition.makeCondition("emplPositionId", EntityOperator.EQUALS, parameters.emplPositionId))
//List emplPositionFulfillment = select("emplPositionId").from("EmplPositionFulfillment").where(condition).queryList()
//println emplPositionFulfillment


//context.employmentDataGroovy= result



