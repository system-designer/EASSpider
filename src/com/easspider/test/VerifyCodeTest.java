package com.easspider.test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by Raymond on 2017/3/24.
 */
public class VerifyCodeTest {
    public final static String IMAGE_ROOT_PATH="G:\\data\\VefiryCode\\";
    /**
     * 得到验证码
     *
     * @return
     * @throws IOException
     */
    public static void getCheckCode(int imageId){
        HttpGet get = new HttpGet("http://www.jwgl.hbnu.edu.cn/CheckCode.aspx");
        CloseableHttpClient client = HttpClients.custom().build();
        //ImageIcon icon =null;
        try {
            CloseableHttpResponse response = client.execute(get);
            byte[] bytes = EntityUtils.toByteArray(response.getEntity());
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
            ImageIO.write(img,"png",new File(IMAGE_ROOT_PATH+imageId+".png"));
            //icon = new ImageIcon(bytes);
            response.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
        }
        /*
        String checkCode = (String) JOptionPane.showInputDialog(null, "",
                "输入验证码", JOptionPane.INFORMATION_MESSAGE, icon, null, null);
        System.out.println(checkCode);
        */
    }

    public static void main(String[] args) {
        for(int i=0;i<5;i++){
            getCheckCode(i);
        }
    }
}
