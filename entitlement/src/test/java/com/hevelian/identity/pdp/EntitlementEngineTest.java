package com.hevelian.identity.pdp;

import java.util.Scanner;

import org.junit.Test;
import org.wso2.balana.ParsingException;

import com.hevelian.identity.entitlement.pdp.EntitlementEngine;

public class EntitlementEngineTest {
    @Test
    public void evaluate_StringRequest_Permit() throws ParsingException {
        System.setProperty("org.wso2.balana.PolicyDirectory",
                "C:\\Y\\Data\\Hevelian\\config\\pdp\\policies\\");

        Scanner scan = new Scanner(
                EntitlementEngineTest.class.getResourceAsStream("/check-permit.xml"));

        String result = new EntitlementEngine().evaluate(scan.useDelimiter("\\A").next());
        System.out.println(result);
        scan.close();
    }
}
