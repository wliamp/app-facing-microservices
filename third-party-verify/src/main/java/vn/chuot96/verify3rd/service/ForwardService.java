package vn.chuot96.verify3rd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rd.compo.ExternalFileReader;
import vn.chuot96.verify3rd.compo.ForwardHelper;
import vn.chuot96.verify3rd.dto.User;

@Service
@RequiredArgsConstructor
public class ForwardService {
    private final ForwardHelper forwardHelper;

    private final ExternalFileReader externalFileReader;

    public Mono<?> forwardAuthServiceLogin(User user) {
        return forwardHelper.post(
                "authentication",
                "/auth/login",
                externalFileReader.getOne("Verify3rdForwardAuthLoginHeaderValue"),
                user,
                User.class);
    }

    public Mono<?> forwardJwtIssApiAccessRefresh(User user) {
        return forwardHelper.post(
                "token-issuer",
                "/issue/non-refresh",
                externalFileReader.getOne("Verify3rdForwardJwtIssARHeaderValue"),
                user,
                User.class);
    }
}
