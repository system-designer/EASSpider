package com.easspider.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ImageDownloader {
	private static CloseableHttpClient client = HttpClients.custom().build();
	// 下载地址
	private final static String downloadPath = "http://www.jwgl.hbnu.edu.cn/%28eugihcn12lwnmoz0jslklv45%29/CheckCode.aspx";

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			download(downloadPath, "F:/verification_code/" + i + ".jpg");
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param downloadPath
	 *            下载URL
	 * @param fileSavedPath
	 *            保存地址
	 */
	public static void download(String downloadPath, String fileSavedPath) {
		HttpGet get = new HttpGet(downloadPath);
		try {
			CloseableHttpResponse response = client.execute(get);
			FileUtils.writeByteArrayToFile(new File(fileSavedPath),
					EntityUtils.toByteArray(response.getEntity()));
			response.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
