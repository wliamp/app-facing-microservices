package vn.chuot96.auth3rdapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.chuot96.auth3rdapi.component.OtpProvider;
import vn.chuot96.auth3rdapi.dto.UserDTO;
import vn.chuot96.auth3rdapi.exception.NoSupportedProviderException;

import java.util.List;

import static vn.chuot96.auth3rdapi.constant.AuthMessage.NOT_FOUND_OTP;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final List<OtpProvider> providers;

    public UserDTO verifyToken(String token) {
        return providers.stream()
                .filter(p -> p.supports(token))
                .findFirst()
                .map(p -> p.verifyToken(token))
                .orElseThrow(() -> new NoSupportedProviderException(NOT_FOUND_OTP.getMessage()));
    }

}
