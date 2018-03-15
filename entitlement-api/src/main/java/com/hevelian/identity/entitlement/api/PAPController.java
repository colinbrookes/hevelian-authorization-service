package com.hevelian.identity.entitlement.api;

import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Iterables;
import com.hevelian.identity.core.api.PrimitiveResult;
import com.hevelian.identity.entitlement.PAPService;
import com.hevelian.identity.entitlement.PAPService.PAPPoliciesNotFoundByPolicyIdsException;
import com.hevelian.identity.entitlement.PAPService.PAPPolicyNotFoundByPolicyIdException;
import com.hevelian.identity.entitlement.api.dto.PolicyIdDTO;
import com.hevelian.identity.entitlement.api.dto.PAPPolicyRequestDTO;
import com.hevelian.identity.entitlement.api.dto.PublishToPDPRequestDTO;
import com.hevelian.identity.entitlement.model.pap.PAPPolicy;
import com.hevelian.identity.entitlement.model.pdp.PDPPolicy;
import com.hevelian.identity.entitlement.pdp.PolicyParsingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/PAPService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PAPController {
  private final PAPService papService;

  @RequestMapping(path = "/addPolicy", method = RequestMethod.POST)
  public PrimitiveResult<String> addPolicy(
      @Valid @RequestBody PAPPolicyRequestDTO popPolicyRequestDTO) throws PolicyParsingException {
    PAPPolicy policy = papService.addPolicy(popPolicyRequestDTO.getContent());
    return new PrimitiveResult<String>(policy.getPolicyId());
  }

  @RequestMapping(path = "/updatePolicy", method = RequestMethod.POST)
  public void updatePolicy(@Valid @RequestBody PAPPolicyRequestDTO popPolicyRequestDTO)
      throws PolicyParsingException {
    papService.updatePolicy(popPolicyRequestDTO.getContent());
  }

  @RequestMapping(path = "/deletePolicy", method = RequestMethod.POST)
  public void deletePolicy(@Valid @RequestBody PolicyIdDTO papPolicyIdDTO)
      throws PAPPolicyNotFoundByPolicyIdException {
    papService.deletePolicy(papPolicyIdDTO.getPolicyId());
  }

  @RequestMapping(path = "/getPolicy", method = RequestMethod.POST)
  public PAPPolicy getPolicy(@Valid @RequestBody PolicyIdDTO papPolicyIdDTO)
      throws PAPPolicyNotFoundByPolicyIdException {
    return papService.getPolicy(papPolicyIdDTO.getPolicyId());
  }

  // TODO pagination. Also maybe this method should not return content. It can be returned by
  // getPolicy or getPolicyContent
  @RequestMapping(path = "/getAllPolicies", method = RequestMethod.GET)
  public PAPPolicy[] getAllPolicies() {
    return Iterables.toArray(papService.getAllPolicies(), PAPPolicy.class);
  }

  @RequestMapping(path = "/publishToPDP", method = RequestMethod.POST)
  public String[] publishToPDP(@Valid @RequestBody PublishToPDPRequestDTO publishToPDPRequestDTO)
      throws PAPPoliciesNotFoundByPolicyIdsException {
    Set<PDPPolicy> publishedPolicies =
        papService.publishToPDP(publishToPDPRequestDTO.getPolicyIds(),
            publishToPDPRequestDTO.getEnabled(), publishToPDPRequestDTO.getOrder());
    return publishedPolicies.stream().map(p -> p.getPolicyId()).toArray(String[]::new);
  }
}
