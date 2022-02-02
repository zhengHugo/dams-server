package model.appointment;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.common.City;

public class AppointmentId implements Serializable, Comparable<AppointmentId> {
  private final City city;
  private final AppointmentTime time;
  private final Date date;
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");

  public AppointmentId(City city, AppointmentTime time, String dateString) throws ParseException {
    this.city = city;
    this.time = time;
    this.date = dateFormat.parse(dateString);
  }

  public String getId() {
    return city.code + time.code + dateFormat.format(date);
  }

  @Override
  public int compareTo(AppointmentId anotherId) {
    if (this.date.compareTo(anotherId.date) == 0) {
      return this.time.compareTo(anotherId.time);
    }
    return this.date.compareTo(anotherId.date);
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
}
