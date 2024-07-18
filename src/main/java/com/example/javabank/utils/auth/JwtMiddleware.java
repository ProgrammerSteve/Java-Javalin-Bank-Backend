package com.example.javabank.utils.auth;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpResponseException;
import io.jsonwebtoken.Claims;

public class JwtMiddleware implements Handler  {
    @Override
    public void handle(Context ctx) throws Exception {
        String authHeader = ctx.header("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new HttpResponseException(401, "Unauthorized");
        }
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        try {
            Claims claims = JwtUtil.validateToken(token);
            ctx.attribute("claims", claims);
        } catch (Exception e) {
            throw new HttpResponseException(401, "Unauthorized");
        }
    }

//    @Override
//    public void handle(Context ctx) throws Exception {
//
//        String token = ctx.header("Authorization");
//        if (token == null) {
//            throw new HttpResponseException(401, "Unauthorized, no Authorization header");
//        }
//        try {
//            Claims claims = JwtUtil.validateToken(token);
//            ctx.attribute("claims", claims);
//        } catch (Exception e) {
//            throw new HttpResponseException(401, "Unauthorized, token not validated");
//        }
//    }
}