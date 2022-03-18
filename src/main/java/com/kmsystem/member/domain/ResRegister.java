package com.kmsystem.member.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
public class ResRegister {

    private Integer memberId;

    private String memId;

    private String memPw;

    private String memName;

    private List<ResgisterAuth> authList;
    
}
