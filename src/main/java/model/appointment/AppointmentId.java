package model.appointment;

import java.io.Serializable;

import model.common.City;

public class AppointmentId implements Serializable, Comparable<AppointmentId> {
  private final City city;
  private final AppointmentTime time;
  private final String dateString;

  public AppointmentId(City city, AppointmentTime time, String dateString) {
    this.city = city;
    this.time = time;
    this.dateString = dateString;
  }

  public String getId() {
    return city.code + time.code + dateString;
  }

  public City getCity() {
    return city;
  }

  public String getDateString() {
    return dateString;
  }

  @Override
  public int compareTo(AppointmentId anotherId) {
    var thisDateNumbers = this.splitDateString();
    var otherDateNumbers = anotherId.splitDateString();
    if (thisDateNumbers[2] == otherDateNumbers[2]) {
      // same year
      if (thisDateNumbers[1] == otherDateNumbers[1]) {
        // same month
        if (thisDateNumbers[0] == otherDateNumbers[0]) {
          // same date
          return this.time.compareTo(anotherId.time);
        } else {
          return thisDateNumbers[0] - otherDateNumbers[0];
        }
      } else {
        return thisDateNumbers[1] - otherDateNumbers[1];

      }
    } else {
      return thisDateNumbers[2] - otherDateNumbers[2];
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AppointmentId that = (AppointmentId) o;

    return this.getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return this.getId().hashCode();
  }

  @Override
  public String toString() {
    return this.getId();
  }

  private int[] splitDateString() {
    int date = Integer.parseInt(dateString.substring(0, 2));
    int month = Integer.parseInt(dateString.substring(2, 4));
    int year = Integer.parseInt(dateString.substring(4, 8));
    return new int[] {date, month, year};
  }
}
