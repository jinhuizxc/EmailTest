package com.example.emailtest.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户bean
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -4643698637217489886L;
    private int id;
    private String username;  // 用户名
    private String pwd;
    private String ismain;   // 主账户为1， 非主账户为0



}
