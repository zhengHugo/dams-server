package model.role;

import model.common.City;
import model.common.ClientId;

public class PatientId implements ClientId {
  private final City city;
  private final int number;

  public PatientId(City city, int number) {
    this.city = city;
    this.number = number;
  }

  public String getId() {
    return city.code + "P" + number;
  }

  @Override
  public City getCity() {
    return this.city;
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

    PatientId patientId = (PatientId) o;

    return this.getId().equals(patientId.getId());
  }

  @Override
  public int hashCode() {
    return this.getId().hashCode();
  }
}
