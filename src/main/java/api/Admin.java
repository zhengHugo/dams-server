package api;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.appointment.Appointment;
import model.appointment.AppointmentAvailability;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.PatientId;

public interface Admin extends Remote {

  /**
   * Create a new appointment slot
   *
   * @param appointmentId Appointment id
   * @param appointmentType Appointment type
   * @param capacity Appointment capacity
   * @return true if operation is successful
   */
  boolean addAppointment(AppointmentId appointmentId, AppointmentType appointmentType, int capacity)
      throws RemoteException;

  /**
   * Remove a new appointment slot
   *
   * @param appointmentId Appointment id
   * @param appointmentType Appointment type
   * @return true if operation is successful
   */
  String removeAppointment(AppointmentId appointmentId, AppointmentType appointmentType)
      throws RemoteException;

  /**
   * List the number of spaces of all appointments
   *
   * @param appointmentType Appointment type
   * @return a list of appointments whose capacity is the available number
   */
  List<AppointmentAvailability> listAppointmentAvailability(AppointmentType appointmentType)
      throws RemoteException;

  /**
   * Book an appointment
   *
   * @param patientId Patient id
   * @param appointmentId Appointment id
   * @param type Appointment capacity
   * @return true if operation is successful
   */
  boolean bookAppointment(PatientId patientId, AppointmentId appointmentId, AppointmentType type)
      throws RemoteException, NotBoundException;

  /**
   * List all appointments associated with this patient
   *
   * @param patientId Patient id
   * @return a list of appointments associated with this patient
   */
  List<Appointment> getAppointmentSchedule(PatientId patientId)
      throws RemoteException, NotBoundException;

  /**
   * Cancel an appointment
   *
   * @param patientId Patient id
   * @param appointmentId Appointment id
   * @return true if operation is successful
   */
  boolean cancelAppointment(PatientId patientId, AppointmentType type, AppointmentId appointmentId)
      throws RemoteException;
}
