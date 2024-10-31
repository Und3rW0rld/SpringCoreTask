package com.uw.config;


import com.uw.config.jwt.AuthEntryPointJwt;
import com.uw.config.jwt.AuthTokenFilter;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
      private final AuthEntryPointJwt unauthorizedHandler;
      private final ApplicationUserService applicationUserService;

      public WebSecurityConfig(AuthEntryPointJwt unauthorizedHandler, ApplicationUserService applicationUserService) {
            this.unauthorizedHandler = unauthorizedHandler;
            this.applicationUserService = applicationUserService;
      }

      @Bean
      public AuthTokenFilter authenticationJwtTokenFilter() {
            return new AuthTokenFilter();
      }

      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .cors().and().csrf().disable().addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/actuator", "/api/v1/login", "/api/v1/trainers", "/api/v1/trainees",
                                    "/swagger-ui/**", "/v3/**", "/api/v1/refresh")
                            .permitAll()
                            .requestMatchers(EndpointRequest.to(HealthEndpoint.class)).permitAll()
                            .anyRequest().authenticated()
                    );

            http.headers()
                    .xssProtection()
                    .and()
                    .contentSecurityPolicy("script-src 'self'");

            http.authenticationProvider(daoAuthenticationProvider());

            http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

            return http.build();
      }

      @Bean
      public DaoAuthenticationProvider daoAuthenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setPasswordEncoder(PasswordConfig.passwordEncoder());
            provider.setUserDetailsService(applicationUserService);
            return provider;
      }

      @Bean
      AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
            return authConfig.getAuthenticationManager();
      }
}
