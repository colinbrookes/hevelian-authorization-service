package com.hevelian.identity.entitlement;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wso2.balana.ParsingException;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.ResponseCtx;

import com.hevelian.identity.entitlement.pdp.EntitlementEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
@Log4j2
public class EntitlementService {
    private final EntitlementEngine entitlementEngine;

    public String getDecision(String request) throws ParsingException {

        if (log.isDebugEnabled()) {
            log.debug("XACML Request:\n" + request);
        }
        String response = entitlementEngine.evaluate(request);
        logResponse(response);
        return response;
    }

    public String getDecisionByAttributes(String subject, String resource, String action,
            String environment) throws ParsingException {
        logAttributeRequest(subject, resource, action, environment);
        String response = entitlementEngine.evaluate(subject, resource, action, environment);
        logResponse(response);
        return response;
    }

    public boolean getBooleanDecision(String subject, String resource, String action,
            String environment) throws ParsingException {
        logAttributeRequest(subject, resource, action, environment);
        ResponseCtx responseCtx = entitlementEngine.evaluateAsResponseCtx(subject, resource, action,
                environment);
        logResponse(responseCtx.encode());
        Iterator<AbstractResult> iter = responseCtx.getResults().iterator();
        return iter.hasNext() ? iter.next().getDecision() == AbstractResult.DECISION_PERMIT : false;
    }

    private void logAttributeRequest(String subject, String resource, String action,
            String environment) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("XACML Request Attributes:\n").append("Subject: ")
                    .append(subject).append("\nResource: ").append(resource).append("\nAction: ")
                    .append(action).append("\nEnvironment: ").append(environment);
            log.debug(sb.toString());
        }
    }

    private void logResponse(String response) {
        if (log.isDebugEnabled()) {
            log.debug("XACML Response:\n" + response);
        }
    }
}
