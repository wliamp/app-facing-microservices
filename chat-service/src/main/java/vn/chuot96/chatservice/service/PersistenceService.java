package vn.chuot96.chatservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.chatservice.dto.ChatMsg;
import vn.chuot96.chatservice.model.Msg;
import vn.chuot96.chatservice.repo.MsgRepo;

@Service
@RequiredArgsConstructor
public class PersistenceService {
    private final MsgRepo msgRepo;

    public Mono<Msg> persist(ChatMsg msg) {
        Msg e = new Msg();
        e.setSender(msg.sender());
        e.setContent(msg.content());
        e.setChannel(msg.channel());
        e.setTimestamp(msg.timestamp());
        return msgRepo.save(e);
    }
}
