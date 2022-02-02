package model.appointment;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.common.City;

public class AppointmentId implements Serializable {
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
}
