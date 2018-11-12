package com.hevelian.identity.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.base.Preconditions;
import com.hevelian.identity.core.SystemRoles;
import com.hevelian.identity.users.model.UserStore;
import com.hevelian.identity.users.repository.UserStoreRepository;
import lombok.RequiredArgsConstructor;

@Service
// Manage all transactions in service layer, where business logic occurs.
@Transactional(readOnly = true)
@Secured(value = SystemRoles.TENANT_ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserStoreService {
  private final UserStoreRepository userStoreRepository;

  @Transactional(readOnly = false)
  public UserStore addUserStore(UserStore userStore) {
    Preconditions.checkArgument(userStore.getId() == null);
    return userStoreRepository.save(userStore);
  }

  public Iterable<UserStore> findAll() {
    return userStoreRepository.findAll();
  }
}
