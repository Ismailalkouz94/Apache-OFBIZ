package org.apache.ofbiz.humanres.employee;
import java.math.BigDecimal;
import java.sql.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rbab3a
 */
public class EmployeePosition {
private String partyId;
private String emplSalaryGradeId;
    private String gradId;
    private String degreeId;
    private String gradeId;
    private String jobGroupId;
    private String partyIdFrom;
    private String vacationId;
    private BigDecimal basicSalary;
    private java.sql.Date TransDate;
    private String TransType;
    private java.sql.Date endDate;
    private String emplPositionId;
    private String notes;
    private String userLoginId;

    public String getEmplSalaryGradeId() {
        return emplSalaryGradeId;
    }

    public void setEmplSalaryGradeId(String emplSalaryGradeId) {
        this.emplSalaryGradeId = emplSalaryGradeId;
    }

    
    
    
    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    
    public String getGradId() {
        return gradId;
    }

    public void setGradId(String gradId) {
        this.gradId = gradId;
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

    public String getPartyIdFrom() {
        return partyIdFrom;
    }

    public void setPartyIdFrom(String partyIdFrom) {
        this.partyIdFrom = partyIdFrom;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Date getTransDate() {
        return TransDate;
    }

    public void setTransDate(Date TransDate) {
        this.TransDate = TransDate;
    }

    public String getTransType() {
        return TransType;
    }

    public void setTransType(String TransType) {
        this.TransType = TransType;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEmplPositionId() {
        return emplPositionId;
    }

    public void setEmplPositionId(String emplPositionId) {
        this.emplPositionId = emplPositionId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getVacationId() {
        return vacationId;
    }

    public void setVacationId(String vacationId) {
        this.vacationId = vacationId;
    }

    
}
