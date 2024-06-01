package com.thuctap.fastfood.entities.stringId;

import com.thuctap.fastfood.controllers.TestController;
import com.thuctap.fastfood.entities.Staff;
import java.util.List;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class StaffIdGenerator implements IdentifierGenerator {

  private static final String PREFIX = "NV";

  @Override
  public Object generate(SharedSessionContractImplementor session, Object object) {
    return PREFIX + generateUniqueId();
  }

  private String generateUniqueId() {
    List<Staff> list = TestController.listStaffs();
    Integer currentId;
    try {
      Staff staff = list.get(list.size() - 1);
      currentId = Integer.parseInt(staff.getId().substring(2));

    } catch (IndexOutOfBoundsException ex) {
      currentId = 0;
    }
    return String.valueOf(currentId + 1);
  }
}
