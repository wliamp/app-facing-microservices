package vn.chuot96.tokenIssuerAPI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import vn.chuot96.tokenIssuerAPI.constant.AccessToken;
import vn.chuot96.tokenIssuerAPI.dto.TokenRequestDTO;
import vn.chuot96.tokenIssuerAPI.util.JwtHandler;

import java.time.Instant;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder, TokenRequestDTO request) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateAccess(@RequestBody TokenRequestDTO request) {
        Instant now = Instant.now();
        long exp = now.getEpochSecond() + (request.duration() > 0 ? request.duration() : AccessToken.PATTERN.getDuration());
        String jti = UUID.randomUUID().toString();
        String formatted = String.format(AccessToken.PATTERN.getPattern(),
                request.subject(), now.getEpochSecond(), exp, jti,
                request.scope() != null ? request.scope() : "",
                request.audience() != null ? request.audience() : "");
        return JwtHandler.encodeToken(jwtEncoder, formatted);
    }

}
