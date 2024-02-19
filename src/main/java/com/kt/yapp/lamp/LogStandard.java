package com.kt.yapp.lamp;

import lombok.Data;

@Data
public class LogStandard {
	private String timestamp;
	private String service;
	private String operation;
	private String transactionId;
	private String logType;
	private String url;
	private String serviceDomain; //20220627 
	
	private LOGSTANDARDHost host;
	private LOGSTANDARDResponse response;
	private LOGSTANDARDUser user;
	private LOGSTANDARDSecurity security;
}
