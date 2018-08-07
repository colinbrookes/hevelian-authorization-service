package com.hevelian.identity.entitlement.api;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wso2.balana.ParsingException;
import com.hevelian.identity.core.api.PrimitiveResult;
import com.hevelian.identity.entitlement.EntitlementService;
import com.hevelian.identity.entitlement.api.dto.EntitlementAttributesDTO;
import com.hevelian.identity.entitlement.api.dto.EntitlementRequestDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/EntitlementService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EntitlementController {
  private final EntitlementService entitlementService;

  @RequestMapping(path = "/getDecision", method = RequestMethod.POST)
  public PrimitiveResult<String> getDecision(@Valid @RequestBody EntitlementRequestDTO request)
      throws ParsingException {
    return new PrimitiveResult<>(entitlementService.getDecision(request.getRequest()));
  }

  @RequestMapping(path = "/getDecisionByAttributes", method = RequestMethod.POST)
  public PrimitiveResult<String> getDecisionByAttributes(
      @Valid @RequestBody EntitlementAttributesDTO attributes) throws ParsingException {
    return new PrimitiveResult<>(
        entitlementService.getDecisionByAttributes(attributes.getSubject(),
            attributes.getResource(), attributes.getAction(), attributes.getEnvironment()));
  }

  @RequestMapping(path = "/getBooleanDecision", method = RequestMethod.POST)
  public PrimitiveResult<Boolean> getBooleanDecision(
      @Valid @RequestBody EntitlementAttributesDTO attributes) throws ParsingException {
    return new PrimitiveResult<>(
        entitlementService.getBooleanDecision(attributes.getSubject(), attributes.getResource(),
            attributes.getAction(), attributes.getEnvironment()));
  }
}
