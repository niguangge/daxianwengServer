package com.niguang.daxianfeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.niguang.daxianfeng.controller.WebSocketServer;

@SpringBootApplication
public class DaxianfengApplication {

	public static void main(String[] args) {
		ApplicationContext run = SpringApplication.run(DaxianfengApplication.class, args);
	}

}
