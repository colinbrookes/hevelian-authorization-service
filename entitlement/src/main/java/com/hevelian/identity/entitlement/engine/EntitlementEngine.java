package com.hevelian.identity.entitlement.engine;

import com.hevelian.identity.entitlement.ctx.RequestCtxFactory2;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
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
import org.wso2.balana.finder.ResourceFinder;
import org.wso2.balana.finder.impl.CurrentEnvModule;
import org.wso2.balana.finder.impl.SelectorModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a policy engine, providing the point for request evaluation.
 */
public class EntitlementEngine {
  @Getter
  @Setter(AccessLevel.PROTECTED)
  private PDP pdp;

  /**
   * Initializes {@link PDP} instance.
   * <p>
   * For a correct work of "evaluate" functions - this object must be initialized.
   * No any checks are performed during evaluation for performance considerations, so if the
   * object is not initialized - PDP instance might be null and a NullPointerException will occur.
   */
  public final void init() {
    PDPConfig pdpConfig = new PDPConfig(getAttributeFinder(), getPolicyFinder(), getResourceFinder(), isHandleMultipartRequest());
    setPdp(new PDP(pdpConfig));
  }

  /**
   * Returns the default configured {@link AttributeFinder}.
   */
  protected AttributeFinder getAttributeFinder() {
    AttributeFinder attributeFinder = new AttributeFinder();
    List<AttributeFinderModule> attributeFinderModules = new ArrayList<>();
    SelectorModule selectorModule = new SelectorModule();
    CurrentEnvModule currentEnvModule = new CurrentEnvModule();
    attributeFinderModules.add(selectorModule);
    attributeFinderModules.add(currentEnvModule);
    attributeFinder.setModules(attributeFinderModules);
    return attributeFinder;
  }

  protected PolicyFinder getPolicyFinder() {
    return new PolicyFinder();
  }

  protected ResourceFinder getResourceFinder() {
    return new ResourceFinder();
  }

  /**
   * Returns the boolean whether PDP is capable of handling multiple requests or not.
   *
   * @return true or false
   */
  protected boolean isHandleMultipartRequest() {
    return false;
  }

  /**
   * Evaluates the policy parameters as {@link RequestCtx}.
   *
   * @param subject  the subject policy value.
   * @param resource  the resource policy value.
   * @param action  the action policy value.
   * @param environment the environment policy value.
   * @return a response paired to the request.
   */
  public ResponseCtx evaluateAsResponseCtx(String subject, String resource, String action,
                                           String environment) throws ParsingException {
    RequestCtx requestCtx =
        RequestCtxFactory2.getFactory().getRequestCtx(subject, resource, action, environment);
    return evaluateAsResponseCtx(requestCtx);
  }

  /**
   * Uses the given policy parameters to determine a response.
   *
   * @param subject  the subject policy value.
   * @param resource  the resource policy value.
   * @param action  the action policy value.
   * @param environment the environment policy value.
   * @return he string representation of evaluating.
   */
  public String evaluate(String subject, String resource, String action, String environment)
      throws ParsingException {
    RequestCtx requestCtx =
        RequestCtxFactory2.getFactory().getRequestCtx(subject, resource, action, environment);
    return evaluate(requestCtx);
  }

  /**
   * Evaluates a string representation of XACML request.
   *
   * @param xacmlRequest the string request to evaluate.
   * @return the string representation of evaluating.
   */
  public String evaluate(String xacmlRequest) throws ParsingException {
    Element xacmlRequestElement = RequestCtxFactory.getFactory().getXacmlRequest(xacmlRequest);
    return evaluate(xacmlRequestElement);
  }

  /**
   * Evaluates the {@link Element} instance.
   *
   * @param xacmlRequestElement the request to evaluate.
   * @return the string representation of evaluating.
   */
  public String evaluate(Element xacmlRequestElement) throws ParsingException {
    RequestCtx requestCtx = RequestCtx.getInstance(xacmlRequestElement);
    return evaluate(requestCtx);
  }

  /**
   * Evaluates the {@link RequestCtx} instance.
   *
   * @param xacmlRequestCtx the request to evaluate.
   * @return the string representation of evaluating.
   */
  public String evaluate(RequestCtx xacmlRequestCtx) {
    return evaluateAsResponseCtx(xacmlRequestCtx).encode();
  }

  /**
   * Attempts to evaluate the request against the policies known to this PDP.
   * <p>
   * Note that if the request is somehow invalid (it was missing a required attribute, it was
   * using an unsupported scope, etc), then the result will be a decision of INDETERMINATE.
   *
   * @param xacmlRequestCtx the request to evaluate.
   * @return a response paired to the request.
   */
  public ResponseCtx evaluateAsResponseCtx(RequestCtx xacmlRequestCtx) {
    return getPdp().evaluate(xacmlRequestCtx);
  }
}
