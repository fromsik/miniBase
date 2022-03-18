package com.kmsystem.util.cmd;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ShellService {

    public List<String> shellCmd(String cmd)throws Exception{
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(cmd);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String line;
        List<String> result = new ArrayList<>();
        while((line = br.readLine()) != null) {
            result.add(line);
        }

        process.waitFor();
        process.destroy();
        System.out.println("cmdResult : " + result);

        return result;
    }
}
