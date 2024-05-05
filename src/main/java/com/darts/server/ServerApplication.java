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
		ArrayList<String> syms = new ArrayList<>();
		syms.add("fatigue");
		syms.add("muscle_pain");
		syms.add("red_spots_over_body");
		System.out.println(DecisionTreeSymp.getDisease(syms));
		SpringApplication.run(ServerApplication.class, args);
	}

}
