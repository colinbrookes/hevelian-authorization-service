package com.hevelian.identity.users.stores.ldap;

import java.util.List;
import javax.naming.directory.Attributes;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQueryBuilder;
import com.hevelian.identity.users.stores.UserStoreClient;
import com.hevelian.identity.users.stores.UserStoreException;

public class LDAPUserStoreClient implements UserStoreClient<LDAPUserStoreClientConfig> {
  private LdapTemplate ldapTemplate;

  public LDAPUserStoreClient() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void setup(LDAPUserStoreClientConfig userStoreClientConfig) throws UserStoreException {

    LdapContextSource contextSource = new LdapContextSource();

    contextSource.setUrl(userStoreClientConfig.getConnectionURL());
    contextSource.setUserDn(userStoreClientConfig.getConnectionName());
    contextSource.setPassword(userStoreClientConfig.getConnectionPassword());
    contextSource.setBase(userStoreClientConfig.getUserSearchBase());

    contextSource.afterPropertiesSet();

    ldapTemplate = new LdapTemplate(contextSource);
    try {
      ldapTemplate.afterPropertiesSet();
    } catch (Exception e) {
      e.printStackTrace();
    }
    ldapTemplate.authenticate(LdapQueryBuilder.query().where("objectClass").is(userStoreClientConfig.getUserEntryObjectClass()).and(userStoreClientConfig.getUsernameAttribute()).is("Flyud"), "pass123");
    
    List<Attributes> result = ldapTemplate.search(
        LdapQueryBuilder.query().filter(userStoreClientConfig.getUserListFilter()),
        (Attributes atts) -> atts);
    
    System.out.println(result.size());
  }

  public static void main(String[] args) throws UserStoreException {
    LDAPUserStoreClientConfig c = new LDAPUserStoreClientConfig();
    c.setConnectionURL("ldap://localhost:10389");
    c.setUserSearchBase("ou=Dev,o=Hevelian,dc=hevelian,dc=com");
    c.setConnectionName("cn=Yuriy Flyud,ou=Dev,o=Hevelian,dc=hevelian,dc=com");
    c.setConnectionPassword("pass123");
    
    c.setUserEntryObjectClass("person");
    c.setUsernameAttribute("sn");
    
    
    c.setUserListFilter("(objectClass=person)");
    new LDAPUserStoreClient().setup(c);
  }

  @Override
  public void tearDown() throws UserStoreException {
    // TODO Auto-generated method stub

  }

  @Override
  public String[] listUsers() throws UserStoreException {
    // TODO Auto-generated method stub
    return null;
  }
}
