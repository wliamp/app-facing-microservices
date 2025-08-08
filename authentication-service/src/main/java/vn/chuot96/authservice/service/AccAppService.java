package vn.chuot96.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.model.AccApp;
import vn.chuot96.authservice.repo.AccAppRepo;

@Service
@RequiredArgsConstructor
public class AccAppService {
    private final DatabaseClient dbClient;

    private final AccAppRepo accAppRepo;

    public Mono<AccApp> insertAccApp(Long accId, Long appId) {
        return accAppRepo.save(AccApp.builder().accId(accId).appId(appId).build());
    }

    public Mono<Void> deleteByAccCredentialAndAppCode(String credential, String code) {
        return accAppRepo.deleteById(
                findIdByAccCredentialAndAppCode(credential, code).then());
    }

    public Mono<Long> findIdByAccCredentialAndAppCode(String accCode, String appCode) {
        return dbClient.sql(
                        """
                                SELECT aa.id
                                FROM account_application aa
                                JOIN accounts acc ON acc.id = aa.account_id
                                JOIN applications app ON app.id = aa.application_id
                                WHERE acc.credential = ? AND app.code = ?
                                """)
                .bind(0, accCode)
                .bind(1, appCode)
                .map(row -> (Long) row.get("id"))
                .one();
    }
}
