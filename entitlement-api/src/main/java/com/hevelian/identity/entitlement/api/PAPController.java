package com.hevelian.identity.entitlement.api;

import com.hevelian.identity.core.api.PrimitiveResult;
import com.hevelian.identity.core.api.pagination.PageRequestParameters;
import com.hevelian.identity.core.api.pagination.PageRequestParametersReader;
import com.hevelian.identity.core.pagination.PageRequestBuilder;
import com.hevelian.identity.core.specification.EntitySpecificationsBuilder;
import com.hevelian.identity.entitlement.PAPService;
import com.hevelian.identity.entitlement.PAPService.PAPPoliciesNotFoundByPolicyIdsException;
import com.hevelian.identity.entitlement.PAPService.PAPPolicyNotFoundByPolicyIdException;
import com.hevelian.identity.entitlement.api.dto.PAPPolicyRequestDTO;
import com.hevelian.identity.entitlement.api.dto.PolicyIdDTO;
import com.hevelian.identity.entitlement.api.dto.PublishToPDPRequestDTO;
import com.hevelian.identity.entitlement.model.PolicyType;
import com.hevelian.identity.entitlement.model.pap.PAPPolicy;
import com.hevelian.identity.entitlement.model.pdp.PDPPolicy;
import com.hevelian.identity.entitlement.pdp.PolicyParsingException;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Set;

@RestController
@RequestMapping(path = "/PAPService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class PAPController {
  private final PAPService papService;

  @RequestMapping(path = "/addPolicy", method = RequestMethod.POST)
  public PrimitiveResult<String> addPolicy(
      @Valid @RequestBody PAPPolicyRequestDTO popPolicyRequestDTO) throws PolicyParsingException, PAPService.PAPPolicyAlreadyExistException {
    PAPPolicy policy = papService.addPolicy(popPolicyRequestDTO.getContent());
    return new PrimitiveResult<>(policy.getPolicyId());
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

  // TODO Maybe this method should not return content. It can be returned by
  // getPolicy or getPolicyContent
  @RequestMapping(path = "/getAllPolicies", method = RequestMethod.GET)
  public Page<PAPPolicy> getAllPolicies(@ApiParam(value = PageRequestParameters.PAGE_DESCRIPTION) @RequestParam(name = PageRequestParameters.PAGE, required = false) @Min(PageRequestParameters.PAGE_MIN) Integer page,
                                        @ApiParam(value = PageRequestParameters.SIZE_DESCRIPTION) @RequestParam(name = PageRequestParameters.SIZE, required = false) @Min(PageRequestParameters.SIZE_MIN) Integer size,
                                        @ApiParam(value = PageRequestParameters.SORT_DESCRIPTION) @RequestParam(name = PageRequestParameters.SORT, required = false) String sort,
                                        @ApiParam(value = "Policy id") @RequestParam(required = false) Integer policyId,
                                        @ApiParam(value = "Policy type") @RequestParam(required = false) PolicyType type) {
    PageRequestBuilder pageRequestBuilder = new PageRequestParametersReader().readParameters(page, size, sort);
    EntitySpecificationsBuilder<PAPPolicy> spec = new EntitySpecificationsBuilder<>();
    spec.with(PAPPolicy.FIELD_POLICY_ID, policyId);
    spec.with(PAPPolicy.FIELD_POLICY_TYPE, type);
    return papService.searchPolicies(spec.build(), pageRequestBuilder.build());
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
