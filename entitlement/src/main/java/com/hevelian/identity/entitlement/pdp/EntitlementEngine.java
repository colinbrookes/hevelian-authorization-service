package com.hevelian.identity.entitlement.pdp;

import com.hevelian.identity.entitlement.PDPService;
import com.hevelian.identity.entitlement.ctx.RequestCtxFactory2;
import com.hevelian.identity.entitlement.pdp.finder.PDPPolicyFinderModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.ParsingException;
import org.wso2.balana.ctx.RequestCtxFactory;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.ctx.xacml3.RequestCtx;
import org.wso2.balana.finder.AttributeFinder;
import org.wso2.balana.finder.AttributeFinderModule;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;
import org.wso2.balana.finder.impl.CurrentEnvModule;
import org.wso2.balana.finder.impl.SelectorModule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Scope("singleton")
public class EntitlementEngine {
  private final PDPConfig pdpConfig;
  private final PDP pdp;

  @Autowired
  public EntitlementEngine(PDPService pdpService) {
    AttributeFinder attributeFinder = new AttributeFinder();
    List<AttributeFinderModule> attributeFinderModules = new ArrayList<>();
    SelectorModule selectorModule = new SelectorModule();
    CurrentEnvModule currentEnvModule = new CurrentEnvModule();
    attributeFinderModules.add(selectorModule);
    attributeFinderModules.add(currentEnvModule);
    attributeFinder.setModules(attributeFinderModules);

    PolicyFinder policyFinder = new PolicyFinder();
    Set<PolicyFinderModule> policyFinderModules = new HashSet<>();
    PDPPolicyFinderModule inMemoryPolicyFinderModule = new PDPPolicyFinderModule(pdpService);

    policyFinderModules.add(inMemoryPolicyFinderModule);
    policyFinder.setModules(policyFinderModules);

    pdpConfig = new PDPConfig(attributeFinder, policyFinder, null, false);
    pdp = new PDP(pdpConfig);
  }

  public String evaluate(String xacmlRequest) throws ParsingException {
    Element xacmlRequestElement = RequestCtxFactory.getFactory().getXacmlRequest(xacmlRequest);
    return evaluate(xacmlRequestElement);
  }

  public String evaluate(Element xacmlRequestElement) throws ParsingException {
    RequestCtx requestCtx = RequestCtx.getInstance(xacmlRequestElement);
    return evaluate(requestCtx);
  }

  public String evaluate(String subject, String resource, String action, String environment)
      throws ParsingException {
    RequestCtx requestCtx =
        RequestCtxFactory2.getFactory().getRequestCtx(subject, resource, action, environment);
    return evaluate(requestCtx);
  }

  public ResponseCtx evaluateAsResponseCtx(String subject, String resource, String action,
      String environment) throws ParsingException {
    RequestCtx requestCtx =
        RequestCtxFactory2.getFactory().getRequestCtx(subject, resource, action, environment);
    return evaluateAsResponseCtx(requestCtx);
  }

  public ResponseCtx evaluateAsResponseCtx(RequestCtx xacmlRequestCtx) {
    return pdp.evaluate(xacmlRequestCtx);
  }

  public String evaluate(RequestCtx xacmlRequestCtx) {
    return evaluateAsResponseCtx(xacmlRequestCtx).encode();
  }
}
