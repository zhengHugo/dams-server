package model.role;

import model.common.City;
import model.common.ClientId;

public class AdminId implements ClientId {
  private final City city;
  private final int code;

  public AdminId(City city, int code) {
    this.city = city;
    this.code = code;
  }

  public String getId() {
    return city.code + "A" + code;
  }

  public City getCity() {
    return city;
  }

  @Override
  public String toString() {
    return this.getId();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AdminId adminId = (AdminId) o;

    return this.getId().equals(adminId.getId());
  }

  @Override
  public int hashCode() {
    return this.getId().hashCode();
  }
}
