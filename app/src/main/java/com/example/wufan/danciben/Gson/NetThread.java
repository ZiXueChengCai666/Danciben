package com.example.wufan.danciben.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Admin on 2017/9/11.
 *
 */

public class NetThread extends Thread {
    private String word;
    private final String key="cycycd123";
    private String finalurl;
    public void setWord(String w)
    {
        this.word=w;
    }
    public NetThread(String u)
    {
        this.finalurl=u;
    }
    private void methodPost()
    {
        String content="word="+word+"&key="+key;
        try {
            URL httpurl=new URL(finalurl+"?"+content);
            HttpURLConnection conn=(HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(3000);
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb=new StringBuffer();
            String str;
            while((str=reader.readLine())!=null)
            {
                sb.append(str);
            }
            Glo.getres=sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void run()
    {
        methodPost();
    }
}
