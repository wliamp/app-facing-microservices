package vn.chuot96.verify3rd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rd.compo.ExternalFileReader;
import vn.chuot96.verify3rd.compo.ForwardHelper;
import vn.chuot96.verify3rd.dto.UserDTO;

@Service
@RequiredArgsConstructor
public class ForwardService {
    private final ForwardHelper forwardHelper;

    private final ExternalFileReader externalFileReader;

    public Mono<?> forwardAuthServiceLogin(UserDTO user, String appCode) {
        return forwardHelper.post(
                "authentication-" + appCode.toLowerCase(),
                "/auth/login",
                externalFileReader.string("Verify3rdAuthLoginHeaderValue"),
                user,
                UserDTO.class);
    }

    public Mono<?> forwardJwtIssApiAccessRefresh(UserDTO user, String appCode) {
        return forwardHelper.post(
                "token-issuer-" + appCode.toLowerCase(),
                "/issue/access-refresh",
                externalFileReader.string("Verify3rdJwtIssARHeaderValue"),
                user,
                UserDTO.class);
    }
}
