package com.hevelian.identity.entitlement.pdp;

import com.hevelian.identity.core.exc.CoreRuntimeException;
import com.hevelian.identity.entitlement.exc.PolicyParsingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.balana.*;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.utils.Utils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class PolicyFactory {
  private static volatile PolicyFactory policyFactory;

  protected PolicyFactory() {}

  public static PolicyFactory getFactory() {
    if (policyFactory == null) {
      synchronized (PolicyFactory.class) {
        if (policyFactory == null) {
          policyFactory = new PolicyFactory();
        }
      }
    }
    return policyFactory;
  }

  public AbstractPolicy getXacmlPolicy(String policyContent) throws PolicyParsingException {
    return getXacmlPolicy(policyContent, null);
  }

  // TODOcheck policy finder behavior. e.g. when PAP is used.
  public AbstractPolicy getXacmlPolicy(String policyContent, PolicyFinder policyFinder)
      throws PolicyParsingException {
    AbstractPolicy policy = null;

    try (Reader reader = new StringReader(policyContent)) {
      // create the factory
      DocumentBuilderFactory factory = Utils.getSecuredDocumentBuilderFactory();
      factory.setIgnoringComments(true);
      factory.setNamespaceAware(true);
      factory.setValidating(false);

      // create a builder based on the factory & try to load the policy
      DocumentBuilder db = factory.newDocumentBuilder();
      Document doc = db.parse(new InputSource(reader));

      // handle the policy, if it's a known type
      Element root = doc.getDocumentElement();
      String name = DOMHelper.getLocalName(root);

      if (name.equals("Policy")) {
        policy = Policy.getInstance(root);
      } else if (name.equals("PolicySet")) {
        policy = (policyFinder == null ? PolicySet.getInstance(root)
            : PolicySet.getInstance(root, policyFinder));
      }
    } catch (IOException e) {
      throw new CoreRuntimeException("Unable to parse XACML Policy. Error reading content.", e);
    } catch (ParsingException e) {
      throw new PolicyParsingException(e.getMessage(), e);
    } catch (ParserConfigurationException e) {
      throw new CoreRuntimeException("XACML Policy parser configuration failed.", e);
    } catch (SAXException e) {
      throw new PolicyParsingException("Error parsing XACML Policy XML.", e);
    }
    return policy;
  }


}
