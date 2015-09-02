package com.easspider.spider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.easspider.bean.ClassInfo;
import com.easspider.constants.LoginConstants;
import com.easspider.dao.ClassInfoDao;

public class TimeTableHandler {
	private CloseableHttpClient client = LoginConstants
			.getCustomCloseableHttpClient();
	private String state;//登录页面表单中__VIEWSTATE值
	private String contentRef;//URL尾部的字符串
	private String searchUrl;//查询学生信息的URL

	/**
	 * 初始化课表查询
	 * 
	 * @param loginHandler 登录操作类
	 */
	public TimeTableHandler(LoginHandler loginHandler) {
		try {
			if (loginHandler.isLogined()) {
				contentRef = MessageFormat.format(LoginConstants.contentRef,
						LoginHandler.UrlCode);
				searchUrl = MessageFormat.format(LoginConstants.timeTableUrl,
						LoginHandler.UrlCode);
				HttpGet get = new HttpGet(searchUrl);
				get.setHeader("Referer", contentRef);
				CloseableHttpResponse response = client.execute(get);
				Document iframe = Jsoup.parse(EntityUtils.toString(
						response.getEntity(), "gb2312"));
				state = iframe
						.getElementsByAttributeValue("name", "__VIEWSTATE")
						.get(0).attr("value");
				response.close();
			}
		} catch (IOException e) {
		}
	}

	/**
	 * 设置学生课表
	 */
	public void setTimeTable() {
		ClassInfoDao classInfoDao = new ClassInfoDao();
		List<ClassInfo> classInfoList = classInfoDao.getList();
		if (classInfoList != null) {
			for (ClassInfo classInfo : classInfoList) {
				Elements contents = null;
				try {
					CloseableHttpResponse response = getSearchResponse(
							classInfo.getGrade(), classInfo.getDepartment(),
							classInfo.getMajor(), classInfo.getCode());
					Document iframe = Jsoup.parse(EntityUtils.toString(
							response.getEntity(), "gb2312"));
					contents = iframe.getElementsByClass("main_box");
				} catch (Exception e) {
					e.printStackTrace(System.out);
				}
				String timeTable = "";
				if (contents != null && contents.size() > 0) {
					timeTable = contents.get(0).html();
				}
				System.out.println(timeTable);
				classInfo.setTimeTable(timeTable);
				/*
				ClassInfoDao cd = new ClassInfoDao();
				cd.update(classInfo);
				*/
			}
		}
	}

	/**
	 * 得到课表的iframe响应
	 * @param nj 年级
	 * @param xy 学院
	 * @param zy 专业
	 * @param kb 
	 * @return
	 * @throws IOException
	 */
	private CloseableHttpResponse getSearchResponse(String nj, String xy,
			String zy, String kb) throws IOException {
		// nj=2014表示14级xy=01文学院,zy=0101汉语言文学,kb=201401012014-2015201011402
		HttpEntity entity = this.buildSearchForm("	2014-2015", "2", nj, xy, zy,
				kb);
		HttpPost post = new HttpPost(searchUrl);
		post.setHeader("Referer", searchUrl);
		post.setEntity(entity);
		CloseableHttpResponse response = client.execute(post);
		return response;
	}

	/**
	 * 构建课表查询表单
	 * @param schoolYear 学年
	 * @param schoolPeriod 学期
	 * @param grade 年级
	 * @param depName 院系名
	 * @param major 专业名
	 * @param className 班级名
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private HttpEntity buildSearchForm(String schoolYear, String schoolPeriod,
			String grade, String depName, String major, String className)
			throws UnsupportedEncodingException {
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		data.add(new BasicNameValuePair("__EVENTTARGET", "kb"));
		data.add(new BasicNameValuePair("__VIEWSTATE", state));
		data.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", "65E8E916"));
		data.add(new BasicNameValuePair("kb", className));
		data.add(new BasicNameValuePair("nj", grade));
		data.add(new BasicNameValuePair("xn", schoolYear));
		data.add(new BasicNameValuePair("xq", schoolPeriod));
		data.add(new BasicNameValuePair("xy", depName));
		data.add(new BasicNameValuePair("zy", major));
		HttpEntity entity = new UrlEncodedFormEntity(data, "gb2312");
		return entity;
	}
}
