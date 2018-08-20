package com.hevelian.identity.entitlement.evaluator;

import com.hevelian.identity.entitlement.ctx.RequestCtxFactory2;
import com.hevelian.identity.entitlement.evaluator.finder.PAPFinderModule;
import com.hevelian.identity.entitlement.model.pap.PAPPolicy;
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

/**
 * Evaluates policy by content or attributes.
 */
public class EntitlementEngineForPAPPolicy {
  private final PDPConfig pdpConfig;
  private final PDP pdp;

  /**
   * Constructs a new <code>PDP</code> object.
   *
   * @param papPolicy the {@link PAPPolicy} object.
   */
  public EntitlementEngineForPAPPolicy(PAPPolicy papPolicy) {
    AttributeFinder attributeFinder = new AttributeFinder();
    List<AttributeFinderModule> attributeFinderModules = new ArrayList<>();
    SelectorModule selectorModule = new SelectorModule();
    CurrentEnvModule currentEnvModule = new CurrentEnvModule();

    attributeFinderModules.add(selectorModule);
    attributeFinderModules.add(currentEnvModule);
    attributeFinder.setModules(attributeFinderModules);

    PolicyFinder policyFinder = new PolicyFinder();
    Set<PolicyFinderModule> policyFinderModule = new HashSet<>();
    PAPFinderModule papFinderModule = new PAPFinderModule(papPolicy);
    policyFinderModule.add(papFinderModule);
    policyFinder.setModules(policyFinderModule);

    pdpConfig = new PDPConfig(attributeFinder, policyFinder, null, false);
    pdp = new PDP(pdpConfig);
  }

  /**
   * Attempts to evaluate the request against the policies known to this PDP. This is really the
   * core method of the entire XACML specification, and for most people will provide what you
   * want. If you need any special handling, you should look at the version of this method that
   * takes an <code>EvaluationCtx</code>.
   * <p>
   * Note that if the request is somehow invalid (it was missing a required attribute, it was
   * using an unsupported scope, etc), then the result will be a decision of INDETERMINATE.
   *
   * @param xacmlRequestCtx the request to evaluate.
   * @return a response paired to the request.
   */
  public ResponseCtx evaluateAsResponseCtx(RequestCtx xacmlRequestCtx) {
    return pdp.evaluate(xacmlRequestCtx);
  }

  /**
   * Converts parameters into {@link RequestCtx} object.
   *
   * @param subject     the subject policy value.
   * @param resource    resource policy value.
   * @param action      action policy value.
   * @param environment environment policy value.
   * @return a response paired to the request.
   * @throws ParsingException when can't read a policy.
   */
  public ResponseCtx evaluateAsResponseCtx(String subject, String resource, String action,
                                           String environment) throws ParsingException {
    RequestCtx requestCtx =
        RequestCtxFactory2.getFactory().getRequestCtx(subject, resource, action, environment);
    return evaluateAsResponseCtx(requestCtx);
  }

  /**
   * Converts a request string into {@link Element} object.
   *
   * @param xacmlRequest the request to evaluate.
   * @return the string representation of evaluating.
   */
  public String evaluate(String xacmlRequest) throws ParsingException {
    Element xacmlRequestElement = RequestCtxFactory.getFactory().getXacmlRequest(xacmlRequest);
    return evaluate(xacmlRequestElement);
  }

  /**
   * Converts {@link Element} into {@link RequestCtx} object.
   *
   * @param xacmlRequestElement the request to evaluate.
   * @return the string result of evaluating.
   */
  public String evaluate(Element xacmlRequestElement) throws ParsingException {
    RequestCtx requestCtx = RequestCtx.getInstance(xacmlRequestElement);
    return evaluate(requestCtx);
  }

  /**
   * Encodes {@link RequestCtx} objects.
   *
   * @param xacmlRequestCtx the request to evaluate.
   * @return the string result of evaluating.
   */
  public String evaluate(RequestCtx xacmlRequestCtx) {
    return evaluateAsResponseCtx(xacmlRequestCtx).encode();
  }
}

