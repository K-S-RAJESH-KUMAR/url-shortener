package com.rajesh.url_shortener.security.jwt;

import com.rajesh.url_shortener.sevice.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.expiration}")
    private  int expiration;

    @Value("${jwt.secret}")
    private String key;

    public String getJwtFromHeader(HttpServletRequest req)
    {
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return  null;
    }

    public String generateToken(UserDetailsImpl userDetails)
    {
        return Jwts.builder().subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime()+expiration))
                .signWith(key())
                .compact();
    }

    private SecretKey key()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    public String getUserName(String token)
    {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public boolean validateToken(String token)
    {
        try{
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(token);
            return true;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.toString());
        }
    }
}
