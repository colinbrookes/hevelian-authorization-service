package com.hevelian.identity.entitlement.api;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Iterables;
import com.hevelian.identity.core.api.PrimitiveResult;
import com.hevelian.identity.entitlement.PolicyService;
import com.hevelian.identity.entitlement.model.Policy;
import com.hevelian.identity.entitlement.model.PolicyType;

@RestController
@RequestMapping(path = "/PolicyService")
public class PolicyController {
    private final PolicyService policyService;

    @Autowired
    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @RequestMapping(path = "/testAddPolicy", method = RequestMethod.GET)
    public void testAddPolicy() {
        Policy policy = new Policy();
        policy.setContent("<Policy/>");
        policy.setPolicyType(PolicyType.POLICY);
        policy.setPolicyId("policy-id-" + new Random().nextInt());
        policyService.getPolicyRepository().save(policy);
    }

    @RequestMapping(path = "/testCount", method = RequestMethod.GET)
    public PrimitiveResult<String> testCount() {
        return new PrimitiveResult<String>(String.valueOf(policyService.getPolicyRepository().count()));
    }

    @RequestMapping(path = "/testGetAll", method = RequestMethod.GET)
    public Policy[] testGetAll() {
        return Iterables.toArray(policyService.getPolicyRepository().findAll(), Policy.class);
    }

    public PolicyService getPolicyService() {
        return policyService;
    }
}
