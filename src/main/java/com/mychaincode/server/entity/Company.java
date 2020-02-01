package com.mychaincode.server.entity;

import lombok.Data;

@Data
public class Company {
    private int id;
    private String email;
    private String company;
    private String creditcode;
    private String verifyinfo;
    private String license;
    private String state;
}
