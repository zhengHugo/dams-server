package service;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
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
  public synchronized void addAppointment(
      AppointmentId appointmentId, AppointmentType appointmentType, int capacity)
      throws RemoteException {
    database.add(appointmentId, appointmentType, capacity);
    try {
      System.out.println(RemoteServer.getClientHost());
    } catch (ServerNotActiveException e) {
      e.printStackTrace();
    }
  }

  @Override
  public synchronized void removeAppointment(
      AppointmentId appointmentId, AppointmentType appointmentType) throws RemoteException {}

  @Override
  public List<Appointment> listAppointmentAvailability(AppointmentType appointmentType)
      throws RemoteException {
    return null;
  }

  @Override
  public synchronized void bookAppointment(
      PatientId patientId, AppointmentId appointmentId, AppointmentType type)
      throws RemoteException {
    this.patient.bookAppointment(patientId, appointmentId, type);
  }

  @Override
  public List<Appointment> getAppointmentSchedule(PatientId patientId) throws RemoteException {
    return this.patient.getAppointmentSchedule(patientId);
  }

  @Override
  public synchronized void cancelAppointment(PatientId patientId, AppointmentId appointmentId)
      throws RemoteException {
    this.patient.cancelAppointment(patientId, appointmentId);
  }
}
