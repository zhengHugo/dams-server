package model.role;

import model.City;
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
}
