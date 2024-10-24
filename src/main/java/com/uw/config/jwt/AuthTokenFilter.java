package com.uw.config.jwt;

import com.uw.config.ApplicationUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ApplicationUserService applicationUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String provideUserName = jwtUtils.getUsernameFromJwtToken(parseJwt(request));
        try{
            String jwt = parseJwt(request);
            if( jwt != null && jwtUtils.validateJwtToken(jwt)){
                String username = jwtUtils.getUsernameFromJwtToken(jwt);

                UserDetails userDetails = applicationUserService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(),
                                userDetails.getPassword(),
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            logger.error(String.format("Cannot set user authentication for user: %s. Error: %s", provideUserName, e.getMessage()));
        }
        filterChain.doFilter(request,response);
    }
    public String parseJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");

        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7);
        }

        return null;
    }
}