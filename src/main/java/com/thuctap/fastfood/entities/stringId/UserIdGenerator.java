package com.thuctap.fastfood.entities.stringId;

import com.thuctap.fastfood.controllers.TestController;
import com.thuctap.fastfood.entities.User;
import java.util.List;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class UserIdGenerator implements IdentifierGenerator {
  private static final String PREFIX = "KH";

  @Override
  public Object generate(SharedSessionContractImplementor arg0, Object arg1) {
    return PREFIX + generateUniqueId();
  }

  private String generateUniqueId() {
    List<User> list = TestController.listUsers();
    Integer currentId;
    try {
      User user = list.get(list.size() - 1);
      currentId = Integer.parseInt(user.getId().substring(2));

    } catch (IndexOutOfBoundsException ex) {
      currentId = 0;
    }
    return String.valueOf(currentId + 1);
  }
}
