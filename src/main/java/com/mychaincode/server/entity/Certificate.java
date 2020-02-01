package com.mychaincode.server.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Certificate {
    private String id;
    private String certificate;
    private String filetype;
    private Date starttime;
    private Date endtime;
}
