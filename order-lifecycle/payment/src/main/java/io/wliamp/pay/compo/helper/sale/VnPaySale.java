package io.wliamp.pay.compo.helper.sale;

import io.github.wliamp.pro.pay.PaymentProvider;
import io.github.wliamp.pro.pay.VnPayRequest;
import io.wliamp.pay.dto.SaleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("vnPay")
@RequiredArgsConstructor
public class VnPaySale implements ISale {
    private final PaymentProvider paymentProvider;

    private final ApplicationContext applicationContext;

    @Override
    public Mono<Boolean> execute(SaleRequest saleRequest) {
        String providerName = applicationContext.getBeanNamesForType(this.getClass())[0].toUpperCase();
        return paymentProvider
                .getVnPay()
                .sale(VnPayRequest.builder()
                        .vnpAmount(saleRequest.amount())
                        .vnpIpAddr(saleRequest.ipAddress())
                        .vnpOrderInfo(saleRequest.description())
                        .vnpTxnRef(saleRequest.orderId())
                        .build())
                .hasElement();
    }
}
