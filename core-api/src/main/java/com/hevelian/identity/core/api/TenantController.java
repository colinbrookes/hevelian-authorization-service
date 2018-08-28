package com.hevelian.identity.core.api;

import com.hevelian.identity.core.TenantService;
import com.hevelian.identity.core.TenantService.TenantActiveAlreadyInStateException;
import com.hevelian.identity.core.TenantService.TenantHasNoLogoException;
import com.hevelian.identity.core.TenantService.TenantNotFoundByDomainException;
import com.hevelian.identity.core.api.dto.TenantAdminRequestDTO.NewTenantGroup;
import com.hevelian.identity.core.api.dto.TenantDomainDTO;
import com.hevelian.identity.core.api.dto.TenantRequestDTO;
import com.hevelian.identity.core.api.pagination.PageRequestParameters;
import com.hevelian.identity.core.api.pagination.PageRequestParametersReader;
import com.hevelian.identity.core.api.validation.constraints.Logo;
import com.hevelian.identity.core.model.Tenant;
import com.hevelian.identity.core.pagination.PageRequestBuilder;
import com.hevelian.identity.core.specification.EntitySpecificationsBuilder;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.groups.Default;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.hevelian.identity.core.model.Tenant.FIELD_DATE_CREATED;
import static com.hevelian.identity.core.specification.EntitySpecification.FROM;
import static com.hevelian.identity.core.specification.EntitySpecification.TO;

@RestController
@RequestMapping(path = "/TenantService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class TenantController {
  private final String domain = "Tenant domain";
  private final TenantService tenantService;
  private final PasswordEncoder passwordEncoder;


  @RequestMapping(path = "/activateTenant", method = RequestMethod.POST)
  public void activateTenant(@Valid @RequestBody TenantDomainDTO tenantDomainDTO)
      throws TenantNotFoundByDomainException, TenantActiveAlreadyInStateException {
    tenantService.activateTenant(tenantDomainDTO.getTenantDomain());
  }

  @RequestMapping(path = "/deactivateTenant", method = RequestMethod.POST)
  public void deactivateTenant(@Valid @RequestBody TenantDomainDTO tenantDomainDTO)
      throws TenantNotFoundByDomainException, TenantActiveAlreadyInStateException {
    tenantService.deactivateTenant(tenantDomainDTO.getTenantDomain());
  }

  @RequestMapping(path = "/addTenant", method = RequestMethod.POST)
  public PrimitiveResult<String> addTenant(@Validated(value = {Default.class, NewTenantGroup.class})
                                           @RequestBody TenantRequestDTO tenant) throws TenantService.TenantDomainAlreadyExistsException {
    tenant.getTenantAdmin()
        .setPassword(passwordEncoder.encode(tenant.getTenantAdmin().getPassword()));
    return new PrimitiveResult<>(
        tenantService.addTenant(tenant.toEntity(), tenant.getTenantAdmin().toEntity()).getDomain());
  }

  @RequestMapping(path = "/setTenantLogo", method = RequestMethod.POST)
  public void setTenantLogo(@ApiParam(value = domain, required = true) @RequestParam String tenantDomain,
                            @ApiParam(value = "Tenant logo", required = true) @Logo @RequestParam MultipartFile logo)
      throws TenantNotFoundByDomainException, IOException {
    tenantService.addTenantLogo(tenantDomain, logo.getBytes());
  }

  @RequestMapping(path = "/getTenantLogo", method = RequestMethod.GET,
      produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
  public BufferedImage getTenantLogo(@ApiParam(value = domain, required = true) @RequestParam String tenantDomain)
      throws TenantNotFoundByDomainException, TenantHasNoLogoException {
    return tenantService.getTenantLogo(tenantDomain);
  }

  @RequestMapping(path = "/updateTenant", method = RequestMethod.POST)
  public void updateTenant(@Valid @RequestBody TenantRequestDTO tenant)
      throws TenantNotFoundByDomainException {
    if (tenant.getTenantAdmin().getPassword() != null)
      tenant.getTenantAdmin()
          .setPassword(passwordEncoder.encode(tenant.getTenantAdmin().getPassword()));
    tenantService.updateTenant(tenant.toEntity(), tenant.getTenantAdmin().toEntity());
  }

  @RequestMapping(path = "/deleteTenant", method = RequestMethod.POST)
  public void deleteTenant(@Valid @RequestBody TenantDomainDTO tenantDomainDTO)
      throws TenantNotFoundByDomainException {
    tenantService.deleteTenant(tenantDomainDTO.getTenantDomain());
  }

  @RequestMapping(path = "/getTenant", method = RequestMethod.POST)
  public Tenant getTenant(@Valid @RequestBody TenantDomainDTO tenantDomainDTO)
      throws TenantNotFoundByDomainException {
    return tenantService.getTenant(tenantDomainDTO.getTenantDomain());
  }

  @RequestMapping(path = "/getAllTenants", method = RequestMethod.GET)
  public Page<Tenant> getAllTenants(@ApiParam(value = PageRequestParameters.PAGE_DESCRIPTION,defaultValue = PageRequestParameters.DEFAULT_PAGE) @RequestParam(name = PageRequestParameters.PAGE, required = false) @Min(PageRequestParameters.PAGE_MIN) Integer page,
                                    @ApiParam(value = PageRequestParameters.SIZE_DESCRIPTION,defaultValue = PageRequestParameters.DEFAULT_SIZE) @RequestParam(name = PageRequestParameters.SIZE, required = false) @Min(PageRequestParameters.SIZE_MIN) Integer size,
                                    @ApiParam(value = PageRequestParameters.SORT_DESCRIPTION) @RequestParam(name = PageRequestParameters.SORT, required = false) String sort,
                                    @ApiParam(value = "Domain") @RequestParam(required = false) String domain,
                                    @ApiParam(value = "Tenant is active") @RequestParam(required = false) Boolean active,
                                    @ApiParam(value = "Date created from (including entered time).API uses UTC time zone. Format: yyyy-MM-dd'T'HH:mm:ss.SSS")
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                    @ApiParam(value = "Date created to (excluding entered time).API uses UTC time zone. Format: yyyy-MM-dd'T'HH:mm:ss.SSS")
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
    PageRequestBuilder pageRequestBuilder = new PageRequestParametersReader().readParameters(page, size, sort);
    EntitySpecificationsBuilder<Tenant> builder = new EntitySpecificationsBuilder<>();
    builder.with(Tenant.FIELD_DOMAIN, domain)
        .with(Tenant.FIELD_ACTIVE, active)
        .with(FIELD_DATE_CREATED + FROM, dateFrom)
        .with(FIELD_DATE_CREATED + TO, dateTo);
    return tenantService.searchTenants(builder.build(), pageRequestBuilder.build());
  }
}
