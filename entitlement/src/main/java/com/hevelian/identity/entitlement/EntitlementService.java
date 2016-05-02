package com.hevelian.identity.entitlement;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wso2.balana.ParsingException;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.ResponseCtx;

import com.hevelian.identity.entitlement.pdp.EntitlementEngine;

@Service
public class EntitlementService {
    private static final Logger LOG = LogManager.getLogger(EntitlementService.class);

    private final EntitlementEngine entitlementEngine;

    @Autowired
    public EntitlementService(EntitlementEngine entitlementEngine) {
        this.entitlementEngine = entitlementEngine;
    }

    public String getDecision(String request) throws ParsingException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("XACML Request:\n" + request);
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
        if (LOG.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("XACML Request Attributes:\n").append("Subject: ")
                    .append(subject).append("\nResource: ").append(resource).append("\nAction: ")
                    .append(action).append("\nEnvironment: ").append(environment);
            LOG.debug(sb.toString());
        }
    }

    private void logResponse(String response) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("XACML Response:\n" + response);
        }
    }

    public EntitlementEngine getEntitlementEngine() {
        return entitlementEngine;
    }

}
