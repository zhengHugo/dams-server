package service;

import java.rmi.RemoteException;
import java.util.List;

import api.Patient;
import database.Database;
import java.util.stream.Collectors;
import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.PatientId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PatientImpl implements Patient {
  private static PatientImpl instance;
  private final Database database = Database.getInstance();
  private static final Logger logger = LogManager.getLogger();

  private PatientImpl() {}

  public static synchronized PatientImpl getInstance() {
    if (instance == null) {
      instance = new PatientImpl();
    }
    return instance;
  }

  @Override
  public synchronized boolean bookAppointment(
      PatientId patientId, AppointmentId appointmentId, AppointmentType type)
      throws RemoteException {

    var appointmentOptional = database.findByTypeAndId(type, appointmentId);
    if (appointmentOptional.isPresent()) {
      var appointment = appointmentOptional.orElseThrow();
      if (appointment.addPatient(patientId) && database.update(appointment, type)) {
        logger.info(
            "Book appointment success: %s, %s - %s"
                .formatted(patientId.getId(), type.toString(), appointmentId.getId()));
        return true;
      } else {
        logger.info(
            "Cannot book appointment: %s, %s - %s"
                .formatted(patientId.getId(), type.toString(), appointmentId.getId()));
        return false;
      }
    } else {
      logger.info("The appointment %s - %s doesn't exist.".formatted(type, appointmentId));
      return false;
    }
  }

  @Override
  public List<AppointmentId> getAppointmentSchedule(PatientId patientId) throws RemoteException {
    // TODO: list from all cities
    return database.findAllByPatientId(patientId).stream()
        .map(Appointment::getAppointmentId)
        .collect(Collectors.toList());
  }

  @Override
  public synchronized boolean cancelAppointment(
      PatientId patientId, AppointmentType type, AppointmentId appointmentId)
      throws RemoteException {
    var appointmentOptional = database.findByTypeAndId(type, appointmentId);
    if (appointmentOptional.isPresent()) {
      var appointment = appointmentOptional.orElseThrow();
      if (appointment.removePatient(patientId)) {
        database.update(appointment, type);
        logger.info(
            "Cancel appointment success: %s, %s - %s".formatted(patientId, type, appointmentId));
        return true;
      } else {
        logger.info(
            "The patient %s doesn't have an appointment with %s - %s"
                .formatted(patientId, type, appointmentId));
        return false;
      }
    } else {
      logger.info("The appointment %s doesn't exist".formatted(appointmentId));
      return false;
    }
  }
}
