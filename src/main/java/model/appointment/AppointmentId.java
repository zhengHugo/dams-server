package model.appointment;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.common.City;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AppointmentId implements Serializable, Comparable<AppointmentId> {
  private final City city;
  private final AppointmentTime time;
  private final LocalDate date;
  private final DateTimeFormatter formatter = DateTimeFormat.forPattern("ddMMyyyy");

  public AppointmentId(City city, AppointmentTime time, String dateString) {
    this.city = city;
    this.time = time;
    this.date = LocalDate.parse(dateString, formatter);
  }

  public String getId() {
    return city.code + time.code + date.toString(formatter);
  }

  public City getCity() {
    return city;
  }

  public LocalDate getDate() {
    return date;
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

  @Override
  public String toString() {
    return this.getId();
  }
}
