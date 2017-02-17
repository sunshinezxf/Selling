package selling.sunshine.model;

import common.sunshine.model.Entity;

/**
 * 日志记录类
 * @author wang_min
 *
 */
public class BackOperationLog extends Entity {
	private String logId;//日志ID
	private String adminInfo;//管理员信息，一般是管理员ID
	private String operationEvent;//操作事件
	private String ip;//IP
	
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
