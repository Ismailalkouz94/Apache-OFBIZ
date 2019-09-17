/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.ofbiz.common.activitiServices;

/**
 *
 * @author ASUS
 */
public class Task {

    private String id;
    private String url;
    private String assignee;
    private String name;
    private String createTime;
    private int priority;
    private boolean suspended;
  private String total;
    private String taskDefinitionKey;
    private String executionId;
    private String tenantId;
    private String executionUrl;
    private String processInstanceId;
    private String processInstanceUrl;
    private String processDefinitionId;
    private String processDefinitionUrl;
private String description;
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the assignee
     */
    public String getAssignee() {
        return assignee;
    }

    /**
     * @param assignee the assignee to set
     */
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * @return the suspended
     */
    public boolean isSuspended() {
        return suspended;
    }

    /**
     * @param suspended the suspended to set
     */
    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    /**
     * @return the taskDefinitionKey
     */
    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    /**
     * @param taskDefinitionKey the taskDefinitionKey to set
     */
    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    /**
     * @return the executionId
     */
    public String getExecutionId() {
        return executionId;
    }

    /**
     * @param executionId the executionId to set
     */
    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    /**
     * @return the tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * @param tenantId the tenantId to set
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return the executionUrl
     */
    public String getExecutionUrl() {
        return executionUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param executionUrl the executionUrl to set
     */
    public void setExecutionUrl(String executionUrl) {
        this.executionUrl = executionUrl;
    }

    /**
     * @return the processInstanceId
     */
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    /**
     * @param processInstanceId the processInstanceId to set
     */
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    /**
     * @return the processInstanceUrl
     */
    public String getProcessInstanceUrl() {
        return processInstanceUrl;
    }

    /**
     * @param processInstanceUrl the processInstanceUrl to set
     */
    public void setProcessInstanceUrl(String processInstanceUrl) {
        this.processInstanceUrl = processInstanceUrl;
    }

    /**
     * @return the processDefinitionId
     */
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    /**
     * @param processDefinitionId the processDefinitionId to set
     */
    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    /**
     * @return the processDefinitionUrl
     */
    public String getProcessDefinitionUrl() {
        return processDefinitionUrl;
    }

    /**
     * @param processDefinitionUrl the processDefinitionUrl to set
     */
    public void setProcessDefinitionUrl(String processDefinitionUrl) {
        this.processDefinitionUrl = processDefinitionUrl;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "{" + "id=" + id + ", url=" + url +", total=" + total + ", assignee=" + assignee + ", name=" + name + ", createTime=" + createTime + ", priority=" + priority + ", suspended=" + suspended + ", taskDefinitionKey=" + taskDefinitionKey + ", executionId=" + executionId + ", tenantId=" + tenantId + ", executionUrl=" + executionUrl + ", processInstanceId=" + processInstanceId + ", processInstanceUrl=" + processInstanceUrl + ", processDefinitionId=" + processDefinitionId + ", processDefinitionUrl=" + processDefinitionUrl + '}';
    }

}
