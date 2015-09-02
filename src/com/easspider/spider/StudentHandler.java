package com.easspider.spider;

import com.easspider.bean.Student;
import com.easspider.constants.LoginConstants;
import com.easspider.dao.StudentDao;

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

public class StudentHandler {

	private CloseableHttpClient client = LoginConstants
			.getCustomCloseableHttpClient();
	private String state;// 登录页面表单中__VIEWSTATE值
	private String contentRef;// URL尾部的字符串
	private String searchUrl;// 查询学生信息的URL
	private String departName = "";// 院系名
	private String imgSavedPath = "F:/StudentPicture/";// 照片存放路径

	/**
	 * 初始化查询，为contentRef，searchUrl，state赋值
	 * 
	 * @param loginHandler
	 *            登录操作
	 */
	public StudentHandler(LoginHandler loginHandler) {
		try {
			if (loginHandler.isLogined()) {
				contentRef = MessageFormat.format(LoginConstants.contentRef,
						LoginHandler.UrlCode);
				searchUrl = MessageFormat.format(LoginConstants.searchUrl,
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
	 * 得到学生的学号和姓名列表
	 * 
	 * @param departName
	 *            院系名称
	 * @return 学生列表
	 */
	public List<Student> getStudentList(String departName) {
		this.departName = departName;
		List<Student> students = null;
		try {
			// 通过学生信息iframe下拉框得到学生列表
			CloseableHttpResponse response = getSearchResponse(departName,
					"a.xy");
			Document iframe = Jsoup.parse(EntityUtils.toString(
					response.getEntity(), "gb2312"));
			String options = iframe.getElementById("DropDownList2").text();
			String[] optionsArr = options.split(" ");
			students = new ArrayList<Student>();
			for (int i = 0, len = optionsArr == null ? 0 : optionsArr.length; i < len; i++) {
				String studentStr = optionsArr[i];
				String studentNo = studentStr.substring(0,
						studentStr.indexOf("|"));
				String studentName = studentStr.substring(
						studentStr.lastIndexOf("|") + 1).replace("\\s+", "");
				Student student = new Student(studentNo, studentName);
				students.add(student);
			}
			response.close();
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return students;
	}

	/**
	 * 通过入学年份得到学生学号列表
	 * 
	 * @param yearOfAdmission
	 * @return
	 */
	private List<String> getStudentSnoList(String yearOfAdmission) {
		List<String> snoList = null;
		try {
			CloseableHttpResponse response = getSearchResponse(yearOfAdmission,
					"a.xh");
			Document iframe = Jsoup.parse(EntityUtils.toString(
					response.getEntity(), "gb2312"));
			String options = iframe.getElementById("DropDownList2").text();
			String[] optionsArr = options.split(" ");
			snoList = new ArrayList<String>();
			for (int i = 0, len = optionsArr == null ? 0 : optionsArr.length; i < len; i++) {
				String studentStr = optionsArr[i];
				if (studentStr.startsWith(yearOfAdmission)) {
					if (studentStr != null && studentStr.contains("|")) {
						String studentNo = studentStr.substring(0,
								studentStr.indexOf("|"));
						snoList.add(studentNo);
					}
				}
			}
			response.close();
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return snoList;
	}

	/**
	 * 得到学生所有信息列表
	 * 
	 * @param yearOfAdmission
	 *            入学年份
	 * @return 学生列表
	 */
	public List<Student> getStudentInfoList(String yearOfAdmission) {
		List<String> snoList = getStudentSnoList(yearOfAdmission);
		List<Student> studentList = null;
		try {
			studentList = new ArrayList<Student>();
			int length = snoList.size();
			for (int i = 0; i < length; i++) {
				String sno = snoList.get(i);
				CloseableHttpResponse response = getSearchResponse(sno, "a.xh");
				Document iframe = Jsoup.parse(EntityUtils.toString(
						response.getEntity(), "gb2312"));
				Student student = new Student();
				student.setStudentNo(sno);
				if (iframe.getElementById("xm") != null) {
					String xm = iframe.getElementById("xm").text();
					if (xm == null) {
						xm = "";
					}
					student.setName(new String(xm.getBytes(), "utf-8"));
					String xb = iframe.getElementById("xb").text();
					if (xb == null) {
						xb = "";
					}
					student.setGender(new String(xb.getBytes(), "utf-8"));
					student.setBirthDate(iframe.getElementById("csrq").text());
					String zzmm = iframe.getElementById("zzmm").text();
					if (zzmm == null) {
						zzmm = "";
					}
					student.setPoliticalStatus(new String(zzmm.getBytes(),
							"utf-8"));
					String mz = iframe.getElementById("mz").text();
					if (mz == null) {
						mz = "";
					}
					student.setNation(new String(mz.getBytes(), "utf-8"));
					String xy = iframe.getElementById("xy").text();
					if (xy == null) {
						xy = "";
					}
					student.setDepName(new String(xy.getBytes(), "utf-8"));
					String zymc = iframe.getElementById("zymc").text();
					if (zymc == null) {
						zymc = "";
					}
					student.setMajorName(new String(zymc.getBytes(), "utf-8"));
					String xzb = iframe.getElementById("xzb").text();
					if (xzb == null) {
						xzb = "";
					}
					student.setClassName(new String(xzb.getBytes(), "utf-8"));
					student.setSchoolSystem(iframe.getElementById("xz").text());
					String xjzt = iframe.getElementById("xjzt").text();
					if (xjzt == null) {
						xjzt = "";
					}
					student.setEnrollmentStatus(new String(xjzt.getBytes(),
							"utf-8"));
					student.setYearOfAdmission(iframe.getElementById("dqszj")
							.text());
					student.setAdmissionDate(iframe.getElementById("rxrq")
							.text());
					student.setIdCard(iframe.getElementById("sfzh").text());
					String zyfx = iframe.getElementById("zyfx").text();
					if (zyfx == null) {
						zyfx = "";
					}
					student.setMajorDirection(new String(zyfx.getBytes(),
							"utf-8"));
					System.out.println(i + ":" + student.toString());
					studentList.add(student);
				}
				response.close();
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return studentList;
	}

	/**
	 * 插入学生数据
	 * 
	 * @param studentList
	 *            学生列表
	 */
	public void insertStudentList(List<Student> studentList) {
		StudentDao sd = new StudentDao();
		sd.insert(studentList);
	}

	/**
	 * 得到学生照片
	 * 
	 * @param studentList
	 *            学生列表
	 * @param other
	 *            附加到图片名称的文字
	 */
	public void getStudentPicture(List<Student> studentList, String... other) {
		HttpGet get = new HttpGet();
		get.setHeader("Referer", searchUrl);
		for (int i = 0, len = studentList == null ? 0 : studentList.size(); i < len; i++) {
			Student student = studentList.get(i);
			String studentName = student.getName();
			String studentNo = student.getStudentNo();
			if (studentNo != null && studentNo.startsWith("2014")) {
				try {
					get.setURI(new URI(MessageFormat.format(
							LoginConstants.imageUrl, LoginHandler.UrlCode,
							studentNo)));
					CloseableHttpResponse response = client.execute(get);
					System.out.println("GET:" + get.getURI());
					// 设置文件路径和文件名
					StringBuilder fileName = new StringBuilder(imgSavedPath);
					if (!departName.isEmpty()) {
						fileName.append(departName).append('/')
								.append(studentNo);
					}
					if (studentName != null) {
						fileName.append("-").append(studentName);
					}
					if (other != null && other.length > 0) {
					}
					// 文件后缀
					fileName.append(".jpg");
					FileUtils.writeByteArrayToFile(
							new File(fileName.toString()),
							EntityUtils.toByteArray(response.getEntity()));
					response.close();
				} catch (URISyntaxException e) {
					System.out.println("url错误:" + get.getURI());
					if (!get.isAborted()) {
						get.abort();
					}
				} catch (IOException e) {
					System.out.println("url错误:" + get.getURI());
					if (!get.isAborted()) {
						get.abort();
					}
				}
			}
		}
	}

	/**
	 * 得到学生信息的iframe响应
	 * 
	 * @param serachVal
	 *            查询值
	 * @param mode
	 *            查询方式:学号，姓名等
	 * @return response响应
	 * @throws IOException
	 */
	private CloseableHttpResponse getSearchResponse(String serachVal,
			String mode) throws IOException {
		HttpEntity entity = this.buildSearchForm(state, serachVal, mode);
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
	 *            表单中的 __VIEWSTATE
	 * @param search
	 *            输入框的值
	 * @param mode
	 *            输入框的选项:学号，姓名等
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private HttpEntity buildSearchForm(String state, String search, String mode)
			throws UnsupportedEncodingException {
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("__VIEWSTATE", state));
		data.add(new BasicNameValuePair("DropDownList1", mode));
		data.add(new BasicNameValuePair("TextBox1", search));
		data.add(new BasicNameValuePair("Button3", "查  询"));
		HttpEntity entity = new UrlEncodedFormEntity(data, "gb2312");
		return entity;
	}
}
