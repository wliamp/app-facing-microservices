package vn.chuot96.chatservice.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import vn.chuot96.chatservice.model.Msg;

public interface MsgRepo extends ReactiveMongoRepository<Msg, String> {
    Flux<Msg> findByChannelOrderByTimestampDesc(String channel);
}
