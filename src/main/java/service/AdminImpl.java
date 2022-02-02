package service;

import java.rmi.RemoteException;
import java.util.List;

import api.Admin;
import database.Database;
import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.PatientId;

public class AdminImpl implements Admin {

  private final Database database = Database.getInstance();
  private final PatientImpl patient = PatientImpl.getInstance();

  private static AdminImpl instance;

  private AdminImpl() {}

  public static synchronized AdminImpl getInstance() {
    if (instance == null) {
      instance = new AdminImpl();
    }
    return instance;
  }

  @Override
  public synchronized boolean addAppointment(
      AppointmentId appointmentId, AppointmentType appointmentType, int capacity)
      throws RemoteException {
    if (database.findByTypeAndId(appointmentType, appointmentId).isEmpty()) {
      database.add(appointmentId, appointmentType, capacity);
    }
    return true;
  }

  @Override
  public synchronized boolean removeAppointment(
      AppointmentId appointmentId, AppointmentType appointmentType) throws RemoteException {
    return false;
  }

  @Override
  public List<Appointment> listAppointmentAvailability(AppointmentType appointmentType)
      throws RemoteException {
    return null;
  }

  @Override
  public synchronized boolean bookAppointment(
      PatientId patientId, AppointmentId appointmentId, AppointmentType type)
      throws RemoteException {
    return this.patient.bookAppointment(patientId, appointmentId, type);
  }

  @Override
  public List<Appointment> getAppointmentSchedule(PatientId patientId) throws RemoteException {
    return this.patient.getAppointmentSchedule(patientId);
  }

  @Override
  public synchronized boolean cancelAppointment(PatientId patientId, AppointmentId appointmentId)
      throws RemoteException {
    return this.patient.cancelAppointment(patientId, appointmentId);
  }
}
