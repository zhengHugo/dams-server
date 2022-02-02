package service;

import java.rmi.RemoteException;
import java.util.List;

import api.Admin;
import database.Database;
import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.PatientId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdminImpl implements Admin {

  private final Database database = Database.getInstance();
  private final PatientImpl patient = PatientImpl.getInstance();

  private static AdminImpl instance;

  private static final Logger logger = LogManager.getLogger();

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
    logger.info(
        "Appointment is added: %s, %s"
            .formatted(appointmentType.toString(), appointmentId.getId()));
    return true;
  }

  @Override
  public synchronized boolean removeAppointment(
      AppointmentId appointmentId, AppointmentType appointmentType) throws RemoteException {
    database
        .findByTypeAndId(appointmentType, appointmentId)
        .ifPresent(
            appointment -> {
              var patientIds = appointment.getPatientIds();
              if (patientIds.size() > 0) {
                putPatientsAfterAppointment(patientIds, appointmentType, appointmentId);
              }
              database.remove(appointmentId, appointmentType);
            });
    return true;
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

  private List<PatientId> getAppointmentPatientIds(AppointmentType type, AppointmentId id) {
    return database.findByTypeAndId(type, id).orElseThrow().getPatientIds();
  }

  private synchronized void putPatientsAfterAppointment(
      List<PatientId> patientIds, AppointmentType type, AppointmentId id) {
    var targetAppointmentOptional = database.findByTypeAndId(type, id);
    if (targetAppointmentOptional.isPresent()) {
      var nextAppointmentIdOptional = database.findNextAppointmentId(type, id);
      if (nextAppointmentIdOptional.isPresent()) {
        var nextAppId = nextAppointmentIdOptional.orElseThrow();
        var nextApp = database.findByTypeAndId(type, nextAppId).orElseThrow();
        if (patientIds.size() > nextApp.getRemainingCapacity()) {
          // next appointment cannot fit all patients; try put them to later ones
          int fromIndex = nextApp.getRemainingCapacity();
          nextApp.addPatients(patientIds.subList(0, fromIndex));
          logger.info(
              "Patient(s) %s are assigned to appointment %s:%s".formatted(patientIds, type, id));
          putPatientsAfterAppointment(
              patientIds.subList(fromIndex, patientIds.size()), type, nextAppId);
        } else {
          nextApp.addPatients(patientIds);
          logger.info(
              "Patient(s) %s are assigned to appointment %s:%s".formatted(patientIds, type, id));
        }
      } else {
        // this is the last appointment; no future appointment is available
        logger.warn(
            "Patient(s) %s are dropped from appointment %s:%s because no future appointments are available"
                .formatted(patientIds, type, id));
      }
    }
  }
}
