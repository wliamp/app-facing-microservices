package vn.chuot96.authen3rdAPI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.chuot96.authen3rdAPI.component.OtpProvider;
import vn.chuot96.authen3rdAPI.dto.ResponseDTO;
import vn.chuot96.authen3rdAPI.exception.NoSupportedProviderException;

import java.util.List;

import static vn.chuot96.authen3rdAPI.constant.AuthMessage.NOT_FOUND_OTP;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final List<OtpProvider> providers;

    public ResponseDTO verifyToken(String token) {
        return providers.stream()
                .filter(p -> p.supports(token))
                .findFirst()
                .map(p -> p.verifyToken(token))
                .orElseThrow(() -> new NoSupportedProviderException(NOT_FOUND_OTP.getMessage()));
    }

}
