package com.blueocn.SpringSecurityJWT.service;

import com.blueocn.SpringSecurityJWT.data.dto.AuthRequest;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;


    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .and()
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    public String extractUsername(String token) {
//            return extractClaim(token, Claims::getSubject);
//    }

    public Optional<String> extractUsername(String token) {
        try {
            return Optional.ofNullable(extractClaim(token, Claims::getSubject));
        } catch (Exception e) {
            // You could log this if needed
            return Optional.empty();
        }
    }

    public Optional<List<? extends GrantedAuthority>> extractAuthorities(String token) {
        try {
            List<String> roles = extractClaim(token, claims -> claims.get("roles", List.class));
            List<? extends GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            return Optional.of(authorities);
        } catch (Exception e) {
            return Optional.empty();
        }
    }




    private  <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            // Token has expired
            throw new RuntimeException("JWT token has expired", e);
        } catch (MalformedJwtException e) {
            // Token structure is invalid
            throw new RuntimeException("Invalid JWT token format", e);
        } catch (JwtException e) {
            // Covers SignatureException, UnsupportedJwtException, etc.
            throw new RuntimeException("JWT token parsing failed", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            String username = extractUsername(token).orElse(null);
            return username != null && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}