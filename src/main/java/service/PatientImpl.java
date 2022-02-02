package service;

import java.rmi.RemoteException;
import java.util.List;

import api.Patient;
import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.PatientId;

public class PatientImpl implements Patient {
  private static PatientImpl instance;

  private PatientImpl(){}

  public static synchronized PatientImpl getInstance() {
    if (instance == null) {
      instance = new PatientImpl();
    }
    return instance;
  }

  @Override
  public synchronized void bookAppointment(
      PatientId patientId, AppointmentId appointmentId, AppointmentType type)
      throws RemoteException {

  }

  @Override
  public List<Appointment> getAppointmentSchedule(PatientId patientId) throws RemoteException {
    return null;
  }

  @Override
  public synchronized void cancelAppointment(PatientId patientId, AppointmentId appointmentId)
      throws RemoteException {}
}
