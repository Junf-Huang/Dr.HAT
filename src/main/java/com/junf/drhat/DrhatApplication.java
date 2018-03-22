package com.junf.drhat;

import com.junf.drhat.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DrhatApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrhatApplication.class, args);
	}

	//删除上传文件夹，并初始化
    /*@Bean
	CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }*/

}
