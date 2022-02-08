package model.appointment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import model.role.PatientId;

public class Appointment implements Serializable {
  private final AppointmentId appointmentId;
  private final int capacity;
  private final ArrayList<PatientId> patientIds;
  private final AppointmentType type;

  public Appointment(AppointmentId appointmentId, int capacity, AppointmentType appointmentType) {
    this.appointmentId = appointmentId;
    this.capacity = capacity;
    this.patientIds = new ArrayList<>();
    this.type = appointmentType;
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

  public AppointmentType getType() {
    return type;
  }

  public synchronized boolean addPatient(PatientId id) {
    if (patientIds.size() < capacity) {
      this.patientIds.add(id);
      return true;
    }
    return false;
  }

  public synchronized void addPatients(Collection<PatientId> ids) {
    if (patientIds.size() + ids.size() <= capacity) {
      patientIds.addAll(ids);
    } else {
      throw new IllegalArgumentException("Number of new patients exceeds the limit.");
    }
  }

  public synchronized boolean removePatient(PatientId id) {
    return this.patientIds.remove(id);
  }
}
