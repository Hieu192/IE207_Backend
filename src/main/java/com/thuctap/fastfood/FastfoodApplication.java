package com.thuctap.fastfood;

import com.thuctap.fastfood.services.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;

@RequiredArgsConstructor
@SpringBootApplication
public class FastfoodApplication {

  public static void main(String[] args) {
    SpringApplication.run(FastfoodApplication.class, args);
  }
  public static final HashMap<String, String> forgotPasswordMap =  new HashMap<>();
  private final EmailSenderService senderService;
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
      }
    };
  }

//  @EventListener(ApplicationReadyEvent.class)
//  public void sendEmail() {
//    senderService.sendEmail("phuongsover5@gmail.com","This is the subject","This is the body");
//  }
}
