package com.easspider.test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

/**
 * Created by Raymond on 2017/3/24.
 */
public class VerifyCodeTest {
    public final static String IMAGE_ROOT_PATH="G:\\data\\VefiryCode\\";
    public final static int IMAGE_COUNT=10;

    /**
     * 得到验证码
     *
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

    /**
     * 获得验证码图片和信息
     *
     * @return 图片流和图片内容
     */
    public static void createImage() {
        // 在内存中创建图象
        int width = 60, height = 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        // 设定背景色
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);

        // 画边框
        g.setColor(Color.red);
        g.drawRect(0, 0, width - 1, height - 1);

        // 取随机产生的认证码(4位数字)
        String rand = Math.random() + "";
        rand = rand.substring(rand.indexOf(".") + 1);

        switch (rand.length()) {
            case 1:
                rand = "000" + rand;
                break;
            case 2:
                rand = "00" + rand;
                break;
            case 3:
                rand = "0" + rand;
                break;
            default:
                rand = rand.substring(0, 4);
                break;
        }
        // 将认证码显示到图象中
        g.setColor(Color.gray);
        String numberStr = rand;
        g.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
        String Str = numberStr.substring(0, 1);
        g.drawString(Str, 8, 17);
        Str = numberStr.substring(1, 2);
        g.drawString(Str, 20, 15);
        Str = numberStr.substring(2, 3);
        g.drawString(Str, 35, 18);
        Str = numberStr.substring(3, 4);
        g.drawString(Str, 45, 15);
        // 随机产生88个干扰点，使图象中的认证码不易被其它程序探测到
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // 图象生效
        g.dispose();
        try {
            ImageIO.write(image, "png", new File(IMAGE_ROOT_PATH + rand + ".png"));
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {

        }
    }

    public static void main(String[] args) {
        for(int i=0;i<IMAGE_COUNT;i++){
            createImage();
        }
        /*
        for(int i=0;i<IMAGE_COUNT;i++){
            getCheckCode(i);
        }
        */
    }
}
