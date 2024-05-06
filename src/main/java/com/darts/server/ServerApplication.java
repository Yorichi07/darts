package com.darts.server;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.darts.server.functions.DecisionTreeSymp;
import com.google.zxing.WriterException;

@SpringBootApplication
public class ServerApplication {
	public static void main(String[] args) throws WriterException, IOException {
		DecisionTreeSymp.initObjs();
		SpringApplication.run(ServerApplication.class, args);
	}

}
