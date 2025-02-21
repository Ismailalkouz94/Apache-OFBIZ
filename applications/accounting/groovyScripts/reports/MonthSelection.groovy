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

import org.apache.ofbiz.base.util.UtilDateTime
import org.apache.ofbiz.service.calendar.ExpressionUiHelper

if(parameters.selectedMonth==null){
    parameters.selectedMonth='0'
}

if (parameters.selectedMonth) {
    selectedMonth = Integer.valueOf(parameters.selectedMonth)
    selectedMonthDate = UtilDateTime.toTimestamp((selectedMonth + 1), 1, UtilDateTime.getYear(UtilDateTime.nowTimestamp(), timeZone, locale), 0, 0, 0)
    //  context.fromDate = UtilDateTime.getMonthStart(selectedMonthDate, timeZone, locale)
    context.fromDate1 = UtilDateTime.getMonthStart(selectedMonthDate, timeZone, locale) 
    context.thruDate1 = UtilDateTime.getMonthEnd(selectedMonthDate, timeZone, locale)
    println "selectedMonthDate "+selectedMonthDate
}
context.monthList = ExpressionUiHelper.getMonthValueList(locale)


println "monthList "+context.monthList