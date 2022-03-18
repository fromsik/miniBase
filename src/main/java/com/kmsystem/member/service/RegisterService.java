package com.kmsystem.member.service;

import com.kmsystem.member.domain.Member;
import com.kmsystem.member.domain.ReqRegister;
import com.kmsystem.member.domain.ResgisterAuth;
import com.kmsystem.member.mapper.RegisterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@RequiredArgsConstructor
@Service
public class RegisterService {

    private final RegisterMapper registerMapper;

    @Transactional
	public void create(ReqRegister reqRegister) throws Exception {

    	registerMapper.createRegister(reqRegister);

		ResgisterAuth resgisterAuth = new ResgisterAuth();
		resgisterAuth.setMemberId(reqRegister.getMemberId());
		resgisterAuth.setAuth("ROLE_ADMIN");
		registerMapper.createRegisterAuth(resgisterAuth);
	}

	public Member readMember(Integer memberId)throws Exception {
    	return registerMapper.readMember(memberId);
	}

}
