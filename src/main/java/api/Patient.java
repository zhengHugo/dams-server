package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.PatientId;

public interface Patient extends Remote {

  /**
   * Book an appointment
   *
   * @param patientId Patient id
   * @param appointmentId Appointment id
   * @param type Appointment capacity
   * @return true if operation is successful
   */
  boolean bookAppointment(PatientId patientId, AppointmentId appointmentId, AppointmentType type)
      throws RemoteException;

  /**
   * List all appointments associated with this patient
   *
   * @param patientId Patient id
   * @return a list of appointments associated with this patient
   */
  List<AppointmentId> getAppointmentSchedule(PatientId patientId) throws RemoteException;

  /**
   * Cancel an appointment
   *
   * @param patientId Patient id
   * @param appointmentId Appointment id
   * @return true if operation is successful
   */
  boolean cancelAppointment(
      PatientId patientId, AppointmentType appointmentType, AppointmentId appointmentId)
      throws RemoteException;
}
