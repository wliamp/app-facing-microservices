package vn.chuot96.jwtissapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;
import vn.chuot96.jwtissapi.component.TokenCustomize;
import vn.chuot96.jwtissapi.dto.RequestDTO;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtEncoder encoder;

    private final TokenCustomize tokenCustomize;

    public String issueAccess(RequestDTO request) {
        return tokenCustomize.generateAccess(request);
    }

    public String issueRefresh(RequestDTO request) {
        return tokenCustomize.generateRefresh(request);
    }
}
