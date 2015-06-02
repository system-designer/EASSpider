package com.jplus.spider;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jplus.bean.ClassInfo;
import com.jplus.bean.Department;
import com.jplus.bean.Grade;
import com.jplus.bean.Major;
import com.jplus.bean.TimeTable;
import com.jplus.dao.ClassInfoDao;
import com.jplus.dao.DepartmentDao;
import com.jplus.dao.GradeDao;
import com.jplus.dao.MajorDao;
import com.jplus.dao.TimeTableDao;

public class TimeTableHandler {
	private CloseableHttpClient client = LoginConstants
			.getCustomCloseableHttpClient();
	private String state;
	private String contentRef;
	private String searchUrl;
	private String departName = "";

	/**
	 * 登录拿到token
	 * 
	 * @param login
	 */
	public TimeTableHandler(LoginPage login) {
		try {
			if (login.hasLogin()) {
				contentRef = MessageFormat.format(LoginConstants.contentRef,
						LoginPage.UrlCode);
				searchUrl = MessageFormat.format(LoginConstants.timeTableUrl,
						LoginPage.UrlCode);
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
	 * 通过院系名称查询到该院系所有学生
	 * 
	 * @return 学生列表
	 */
	public List<TimeTable> searchTimeTable() {
		List<TimeTable> timeTableList = null;

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
				classInfo.setTimeTable(timeTable);
				ClassInfoDao cd = new ClassInfoDao();
				cd.update(classInfo);
			}
		}
		return null;
	}

	/**
	 * GradeDao gd = new GradeDao(); List<Grade> gradeList = gd.getList();
	 * DepartmentDao dd = new DepartmentDao(); List<Department> departmentList =
	 * dd.getList(); MajorDao md = new MajorDao(); List<Major> majorList =
	 * md.getList(); for (Grade grade : gradeList) { if
	 * (grade.getCode().equals("2012")) { for (Department department :
	 * departmentList) { for (Major major : majorList) { if
	 * (major.getDepartment().equals( department.getCode())) {
	 * CloseableHttpResponse response = getSearchResponse( grade.getCode(),
	 * department.getCode(), major.getCode()); Document iframe =
	 * Jsoup.parse(EntityUtils .toString(response.getEntity(), "gb2312"));
	 * Elements elements = iframe .getElementsByTag("select"); if (elements !=
	 * null) { for (Element element : elements) { if
	 * (element.attr("name").toString() .equals("kb")) { List<ClassInfo>
	 * classInfoList = new ArrayList<ClassInfo>(); Elements options = element
	 * .getElementsByTag("option"); for (Element option : options) { if
	 * (!option.text().equals("")) { ClassInfo classInfo = new ClassInfo();
	 * classInfo.setName(option .text().trim()); classInfo.setCode(option
	 * .attr("value") .trim()); classInfo.setGrade(grade .getCode()); classInfo
	 * .setDepartment(department .getCode()); classInfo.setMajor(major
	 * .getCode()); // Elements contents = // iframe //
	 * .getElementsByClass("main_box"); // String timeTable = ""; // if
	 * (contents != null) { // timeTable = // contents.get(0) // .html(); // }
	 * // classInfo // .setTimeTable(timeTable); classInfoList .add(classInfo);
	 * } } ClassInfoDao cd = new ClassInfoDao(); cd.insert(classInfoList); } } }
	 * System.out.println(iframe.toString()); response.close(); } } } }
	 * 
	 * } return null;
	 */

	/**
	 * 得到iframe响应
	 * 
	 * @return response响应
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
	 * 构建查询表单
	 * 
	 * @param state
	 *            asp.net __VIEWSTATE值
	 * @return HttpEntity http 实体
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
