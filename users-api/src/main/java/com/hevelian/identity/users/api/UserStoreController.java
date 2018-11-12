package com.hevelian.identity.users.api;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.hevelian.identity.core.api.PrimitiveResult;
import com.hevelian.identity.users.UserStoreService;
import com.hevelian.identity.users.api.dto.UserStoreRequestDTO;
import com.hevelian.identity.users.model.UserStore;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/UserStoreService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserStoreController {

  private final UserStoreService userStoreService;

  @RequestMapping(path = "/addUserStore", method = RequestMethod.POST)
  public PrimitiveResult<String> addUserStore(@Valid @RequestBody UserStoreRequestDTO userStore) {
    return new PrimitiveResult<String>(
        userStoreService.addUserStore(userStore.toEntity()).getDomain());
  }

  @RequestMapping(path = "/list", method = RequestMethod.GET)
  public Iterable<UserStore> list() {
    return userStoreService.findAll();
  }
}
