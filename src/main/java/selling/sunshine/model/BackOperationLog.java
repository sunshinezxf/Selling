package selling.sunshine.model;

import common.sunshine.model.Entity;

public class BackOperationLog extends Entity {
	private String logId;
	private String adminInfo;
	private String operationEvent;
	private String ip;
	
	public BackOperationLog(){
		super();
	}
	
	public BackOperationLog(String adminInfo, String ip, String operationEvent){
		this();
		this.adminInfo = adminInfo;
		this.ip = ip;
		this.operationEvent = operationEvent;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getAdminInfo() {
		return adminInfo;
	}

	public void setAdminInfo(String adminInfo) {
		this.adminInfo = adminInfo;
	}

	public String getOperationEvent() {
		return operationEvent;
	}

	public void setOperationEvent(String operationEvent) {
		this.operationEvent = operationEvent;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
