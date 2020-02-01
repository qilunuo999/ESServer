package com.mychaincode.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@Configuration
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        //  单个数据大小
//        factory.setMaxFileSize("102400KB"); // KB,MB
//        /// 总上传数据大小
//        factory.setMaxRequestSize("102400KB");
//        return factory.createMultipartConfig();
//    }

}
