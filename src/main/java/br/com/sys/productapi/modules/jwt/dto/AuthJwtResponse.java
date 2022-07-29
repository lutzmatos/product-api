package br.com.sys.productapi.modules.jwt.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthJwtResponse {
    private Integer id;
    private String name;
    private String email;

    public static AuthJwtResponse getUser(Claims jwtClaims) {
        try  {
            return new ObjectMapper().convertValue(jwtClaims.get("authUser"), AuthJwtResponse.class);
            //return AuthJwtResponse
            //        .builder()
            //        .id((Integer) jwtClaims.get("id"))
            //        .name((String) jwtClaims.get("name"))
            //        .email((String) jwtClaims.get("email"))
            //        .build();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
