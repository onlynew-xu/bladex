package com.steelman.iot.platform.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-20
 */
public class TokenUtils {


    private static final long OUT_TIME = 60 * 60 * 1000 *24 *7;
    private static final String TOKEN_SECRET = "72e1916dafb1a316aa56B31f089d4742a3017a29";

    /**
     * 生成token
     *
     * @param userId
     * @param
     * @return
     */
    public static String createToken(Long userId,String nextKey,String username,String cid) {
        try {
            Long timeMiles=System.currentTimeMillis();
            Date date=new Date();
            //jwt生成的token 默认七天过期
            date.setTime(timeMiles+OUT_TIME);
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            Map<String, Object> header = new HashMap<>();
            header.put("type", "JWT");
            header.put("alg", "HS56");
            return JWT.create()
                    .withHeader(header)
                    .withClaim("nextKey", nextKey)
                    .withClaim("userId", userId)
                    .withClaim("username",username)
                    .withClaim("cid", cid)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密token
     * @param token
     * @return
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            jwtVerifier.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取userId
     * @param token
     * @return
     */
    public static Long getUserId(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        Long userId = decodedJWT.getClaim("userId").asLong();
        return userId;
    }

    /**
     * 获取username
     * @param token
     * @return
     */
    public static String getUserName(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getClaim("username").asString();
        return username;
    }

    /**
     * 获取username
     * @param token
     * @return
     */
    public static String getCid(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        String cid = decodedJWT.getClaim("cid").asString();
        return cid;
    }

    /**
     * 生成nextKey
     */
    private static Date date = new Date();
    private static int seq = 0;
    private static final int ROTATION = 99999;
    public static synchronized String nextKey(Long userId) {
        if (seq > ROTATION) {
            seq = 0;
        }
        date.setTime(System.currentTimeMillis());
        String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", date, seq++);
        return str+userId;
    }


    public static void main(String[] args){
      String name=  getUserName("eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJuZXh0S2V5IjoiMjAyMTA3MTQxNDEwMjYwMDAwMDEiLCJleHAiOjE2MjY4NDc4MjYsInVzZXJJZCI6MSwidXNlcm5hbWUiOiJhZG1pbi02NGNjNTBiZi0yYTMxLTRkYjEtYTlhNS1iZDEzNTMwNzM3MDIifQ.h16j8wFAw9jdSvz1E4GRs1zu3HbqvFr0z1hCWdUJ4yQ");
        System.out.println(name);
    }
}
