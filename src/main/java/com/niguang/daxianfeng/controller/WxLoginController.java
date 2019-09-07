package com.niguang.daxianfeng.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.niguang.daxianfeng.model.ConfigBeanProp;
import com.niguang.daxianfeng.model.Room;

@RestController
@RequestMapping("/wx/")
@CrossOrigin
public class WxLoginController {
	@Autowired
	private ConfigBeanProp configBeanProp;

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public @ResponseBody String login(Model model, HttpServletRequest request, @RequestParam("code") String code)
			throws Exception {
		String appId = configBeanProp.getAppid();
		String secret = configBeanProp.getSecret();
		String uri = String.format(
				"https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
				appId, secret, code);
		System.out.println("uri is " + uri);
		try {
			URL url = new URL(uri);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoOutput(true); // 设置该连接是可以输出的
			connection.setRequestMethod("GET"); // 设置请求方式
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line = null;
			StringBuilder result = new StringBuilder();
			while ((line = br.readLine()) != null) { // 读取数据
				result.append(line + "\n");
			}
			connection.disconnect();

			System.out.println(result.toString());
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
