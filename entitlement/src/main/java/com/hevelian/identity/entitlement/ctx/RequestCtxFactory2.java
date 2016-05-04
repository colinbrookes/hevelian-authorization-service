package com.hevelian.identity.entitlement.ctx;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.wso2.balana.ParsingException;
import org.wso2.balana.XACMLConstants;
import org.wso2.balana.attr.StringAttribute;
import org.wso2.balana.ctx.Attribute;
import org.wso2.balana.ctx.xacml3.RequestCtx;
import org.wso2.balana.utils.Constants.PolicyConstants;
import org.wso2.balana.xacml3.Attributes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestCtxFactory2 {
    /**
     * Instance of this class
     */
    private static volatile RequestCtxFactory2 factoryInstance;

    public static RequestCtxFactory2 getFactory() {
        if (factoryInstance == null) {
            synchronized (RequestCtxFactory2.class) {
                if (factoryInstance == null) {
                    factoryInstance = new RequestCtxFactory2();
                }
            }
        }

        return factoryInstance;
    }

    public RequestCtx getRequestCtx(String subject, String resource, String action,
            String environment) throws ParsingException {
        Set<Attributes> attributesSet = new HashSet<Attributes>();
        try {
            if (subject != null) {
                attributesSet.add(getStringAttributes(PolicyConstants.SUBJECT_CATEGORY_URI,
                        PolicyConstants.SUBJECT_ID_DEFAULT, subject));
            }
            if (resource != null) {
                attributesSet.add(getStringAttributes(PolicyConstants.RESOURCE_CATEGORY_URI,
                        PolicyConstants.RESOURCE_ID, subject));
            }
            if (action != null) {
                attributesSet.add(getStringAttributes(PolicyConstants.ACTION_CATEGORY_URI,
                        PolicyConstants.ACTION_ID, subject));
            }
            if (environment != null) {
                attributesSet.add(getStringAttributes(PolicyConstants.ENVIRONMENT_CATEGORY_URI,
                        PolicyConstants.ENVIRONMENT_ID, subject));
            }
        } catch (URISyntaxException e) {
            throw new ParsingException(
                    "Error occurred while building XACML request from attributes.", e);
        }
        return new RequestCtx(attributesSet, null);
    }

    private Attributes getStringAttributes(String categoryUri, String idUri, String value)
            throws URISyntaxException {
        Set<Attribute> set = new HashSet<>();
        set.add(new Attribute(new URI(idUri), null, null, new StringAttribute(value),
                XACMLConstants.XACML_VERSION_3_0));

        return new Attributes(new URI(categoryUri), set);
    }
}
