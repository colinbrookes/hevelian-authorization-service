package com.hevelian.identity.entitlement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hevelian.identity.entitlement.repository.PolicyRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class PolicyService {
    @Getter
    private final PolicyRepository policyRepository;
}
