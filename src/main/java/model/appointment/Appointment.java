package model.appointment;

import java.util.ArrayList;
import model.role.PatientId;

public class Appointment {
  private final AppointmentId appointmentId;
  private final int capacity;
  private final ArrayList<PatientId> patientIds;

  public Appointment(AppointmentId appointmentId, int capacity ) {
    this.appointmentId = appointmentId;
    this.capacity = capacity;
    this.patientIds = new ArrayList<>();
  }

  public AppointmentId getAppointmentId() {
    return this.appointmentId;
  }

  public int getRemainingCapacity() {
    return this.capacity - patientIds.size();
  }

  public void addPatient(PatientId id) {
    if (patientIds.size() < capacity) {
      this.patientIds.add(id);
    }
  }
}
