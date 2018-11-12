package com.hevelian.identity.users.stores.ldap;

import com.hevelian.identity.users.stores.UserStoreClientConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LDAPUserStoreClientConfig extends UserStoreClientConfig {
  private String connectionURL;
  private String connectionName;
  private String connectionPassword;
  private String userSearchBase;
  private String userEntryObjectClass;
  private String usernameAttribute;
  private String userSearchFilter;
  private String userListFilter;
}
