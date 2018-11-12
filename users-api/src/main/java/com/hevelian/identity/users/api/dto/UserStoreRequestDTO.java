package com.hevelian.identity.users.api.dto;

import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;
import com.hevelian.identity.core.api.dto.EntityDTO;
import com.hevelian.identity.users.model.UserStore;

public class UserStoreRequestDTO extends UserStore implements EntityDTO<UserStore> {
  @Override
  public UserStore toEntity() {
    UserStore us = new UserStore();
    us.setDomain(getDomain());
    us.setDescription(getDescription());
    us.setClassName(getClassName());
    us.setEnabled(getEnabled() == null ? true : false);
    us.setProperties(getProperties());
    return us;
  }

  @NotEmpty
  @Pattern(regexp = "\\w*",
      message = "must consist only of word characters (any lowercase letter, any uppercase letter, the underscore character, or any digit)")
  @Override
  public String getDomain() {
    return super.getDomain();
  }

  @NotEmpty
  // TODO maybe do additional validation for class name format and availability.
  @Override
  public String getClassName() {
    return super.getClassName();
  }
}
