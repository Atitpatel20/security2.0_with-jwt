package com.security20.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.security20.entity.PropertyUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.expiry.duration}")
    private int expiryDuration;

    @Value("${jwt.issuer}")
    private String issuer;
    private Algorithm algorithm;
    private final static String USER_NAME="username";


    @PostConstruct
    public void postConstruct(){
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateToken(PropertyUser user){
        return JWT.create()
                .withClaim(USER_NAME,user.getUsername())
                .withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryDuration))
                .sign(algorithm);
    }
    // verify the token & get the username
    public String getUserName(String token){
//code to remember rwbv- rozi with bony ki bivi
        DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return decodedJWT.getClaim(USER_NAME).asString();
    }
}
