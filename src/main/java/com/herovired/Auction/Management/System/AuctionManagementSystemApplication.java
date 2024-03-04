package com.herovired.Auction.Management.System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuctionManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionManagementSystemApplication.class, args);
	}

}
