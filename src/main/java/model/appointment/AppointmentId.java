package model.appointment;

import java.io.Serializable;

public class AppointmentId implements Serializable {
  private String id;

  public AppointmentId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
