package com.kmsystem.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.BinaryOperator;

@ApiIgnore
@RequiredArgsConstructor
@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<Void> test01(){

        int[] arr = {1,2,3,4,5};
        System.out.println(Arrays.stream(arr).reduce(0,(a,b)->a+b));

        String[] greetings = {"안녕하세요---","hello","good morning", "반갑습니다"};

        System.out.println(Arrays.stream(greetings).reduce("",(s1,s2) ->
        {
            if(s1.getBytes().length >= s2.getBytes().length)
                return s1;
            else return s2;
        }));

        System.out.println(Arrays.stream(greetings).reduce(new CompareString()).get());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

class CompareString implements BinaryOperator<String>{

    @Override
    public String apply(String s1, String s2) {
        if(s1.getBytes().length >= s2.getBytes().length)
            return s1;
        else return s2;
    }
}
