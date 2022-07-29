package br.com.sys.productapi.modules.jwt.service;

import br.com.sys.productapi.config.exception.AuthenticationException;
import br.com.sys.productapi.modules.jwt.dto.AuthJwtResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class AuthJwtService {

    private static final String BEARER = "bearer ";

    @Value("${app-config.secrets.api-secret}")
    private String apiSecret;

    public void validateAuthorization(String token) {

        var accessToken = this.extractToken(token);

        try  {

            var claims = Jwts
                    .parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(apiSecret.getBytes()))
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            var user = AuthJwtResponse.getUser(claims);

            if (isEmpty(user) || isEmpty(user.getId()))
            {
                throw new AuthenticationException("The user is invalid");
            }
        }  catch (Exception exception) {
            //exception.printStackTrace();
            throw new AuthenticationException("Access denied because the token is invalid");
        }
    }

    private String extractToken(String token) {
        if (isEmpty(token)) {
            throw new AuthenticationException("The informed token is invalid");
        }

        String[] result = token.split(" ");
        if (token.toLowerCase().contains(BEARER) && result.length > 1) {
            token = result[result.length-1];
        }

        return token;
    }
}
