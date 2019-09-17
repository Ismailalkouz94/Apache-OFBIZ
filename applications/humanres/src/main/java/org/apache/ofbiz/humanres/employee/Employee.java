/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rbab3a
 */
package org.apache.ofbiz.humanres.employee;

public class Employee {

    private double amount;
    private String amountType;
    private String type;
    private String degreeId;
    private String gradeId;
    private String jobGroupId;
    private String organizationPartyId;
    private String allowenceId;
    private String positionId;
    private String glAccountId;
    private Long month;
    private Long year;
    private Long nomDay;
    private char isCompany;
    private char isPosted;
    private String relatedType;

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(String degreeId) {
        this.degreeId = degreeId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getJobGroupId() {
        return jobGroupId;
    }

    public void setJobGroupId(String jobGroupId) {
        this.jobGroupId = jobGroupId;
    }

    public String getOrganizationPartyId() {
        return organizationPartyId;
    }

    public void setOrganizationPartyId(String organizationPartyId) {
        this.organizationPartyId = organizationPartyId;
    }

    public String getAllowenceId() {
        return allowenceId;
    }

    public void setAllowenceId(String allowenceId) {
        this.allowenceId = allowenceId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(String glAccountId) {
        this.glAccountId = glAccountId;
    }

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getNomDay() {
        return nomDay;
    }

    public void setNomDay(Long nomDay) {
        this.nomDay = nomDay;
    }

    public char getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(char isCompany) {
        this.isCompany = isCompany;
    }

    public char getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(char isPosted) {
        this.isPosted = isPosted;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }

    
}
