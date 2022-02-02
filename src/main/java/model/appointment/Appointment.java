package model.appointment;

public class Appointment {
  private final AppointmentId appointmentId;
  private final int capacity;

  public Appointment(AppointmentId appointmentId, int capacity) {
    this.appointmentId = appointmentId;
    this.capacity = capacity;
  }

  public AppointmentId getAppointmentId() {
    return this.appointmentId;
  }

  public int getCapacity() {
    return this.capacity;
  }
}
