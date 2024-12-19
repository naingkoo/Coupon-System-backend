package com.coupon.AuthenConfig;

import com.coupon.entity.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
@Service
public class JwtService {

    @Value("${secretkey}")
    private String secretkey ;

    public JwtService() {

//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = keyGen.generateKey();
//            System.out.println("secretkey is    "+sk);
//            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        // Add roles to the claims map (you can name this whatever you want, "roles" in this case)
        claims.put("roles", user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("coupon server")
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getKey())
                .compact();

    }



    //
    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        System.out.println("jwtservice :extractclaim method");
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
       try
       {
           return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
        throw new RuntimeException("Invalid JWT Signature or Token", e);
    } catch (ExpiredJwtException e) {
        throw e; // Optional: Re-throw to handle it in your filter or caller
    } catch (UnsupportedJwtException e) {
        throw new RuntimeException("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("JWT Claims String is Empty", e);
    }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()));
    }
//
//    private boolean isTokenExpired(String token) {
//        System.out.println(" isTokenExpired(String token)");
//        return extractExpiration(token).before(new Date());
//    }
//
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

}
