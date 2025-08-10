package vn.chuot96.chatservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.chuot96.chatservice.model.Msg;
import vn.chuot96.chatservice.repo.MsgRepo;

@Service
@RequiredArgsConstructor
public class MsgService {
    private final MsgRepo msgRepo;

    public Mono<Msg> save(Msg msg) {
        return msgRepo.save(msg);
    }

    public Flux<Msg> getRecentMsgs(String channel, int limit) {
        return msgRepo.findByChannelOrderByTimestampDesc(channel).take(limit);
    }
}
