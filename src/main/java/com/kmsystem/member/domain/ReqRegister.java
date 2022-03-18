package com.kmsystem.member.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReqRegister {

    private Integer memberId;

    private String memId;

    private String memPw;
    
    private String memName;
    
}
