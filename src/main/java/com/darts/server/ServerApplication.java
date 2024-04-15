package com.darts.server;

import java.io.IOException;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.google.zxing.WriterException;

@SpringBootApplication
public class ServerApplication {
	public static void main(String[] args) throws WriterException, IOException {
		ConfigurableApplicationContext context = SpringApplication.run(ServerApplication.class, args);
	}

}
