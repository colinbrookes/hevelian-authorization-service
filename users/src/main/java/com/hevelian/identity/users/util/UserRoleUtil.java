package com.hevelian.identity.users.util;

import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.Sets;
import com.hevelian.identity.users.model.Role;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserRoleUtil {
  public Set<String> getMissingRoleNames(Set<String> roleNamesToCheck, Set<Role> roles) {
    return Sets.difference(roleNamesToCheck,
        roles.stream().map(r -> r.getName()).collect(Collectors.toSet()));
  }

  public Set<String> rolesToNames(Set<Role> roles) {
    return roles.stream().map(r -> r.getName()).collect(Collectors.toSet());
  }

  public Set<Role> namesToRoles(Set<String> roleNames) {
    return roleNames.stream().map(n -> {
      Role r = new Role();
      r.setName(n);
      return r;
    }).collect(Collectors.toSet());
  }
}
