package com.hevelian.identity.users.stores;

public interface UserStoreClient<T extends UserStoreClientConfig> {
  void setup(T clientConfig) throws UserStoreException;

  void tearDown() throws UserStoreException;

  /**
   * Retrieve a list of users
   * 
   * @return An array of user names
   * @throws UserStoreException in case user store interaction fails.
   */
  String[] listUsers() throws UserStoreException;
}
