package com.hevelian.identity.users;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hevelian.identity.users.UserService.UsersNotFoundByNameException;
import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.model.User;
import com.hevelian.identity.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * This class handles the special cases of assign/unassign a signle role to multiple users.
 * 
 *
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MultiUsersRoleAssignManager {
  private final UserRepository userRepository;

  public void assignRoleToUsers(Set<String> newUserNames, Role role)
      throws UsersNotFoundByNameException {
    editRoleForUsers(newUserNames, role, u -> u.getRoles().add(role));
  }

  public void unassignRoleFromUsers(Set<String> removedUserNames, Role role)
      throws UsersNotFoundByNameException {
    editRoleForUsers(removedUserNames, role, u -> u.getRoles().remove(role));
  }

  protected void editRoleForUsers(Set<String> userNames, Role role, Consumer<User> editMethod)
      throws UsersNotFoundByNameException {
    Set<String> missingUserNames = new HashSet<>();
    // TODO find a way to do this using JPA
    userNames.forEach(n -> {
      User u = userRepository.findOneByName(n);
      if (u == null) {
        missingUserNames.add(n);
      } else {
        editMethod.accept(u);
        // Save one by one instead of collectiong all to same collection
        // and saving all at once. Maybe we will get too many names, who
        // knows..
        userRepository.save(u);
      }
    });

    if (!missingUserNames.isEmpty()) {
      throw new UsersNotFoundByNameException(missingUserNames);
    }
  }

}
