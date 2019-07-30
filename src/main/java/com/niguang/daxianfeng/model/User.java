package com.niguang.daxianfeng.model;

import lombok.Data;

@Data
public class User {
    private Long UserId;
    private Long wxID;
    private int level;
    private int curExp;
}
