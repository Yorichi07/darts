package com.darts.server;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.zxing.WriterException;

@SpringBootApplication
public class ServerApplication {
	public static void main(String[] args) throws WriterException, IOException {
		SpringApplication.run(ServerApplication.class, args);
	}

}
