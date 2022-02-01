package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;

public interface Admin extends Remote {
  public void addAppointment(
      AppointmentId appointmentId, AppointmentType appointmentType, int capacity)
      throws RemoteException;

  public void removeAppointment(AppointmentId appointmentId, AppointmentType appointmentType)
      throws RemoteException;

  public List<Appointment> listAppointmentAvailability(AppointmentType appointmentType)
      throws RemoteException;
}
