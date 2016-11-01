package com.easspider.test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by Raymond on 2016/11/1.
 */
public class MUCSpiderTest {
    public static void main(String[] args) {
        String url="http://www.muc.edu.cn/";
        HttpGet httpGet=new HttpGet(url);
        CloseableHttpClient client = HttpClients.custom().build();
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            Document document = Jsoup.parse(EntityUtils.toString(
                    response.getEntity(), "utf-8"));
            Element loginform = document.getElementById("loginform");
            System.out.println(loginform.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
