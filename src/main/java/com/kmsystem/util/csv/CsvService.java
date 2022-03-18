package com.kmsystem.util.csv;

import com.kmsystem.config.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CsvService {

    @Autowired
    private Properties Properties;

    public void writeCSV(List<String[]> dataList) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null; // 출력 스트림 생성
        try {
            fos = new FileOutputStream(Properties.getUploadPath()+"/csv/send.csv");
            osw = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
            bw = new BufferedWriter(osw);
            // csv파일의 기존 값에 이어쓰려면 위처럼 true를 지정하고, 기존 값을 덮어쓰려면 true를 삭제한다

            for (int i = 0; i < dataList.size(); i++) {
                String[] data = dataList.get(i);
                String aData = "";
                aData = data[0] + "," +"\"" +data[1] +"\"";
                // 한 줄에 넣을 각 데이터 사이에 ,를 넣는다
                bw.write(aData);
                // 작성한 데이터를 파일에 넣는다
                bw.newLine(); // 개행
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bw != null) {try { bw.flush(); bw.close(); } catch (IOException e) { e.printStackTrace(); }}
            if(osw != null) {try { osw.close(); } catch (IOException e) { e.printStackTrace(); }}
            if(fos != null) {try { fos.close(); } catch (IOException e) { e.printStackTrace(); }}
//                if (bw != null) {
//                    bw.flush(); // 남아있는 데이터까지 보내 준다
//                    bw.close(); // 사용한 BufferedWriter를 닫아 준다
//                }
        }
    }

}
