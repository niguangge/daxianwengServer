package com.niguang.daxianfeng.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "wx")
@PropertySource(value = "config/config.properties")
@Data
public class ConfigBeanProp {

	private String appid;
	private String secret;

}