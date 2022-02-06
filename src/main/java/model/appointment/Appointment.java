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

  public Appointment(AppointmentId appointmentId, int capacity, ArrayList<PatientId> patientIds) {
    this.appointmentId = appointmentId;
    this.capacity = capacity;
    this.patientIds = patientIds;
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

  public synchronized ArrayList<PatientId> removePatient(PatientId id) {
    if (patientIds.size() < capacity) {
      this.patientIds.remove(id);
    }
    return patientIds;
  }
}
