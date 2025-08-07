package vn.chuot96.jwtiss.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.chuot96.jwtiss.compo.TokenCustomize;
import vn.chuot96.jwtiss.dto.RequestDTO;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenCustomize tokenCustomize;

    public String issueAccess(RequestDTO request) {
        return tokenCustomize.generateAccess(request);
    }

    public String issueRefresh(RequestDTO request) {
        return tokenCustomize.generateRefresh(request);
    }
}
