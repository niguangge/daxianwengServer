package com.niguang.daxianfeng.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.niguang.daxianfeng.service.RoomService;

@Controller
@RequestMapping("/resource/")
@CrossOrigin
public class ResourceController {
	@Autowired
	private RoomService roomService;

	@RequestMapping(value = "getImg", produces = MediaType.IMAGE_JPEG_VALUE)
	public BufferedImage getImage(@RequestParam("imgName") String imgName, HttpServletRequest request)
			throws IOException {
//		return ImageIO.read(new ClassPathResource("image/" + imgName).getInputStream());
		String path = "image/" + imgName;
		File file = new File(path); // 括号里参数为文件图片路径
		if (file.exists()) { // 如果文件存在
			InputStream in = new ClassPathResource(path).getInputStream(); // 用该文件创建一个输入流
			return ImageIO.read(in);
		} else
			return null;
	}
}
