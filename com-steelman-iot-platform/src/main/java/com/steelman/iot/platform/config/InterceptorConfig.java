//package com.steelman.iot.platform.config;
//
//import org.springframework.boot.web.servlet.MultipartConfigFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.servlet.MultipartConfigElement;
//
//
//@Configuration
//public class InterceptorConfig implements WebMvcConfigurer {
//
//	@Bean
//	public AppInterceptor initInterceptor(){
//        return new AppInterceptor();
//    }
//
//
//	@Bean
//    MultipartConfigElement multipartConfigElement() {
//       MultipartConfigFactory factory = new MultipartConfigFactory();
//       factory.setLocation("c://upload");
//       return factory.createMultipartConfig();
//    }
//
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/upload/**").addResourceLocations("file:C:/upload/");
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/page/**").addResourceLocations("classpath:/page/");
//
//	}
//
//
//	@Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        InterceptorRegistration registration = registry.addInterceptor(initInterceptor());   //app  //拦截的对象会进入这个类中进行判断
//        registration.addPathPatterns("/api/apiLogin/**","/api/region/saveRegionArea","/api/region/saveBuilding","/api/region/saveStorey"
//                ,"/api/region/saveRooom","/api/pment/deviceList","/api/pment/saveDevice","/api/pment/updateDevice"
//        		);
//
//        //不过滤的地址
//		registration.excludePathPatterns("/api/apiLogin/userlogin");
//
//        WebMvcConfigurer.super.addInterceptors(registry);
//
//    }
//
//
//}
