package com.hevelian.identity.entitlement.api;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Iterables;
import com.hevelian.identity.entitlement.PDPService;
import com.hevelian.identity.entitlement.PDPService.PDPPolicyNotFoundByPolicyIdException;
import com.hevelian.identity.entitlement.api.dto.EnableDisablePolicyRequestDTO;
import com.hevelian.identity.entitlement.api.dto.OrderPolicyRequestDTO;
import com.hevelian.identity.entitlement.api.dto.PolicyIdDTO;
import com.hevelian.identity.entitlement.model.pdp.PDPPolicy;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/PDPService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PDPController {
  private final PDPService pdpService;

  @RequestMapping(path = "/deletePolicy", method = RequestMethod.POST)
  public void deletePolicy(@Valid @RequestBody PolicyIdDTO pdpPolicyIdDTO)
      throws PDPPolicyNotFoundByPolicyIdException {
    pdpService.deletePolicy(pdpPolicyIdDTO.getPolicyId());
  }

  @RequestMapping(path = "/enableDisablePolicy", method = RequestMethod.POST)
  public void enableDisablePolicy(
      @Valid @RequestBody EnableDisablePolicyRequestDTO enableDisablePolicyRequestDTO)
      throws PDPPolicyNotFoundByPolicyIdException {
    pdpService.enableDisablePolicy(enableDisablePolicyRequestDTO.getPolicyId(),
        enableDisablePolicyRequestDTO.getEnable());
  }

  @RequestMapping(path = "/orderPolicy", method = RequestMethod.POST)
  public void orderPolicy(@Valid @RequestBody OrderPolicyRequestDTO orderPolicyRequestDTO)
      throws PDPPolicyNotFoundByPolicyIdException {
    pdpService.orderPolicy(orderPolicyRequestDTO.getPolicyId(), orderPolicyRequestDTO.getOrder());
  }

  @RequestMapping(path = "/getPolicy", method = RequestMethod.POST)
  public PDPPolicy getPolicy(@Valid @RequestBody PolicyIdDTO policyIdDTO)
      throws PDPPolicyNotFoundByPolicyIdException {
    return pdpService.getPolicy(policyIdDTO.getPolicyId());
  }

  // TODO pagination. Also maybe this method should not return content. It can be returned by
  // getPolicy or getPolicyContent
  @RequestMapping(path = "/getAllPolicies", method = RequestMethod.GET)
  public PDPPolicy[] getAllPolicies() {
    return Iterables.toArray(pdpService.getAllPolicies(), PDPPolicy.class);
  }
}
