package com.orion.regression.bean;


public class FileTransferBean {
	String tokenId;
	String hostName;
	String connectionName;
	String actualConnectionName;
	String connectionId;
	String actualConnectionId;
	String jobName;
	boolean isJSONInMemory;
	String jsonDownloadPath;
	String sourceUploadPath;
	String downloadUrl;
	String jobId;
	String stepId;
	String actualStepId;
	String connParamsJsonStr;
	String runParamsJsonStr;

	public String getActualConnectionName() { return actualConnectionName; }

	public void setActualConnectionName(String actualConnectionName) { this.actualConnectionName = actualConnectionName; }

	public String getActualConnectionId() { return actualConnectionId; }

	public void setActualConnectionId(String actualConnectionId) { this.actualConnectionId = actualConnectionId; }

	public String getActualStepId() { return actualStepId; }

	public void setActualStepId(String actualStepId) { this.actualStepId = actualStepId; }

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getConnParamsJsonStr() {
		return connParamsJsonStr;
	}

	public void setConnParamsJsonStr(String connParamsJsonStr) {
		this.connParamsJsonStr = connParamsJsonStr;
	}

	public String getRunParamsJsonStr() {
		return runParamsJsonStr;
	}

	public void setRunParamsJsonStr(String runParamsJsonStr) {
		this.runParamsJsonStr = runParamsJsonStr;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectorName) {
		this.connectionName = connectorName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJsonDownloadPath() {
		return jsonDownloadPath;
	}

	public void setJsonDownloadPath(String jsonDownloadPath) {
		this.jsonDownloadPath = jsonDownloadPath;
	}
	
	public String getSourceUploadPath() {
		return sourceUploadPath;
	}

	public void setSourceUploadPath(String sourceUploadPath) {
		this.sourceUploadPath = sourceUploadPath;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public boolean isJSONInMemory() {
		return isJSONInMemory;
	}

	public void setJSONInMemory(boolean isJSONInMemory) {
		this.isJSONInMemory = isJSONInMemory;
	}
}
