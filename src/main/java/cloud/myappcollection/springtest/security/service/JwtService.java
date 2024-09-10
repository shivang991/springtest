package cloud.myappcollection.springtest.security.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtService {

    private SecretKey secretKey;

    public JwtService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            secretKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
                .and()
                .signWith(secretKey)
                .compact();
    }

    public boolean isExpired(String token) {
        try {
            final Claims tokenClaims = extractAllClaims(token);
            final Date expiration = tokenClaims.getExpiration();

            return expiration.before(new Date());
        } catch (SignatureException exception) {
            return true;
        }
    }

    public String extractUserName(String token) {
        Claims claims;
        try {
            claims = extractAllClaims(token);
        } catch (SignatureException exception) {
            claims = null;
        }
        if (claims == null)
            return null;
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) throws SignatureException {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException exception) {
            throw exception;
        }
    }

}
