package model.appointment;

public class Appointment {
  private AppointmentId appointmentId;
  private int capacity;

  public Appointment(AppointmentId appointmentId, int capacity) {
    this.appointmentId = appointmentId;
    this.capacity = capacity;
  }

  public AppointmentId getAppointmentId(){
    return this.appointmentId;
  }

  public int getCapacity(){
    return this.capacity;
  }
}
