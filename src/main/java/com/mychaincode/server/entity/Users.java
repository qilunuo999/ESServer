package com.mychaincode.server.entity;

import lombok.Data;

@Data
public class Users {
    private int id;
    private String username;
    private String email;
    private String passwd;
    private String cellphone;
    private String department;
    private String power;
    private String isverify;
    private String state;
}
