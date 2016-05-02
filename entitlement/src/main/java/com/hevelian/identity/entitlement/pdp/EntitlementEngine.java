package com.hevelian.identity.entitlement.pdp;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.wso2.balana.Balana;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.ParsingException;
import org.wso2.balana.ctx.RequestCtxFactory;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.ctx.xacml3.RequestCtx;

import com.hevelian.identity.entitlement.ctx.RequestCtxFactory2;

@Component
@Scope("singleton")
public class EntitlementEngine {
    private final PDPConfig pdpConfig;
    private final PDP pdp;

    public EntitlementEngine() {
        pdpConfig = Balana.getInstance().getPdpConfig();
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
        RequestCtx requestCtx = RequestCtxFactory2.getFactory().getRequestCtx(subject, resource,
                action, environment);
        return evaluate(requestCtx);
    }

    public ResponseCtx evaluateAsResponseCtx(String subject, String resource, String action,
            String environment) throws ParsingException {
        RequestCtx requestCtx = RequestCtxFactory2.getFactory().getRequestCtx(subject, resource,
                action, environment);
        return evaluateAsResponseCtx(requestCtx);
    }

    public ResponseCtx evaluateAsResponseCtx(RequestCtx xacmlRequestCtx) throws ParsingException {
        return pdp.evaluate(xacmlRequestCtx);
    }

    public String evaluate(RequestCtx xacmlRequestCtx) throws ParsingException {
        return evaluateAsResponseCtx(xacmlRequestCtx).encode();
    }
}
