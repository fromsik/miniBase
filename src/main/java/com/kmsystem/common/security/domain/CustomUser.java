package com.kmsystem.common.security.domain;


import com.kmsystem.member.domain.ResRegister;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.stream.Collectors;


public class CustomUser extends User {

    private static final long serialVersionUID = 1L;

    //login 정보 domain
    private ResRegister resRegister;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    //domain에서 id,pw 가져오기
    public CustomUser(ResRegister resRegister) {
        super(resRegister.getMemId(), resRegister.getMemPw(), resRegister.getAuthList().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()));

        this.resRegister = resRegister;
    }

    public CustomUser(ResRegister resRegister, Collection<? extends GrantedAuthority> authorities) {
        super(resRegister.getMemId(), resRegister.getMemPw(), authorities);

        this.resRegister = resRegister;
    }

    public Integer getMemberId() {
        return resRegister.getMemberId();
    }

    public String getMemId() {
        return resRegister.getMemId();
    }
}
