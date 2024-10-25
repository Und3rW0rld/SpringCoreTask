package com.uw.config;

import feign.codec.Decoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

@Configuration
public class FeignClientConfig {

      @Bean
      public Decoder feignDecoder() {
            return new ResponseEntityDecoder(new SpringDecoder(() -> {
                  HttpMessageConverter<?> converter = new StringHttpMessageConverter();
                  return new HttpMessageConverters(converter);
            }));
      }
}