package vn.chuot96.jwtiss.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.jwtiss.compo.IssueHelper;
import vn.chuot96.jwtiss.compo.RedisHelper;
import vn.chuot96.jwtiss.dto.Request;
import vn.chuot96.jwtiss.dto.Response;

import static vn.chuot96.jwtiss.constant.Token.*;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueHelper issueHelper;

    private final RedisHelper redisHelper;

    public Response firstLogin(Request request) {
        return new Response(issueHelper.encodeAccess(request), issueHelper.encodeRefresh(request));
    }

    public Mono<Response> rememberLogin(String refresh) {
        return redisHelper
                .isTokenBlacklisted(refresh)
                .flatMap(isBlacklisted -> {
                    if (Boolean.TRUE.equals(isBlacklisted)) {
                        return Mono.error(new RuntimeException("Refresh token is blacklisted"));
                    }
                    return Mono.just(false);
                })
                .flatMap(ignored -> {
                    boolean valid = issueHelper.validateRefresh(refresh);
                    if (!valid) return Mono.error(new RuntimeException("Invalid refresh token"));
                    return Mono.just(refresh);
                })
                .map(issueHelper::parseRefresh)
                .flatMap(request -> {
                    Duration remaining = issueHelper.getRefreshRemainingTime(refresh);
                    if (remaining.getSeconds() < REFRESH_THRESHOLD_SECONDS.getDuration()) {
                        String accessToken = issueHelper.encodeAccess(request);
                        String newRefreshToken = issueHelper.encodeRefresh(request);
                        return redisHelper
                                .blacklistToken(refresh, Duration.ofDays(REFRESH_TTL_DAYS.getDuration()))
                                .thenReturn(new Response(accessToken, newRefreshToken));
                    } else {
                        String accessToken = issueHelper.encodeAccess(request);
                        return Mono.just(new Response(accessToken, refresh));
                    }
                });
    }
}
