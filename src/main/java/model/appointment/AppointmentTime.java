package model.appointment;

public enum AppointmentTime {
  Morning("M"),
  Afternoon("A"),
  Evening("E"),
  ;

  public final String code;

  AppointmentTime(String code) {
    this.code = code;
  }
}
