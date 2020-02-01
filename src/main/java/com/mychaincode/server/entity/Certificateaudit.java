package com.mychaincode.server.entity;

import lombok.Data;

@Data
public class Certificateaudit {
    private int id;
    private String certificate;
    private int uid;
    private int cid;
    private String filetype;
    private String state;
}
