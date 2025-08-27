package io.wliamp.pay.compo.props;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "payment.gateway")
public class PaymentGtwProps {
    private Gateway creditCard;

    @Data
    public static class Gateway {
        private String url;
        private String apiKey;
    }
}
