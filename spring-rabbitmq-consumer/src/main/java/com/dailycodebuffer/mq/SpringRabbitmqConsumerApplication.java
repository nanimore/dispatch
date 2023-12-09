package com.dailycodebuffer.mq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dailycodebuffer.mq.mapper")
public class SpringRabbitmqConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRabbitmqConsumerApplication.class, args);
	}

}
