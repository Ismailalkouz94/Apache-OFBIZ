/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.accounting.report;

import java.util.Date;

/**
 *
 * @author DELL
 */
public class ObjRecordCostCentersReport {

    public String getPartyId() {
        return PartyId;
    }

    public void setPartyId(String PartyId) {
        this.PartyId = PartyId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public String getCR() {
        return CR;
    }

    public void setCR(String CR) {
        this.CR = CR;
    }

    public String getDR() {
        return DR;
    }

    public void setDR(String DR) {
        this.DR = DR;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String Balance) {
        this.Balance = Balance;
    }
    private String PartyId,GroupName,CR,DR,Balance;
 
    
}
