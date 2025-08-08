package vn.chuot96.authservice.compo;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.AuthRequest;
import vn.chuot96.authservice.service.ForwardService;

@Aspect
@Component
@RequiredArgsConstructor
public class ForwardPriorityAspect {
    private final ForwardService forwardService;

    @Around("@annotation(vn.chuot96.anno.ForwardPriority) || within(@vn.chuot96.anno.ForwardPriority *)")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        AuthRequest request = (AuthRequest) args[0];
        Mono<?> forwardMono = forwardService.forwardRCache(request);
        return forwardMono.then(Mono.defer(() -> {
            try {
                return (Mono<?>) joinPoint.proceed();
            } catch (Throwable e) {
                return Mono.error(e);
            }
        }));
    }
}
