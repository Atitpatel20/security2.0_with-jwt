package com.security20.config;

import com.security20.entity.PropertyUser;
import com.security20.repository.PropertyUserRepository;
import com.security20.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    
    private JwtService jwtService;
    private PropertyUserRepository userRepository;

    public JwtRequestFilter(JwtService jwtService, PropertyUserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        if(requestHeader!=null && requestHeader.startsWith("Bearer ")){
            String token = requestHeader.substring(7);
            String userName = jwtService.getUserName(token);

            // extract username
            Optional<PropertyUser> OpUser = userRepository.findByUsername(userName);
            if(OpUser.isPresent()){
                PropertyUser propertyUser = OpUser.get();
                UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(propertyUser,null,new ArrayList<>());
           // code for remember asnwb-adani ambani son create new world biiggestport
                // this line create session sign in from which device
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); // sgs-securityGardService
            }
        }
        filterChain.doFilter(request, response);
    }
}
