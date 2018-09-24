package com.hevelian.identity.entitlement;

import java.util.Iterator;

import com.hevelian.identity.entitlement.logging.EntitlementLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wso2.balana.ParsingException;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.ResponseCtx;
import com.hevelian.identity.entitlement.pdp.PDPEntitlementEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class EntitlementService {
  private final PDPEntitlementEngine pdpEntitlementEngine;
  private static final EntitlementLogger eLog = new EntitlementLogger(log);

  public String getDecision(String request) throws ParsingException {
    eLog.logRequest(request);
    String response = pdpEntitlementEngine.evaluate(request);
    eLog.logResponse(response);
    return response;
  }

  public String getDecisionByAttributes(String subject, String resource, String action,
      String environment) throws ParsingException {
    eLog.logAttributeRequest(subject, resource, action, environment);
    String response = pdpEntitlementEngine.evaluate(subject, resource, action, environment);
    eLog.logResponse(response);
    return response;
  }

  public boolean getBooleanDecision(String subject, String resource, String action,
      String environment) throws ParsingException {
    eLog.logAttributeRequest(subject, resource, action, environment);
    ResponseCtx responseCtx =
        pdpEntitlementEngine.evaluateAsResponseCtx(subject, resource, action, environment);
    eLog.logResponse(responseCtx.encode());
    Iterator<AbstractResult> iter = responseCtx.getResults().iterator();
    return iter.hasNext() ? iter.next().getDecision() == AbstractResult.DECISION_PERMIT : false;
  }

}
