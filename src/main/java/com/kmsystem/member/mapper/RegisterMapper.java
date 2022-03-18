package com.kmsystem.member.mapper;

import com.kmsystem.member.domain.Member;
import com.kmsystem.member.domain.ReqRegister;
import com.kmsystem.member.domain.ResRegister;
import com.kmsystem.member.domain.ResgisterAuth;

public interface RegisterMapper {
    
    public void createRegister(ReqRegister reqRegister)throws Exception;

    public void createRegisterAuth(ResgisterAuth resgisterAuth)throws Exception;

    public ResRegister readRegister(String memId);

    public Member readMember(Integer memberId)throws Exception;

}
