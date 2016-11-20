package com.hevelian.identity.entitlement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hevelian.identity.entitlement.repository.PAPPolicyRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class PDPAdminService {
    @Getter
    private final PAPPolicyRepository policyRepository;
}
