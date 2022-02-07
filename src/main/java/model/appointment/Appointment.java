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

  public synchronized ArrayList<PatientId> addPatient(PatientId id) {
    if (patientIds.size() < capacity) {
      this.patientIds.add(id);
    }
    return patientIds;
  }

  public synchronized void addPatients(Collection<PatientId> ids) {
    if (patientIds.size() + ids.size() <= capacity) {
      patientIds.addAll(ids);
    } else {
      throw new IllegalArgumentException("Number of new patients exceeds the limit.");
    }
  }

  public synchronized void removePatient(PatientId id) {
    if (patientIds.size() > 0) {
      this.patientIds.remove(id);
    } else {
      throw new IllegalArgumentException("No patient can be removed");
    }
  }
}
