package vn.chuot96.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.chuot96.authservice.repo.ScopeRepo;

@Service
@RequiredArgsConstructor
public class ScopeService {
    private final ScopeRepo scopeRepo;
}
