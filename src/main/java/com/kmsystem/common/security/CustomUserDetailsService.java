package com.kmsystem.common.security;

import com.kmsystem.common.security.domain.CustomUser;
import com.kmsystem.member.domain.ResRegister;
import com.kmsystem.member.mapper.RegisterMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private RegisterMapper registerMapper;

    //사용자상세조회
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("=== LOGIN ID : " + userName);

        ResRegister resRegister = registerMapper.readRegister(userName);

        return resRegister == null ? null : new CustomUser(resRegister);
    }
}
