package model.appointment;

import java.util.ArrayList;
import java.util.Collection;
import model.role.PatientId;

public class Appointment {
  private final AppointmentId appointmentId;
  private final int capacity;
  private final ArrayList<PatientId> patientIds;

  public Appointment(AppointmentId appointmentId, int capacity) {
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

  public ArrayList<PatientId> getPatientIds() {
    return patientIds;
  }

  public synchronized void addPatient(PatientId id) {
    if (patientIds.size() < capacity) {
      this.patientIds.add(id);
    }
  }

  public synchronized void addPatients(Collection<PatientId> ids) {
    if (patientIds.size() + ids.size() <= capacity) {
      patientIds.addAll(ids);
    } else {
      throw new IllegalArgumentException("Number of new patients exceeds the limit.");
    }
  }
}
