package com.jplus.spider;

import com.jplus.bean.Student;
import com.jplus.dao.StudentDao;

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

public class StudentPick {

	private CloseableHttpClient client = LoginConstants
			.getCustomCloseableHttpClient();
	private String state;
	private String contentRef;
	private String searchUrl;
	private String departName = "";

	public StudentPick(LoginPage login) {
		try {
			if (login.hasLogin()) {
				contentRef = MessageFormat.format(LoginConstants.contentRef,
						LoginPage.UrlCode);
				searchUrl = MessageFormat.format(LoginConstants.searchUrl,
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
	 * 得到iframe响应
	 * 
	 * @param serachVal
	 *            查询值
	 * @param mode
	 *            查询方式
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
	 * 通过院系名称查询到该院系所有学生
	 * 
	 * @param departName
	 *            院系名称
	 * @return 学生列表
	 */
	public List<Student> searchPic(String departName) {
		this.departName = departName;
		List<Student> students = null;
		try {
			//通过学生信息iframe下拉框得到学生列表
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
			return students;
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return null;
	}

	private List<String> generateSnoList(String yearOfAdmission) {
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
			return snoList;
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return null;
	}

	public List<Student> searchBasicInfo(String yearOfAdmission) {
		List<String> snoList = generateSnoList(yearOfAdmission);
		try {
			List<Student> studentList = new ArrayList<Student>();
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
			StudentDao sd = new StudentDao();
			sd.insert(studentList);
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return null;
	}

	/**
	 * 构建查询表单
	 * 
	 * @param state
	 *            asp.net __VIEWSTATE值
	 * @param search
	 *            查询名称
	 * @param mode
	 *            查询方式
	 * @return HttpEntity http 实体
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

	/**
	 * 得到学生照片
	 * 
	 * @param students
	 *            学生列表
	 * @param other
	 *            附加到图片名称的文字
	 */
	public void getImage(List<Student> students, String... other) {
		HttpGet get = new HttpGet();
		get.setHeader("Referer", searchUrl);
		for (int i = 0, len = students == null ? 0 : students.size(); i < len; i++) {
			Student student = students.get(i);
			String studentName = student.getName();
			String studentNo = student.getStudentNo();
			if (studentNo != null && studentNo.startsWith("2014")) {
				try {
					get.setURI(new URI(MessageFormat.format(
							LoginConstants.imageUrl, LoginPage.UrlCode,
							studentNo)));
					CloseableHttpResponse response = client.execute(get);
					System.out.println("GET:" + get.getURI());
					// 设置文件路径和文件名
					StringBuilder fileName = new StringBuilder(
							"F:/RayLew/HBNUStudentPicture/");
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
}
