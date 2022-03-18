package com.kmsystem.member.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResgisterAuth {
	
    private Integer memberId;
    private String auth;
}

