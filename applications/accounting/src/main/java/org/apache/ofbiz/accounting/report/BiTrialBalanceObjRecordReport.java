/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.accounting.report;

/**
 *
 * @author DELL
 */
public class BiTrialBalanceObjRecordReport {
    private String GL_ACCOUNT_ID,ACCOUNT_NAME,DR,CR,BALANCE;

    public String getGL_ACCOUNT_ID() {
        return GL_ACCOUNT_ID;
    }

    public void setGL_ACCOUNT_ID(String GL_ACCOUNT_ID) {
        this.GL_ACCOUNT_ID = GL_ACCOUNT_ID;
    }

    public String getACCOUNT_NAME() {
        return ACCOUNT_NAME;
    }

    public void setACCOUNT_NAME(String ACCOUNT_NAME) {
        this.ACCOUNT_NAME = ACCOUNT_NAME;
    }

    public String getDR() {
        return DR;
    }

    public void setDR(String DR) {
        this.DR = DR;
    }

    public String getCR() {
        return CR;
    }

    public void setCR(String CR) {
        this.CR = CR;
    }

    public String getBALANCE() {
        return BALANCE;
    }

    public void setBALANCE(String BALANCE) {
        this.BALANCE = BALANCE;
    }
}
