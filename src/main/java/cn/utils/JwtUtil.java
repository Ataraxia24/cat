package cn.utils;

import cn.domain.Admin;
import cn.domain.Users;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

//需要导入java-jwt / jjwt 两个依赖
public class JwtUtil {

        //秘钥, 随便
        private static final String secretKey="Ataraxia@qq.com!Y#$%&()^><ioasf";

        /**
         * 生成 JWT TOKEN
         * @param user 用户信息
         * @return String token
         */
        public static String createToken(Users user){
            JwtBuilder jwtBuilder = Jwts.builder();
            String token = jwtBuilder
                    //header  固定
                    .setHeaderParam("typ", "JWT")
                    .setHeaderParam("alg", "HS256")
                    //payload  传输的数据
                    .claim("userId", user.getUserId())
                    .claim("userName", user.getUserEmail())
                    .setSubject("admin-test")
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))           //过期时间为1h
                    .setId(UUID.randomUUID().toString())
                    //signature  签名 固定
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
            return token;
        }

        /**
         * 解析 JWT TOKEN
         * @param token JWT TOKEN
         * @return User user
         */
        public static Users parse(String token){
            JwtParser parser = Jwts.parser();
            Jws<Claims> claimsJws = parser.setSigningKey(secretKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            System.out.println(claims.get("userId"));
            System.out.println(claims.get("userName"));
            System.out.println(claims.getId());
            System.out.println(claims.getSubject());
            System.out.println(claims.getExpiration());
            Users user=new Users();
            user.setUserId(Long.valueOf(claims.get("userId").toString()));
            user.setUserEmail((String)claims.get("userName"));
            return user;
        }
}
