package vn.chuot96.jwtissapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;
import vn.chuot96.jwtissapi.dto.RequestDTO;
import vn.chuot96.jwtissapi.util.AccessTokenHandler;
import vn.chuot96.jwtissapi.util.RefreshTokenHandler;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder encoder;

    private final AccessTokenHandler accessTokenHandler;

    private final RefreshTokenHandler refreshTokenHandler;

    public String generateAccess(RequestDTO request) {
        return accessTokenHandler.generate(encoder, );
    }

    public String generateRefresh(RequestDTO request) {
        return refreshTokenHandler.generate(encoder, );
    }

    // --> more Token type here

}