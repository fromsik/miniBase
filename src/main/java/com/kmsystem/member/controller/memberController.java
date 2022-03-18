package com.kmsystem.member.controller;

import com.kmsystem.common.security.domain.CustomUser;
import com.kmsystem.member.domain.Member;
import com.kmsystem.member.domain.ReqRegister;
import com.kmsystem.member.service.RegisterService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class memberController {

    private final PasswordEncoder passwordEncoder;
    private final RegisterService registerService;

    @ApiOperation("회원등록")
    @PostMapping
    @ApiIgnore
    public ResponseEntity<Void> create(@RequestBody ReqRegister reqRegister) throws Exception {

        log.info("register Id : " + reqRegister.getMemId());

        String password = reqRegister.getMemPw();
        reqRegister.setMemPw(passwordEncoder.encode(password));

        registerService.create(reqRegister);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("내정보")
    @GetMapping("/myinfo")
    public ResponseEntity<Member> readMyinfo (@AuthenticationPrincipal CustomUser customUser) throws Exception {
        Member member = registerService.readMember(customUser.getMemberId());
        member.setMemPw(null);
        return new ResponseEntity<>(member,HttpStatus.OK);
    }
}
