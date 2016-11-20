package com.hevelian.identity.entitlement.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Iterables;
import com.hevelian.identity.entitlement.PAPService;
import com.hevelian.identity.entitlement.PAPService.PolicyNotFoundByPolicyIdException;
import com.hevelian.identity.entitlement.model.PolicyType;
import com.hevelian.identity.entitlement.model.pap.PAPPolicy;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/PAPService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PAPController {
    private final PAPService papService;
    private static int I = 0;

    @RequestMapping(path = "/testAddPolicy", method = RequestMethod.POST)
    public void testAddPolicy() {
        PAPPolicy policy = new PAPPolicy();
        policy.setContent("<Policy/>");
        policy.setPolicyType(PolicyType.POLICY);
        policy.setPolicyId("policy-id-" + (I++));
        papService.addPolicy(policy);
    }

    @RequestMapping(path = "/testUpdatePolicy", method = RequestMethod.POST)
    public void testUpdatePolicy() throws PolicyNotFoundByPolicyIdException {
        PAPPolicy policy = new PAPPolicy();
        policy.setContent("<Policy att='" + I++ + "'/>");
        policy.setPolicyId("policy-id-" + 0);
        policy.setPolicyType(PolicyType.POLICY);
        papService.updatePolicy(policy);
    }

    @RequestMapping(path = "/testGetAll", method = RequestMethod.GET)
    public PAPPolicy[] testGetAll() {
        return Iterables.toArray(papService.getAllPolicies(), PAPPolicy.class);
    }
}
