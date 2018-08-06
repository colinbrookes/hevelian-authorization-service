package com.hevelian.identity.core.api;

import com.google.common.collect.Iterables;
import com.hevelian.identity.core.TenantService;
import com.hevelian.identity.core.TenantService.TenantActiveAlreadyInStateException;
import com.hevelian.identity.core.TenantService.TenantNotFoundByDomainException;
import com.hevelian.identity.core.api.dto.TenantAdminRequestDTO.NewTenantGroup;
import com.hevelian.identity.core.api.dto.TenantDomainDTO;
import com.hevelian.identity.core.api.dto.TenantRequestDTO;
import com.hevelian.identity.core.api.validation.constraints.Logo;
import com.hevelian.identity.core.model.Tenant;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping(path = "/TenantService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class TenantController {
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
  public PrimitiveResult<String> addTenant(@Validated(
      value = {Default.class, NewTenantGroup.class}) @RequestBody TenantRequestDTO tenant) {
    tenant.getTenantAdmin()
        .setPassword(passwordEncoder.encode(tenant.getTenantAdmin().getPassword()));
    return new PrimitiveResult<>(
        tenantService.addTenant(tenant.toEntity(), tenant.getTenantAdmin().toEntity()).getDomain());
  }

  @RequestMapping(path = "/setTenantLogo", method = RequestMethod.POST)
  public void setTenantLogo(@ApiParam(value = "Tenant domain", required = true) @RequestParam String tenantDomain,
                            @ApiParam(value = "Tenant logo", required = true) @Logo @RequestParam MultipartFile file)
      throws TenantNotFoundByDomainException,IOException {
    tenantService.addTenantLogo(tenantDomain, file.getBytes());
  }

  @RequestMapping(path = "/getTenantLogo", method = RequestMethod.GET,
      produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
  public BufferedImage getTenantLogo(@ApiParam(value = "Tenant domain", required = true) @RequestParam(name = "tenantDomain") String tenantDomain)
      throws TenantNotFoundByDomainException {
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
  public Tenant[] getAllTenants() {
    return Iterables.toArray(tenantService.getAllTenants(), Tenant.class);
  }
}
