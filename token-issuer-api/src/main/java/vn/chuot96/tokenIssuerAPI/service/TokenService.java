package vn.chuot96.tokenIssuerAPI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;
import vn.chuot96.tokenIssuerAPI.dto.TokenRequestDTO;
import vn.chuot96.tokenIssuerAPI.util.AccessTokenHandler;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder encoder;

    private final AccessTokenHandler handler;

    public String generateAccess(TokenRequestDTO request) {
        return handler.generate(encoder, request);
    }

}
