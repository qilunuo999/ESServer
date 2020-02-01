package com.mychaincode.server.entity;

import lombok.Data;

@Data
public class Sealtable {
    private int id;
    private String sealid;
    private String sealname;
    private int uid;
    private String certificateid;
    private String sealtype;
    private String info;
}
