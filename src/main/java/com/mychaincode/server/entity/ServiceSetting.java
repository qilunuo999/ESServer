package com.mychaincode.server.entity;

import lombok.Data;

@Data
public class ServiceSetting {
    private int id;
    private String scriptid;
    private String scriptkey;
    private String area;
    private String channelname;
    private String chaincode;
    private String clusterid;
}
