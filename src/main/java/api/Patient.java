package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.PatientId;

public interface Patient extends Remote {
  void bookAppointment(PatientId patientId, AppointmentId appointmentId, AppointmentType type)
      throws RemoteException;

  List<Appointment> getAppointmentSchedule(PatientId patientId) throws RemoteException;

  void cancelAppointment(PatientId patientId, AppointmentId appointmentId) throws RemoteException;
}
