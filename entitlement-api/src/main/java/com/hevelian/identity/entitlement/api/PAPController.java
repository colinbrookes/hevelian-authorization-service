package com.hevelian.identity.entitlement.api;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Iterables;
import com.hevelian.identity.entitlement.PAPService;
import com.hevelian.identity.entitlement.PAPService.PolicyNotFoundByPolicyIdException;
import com.hevelian.identity.entitlement.api.dto.PAPPolicyRequestDTO;
import com.hevelian.identity.entitlement.model.pap.PAPPolicy;
import com.hevelian.identity.entitlement.pdp.PolicyParsingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/PAPService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PAPController {
  private final PAPService papService;

  @RequestMapping(path = "/addPolicy", method = RequestMethod.POST)
  public void addPolicy(@Valid @RequestBody PAPPolicyRequestDTO popPolicyRequestDTO)
      throws PolicyParsingException {
    papService.addPolicy(popPolicyRequestDTO.getContent());
  }

  @RequestMapping(path = "/updatePolicy", method = RequestMethod.POST)
  public void updatePolicy(@Valid @RequestBody PAPPolicyRequestDTO popPolicyRequestDTO)
      throws PolicyParsingException, PolicyNotFoundByPolicyIdException {
    papService.updatePolicy(popPolicyRequestDTO.getContent());
  }

  @RequestMapping(path = "/getAllPolicies", method = RequestMethod.GET)
  public PAPPolicy[] getAllPolicies() {
    return Iterables.toArray(papService.getAllPolicies(), PAPPolicy.class);
  }
}
