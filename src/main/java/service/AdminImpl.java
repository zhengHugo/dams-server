package service;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import api.Admin;
import database.Database;
import java.util.concurrent.atomic.AtomicReference;
import model.appointment.Appointment;
import model.appointment.AppointmentAvailability;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;
import model.role.PatientId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.interserver.UdpClient;

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
      logger.info(
          "Appointment is added: %s, %s"
              .formatted(appointmentType.toString(), appointmentId.getId()));
      return true;
    } else {
      logger.info(
          "Appointment is not added because appointment already exists with type: %s, id: %s"
              .formatted(appointmentType, appointmentId.getId()));
      return false;
    }
  }

  @Override
  public synchronized String removeAppointment(
      AppointmentId appointmentId, AppointmentType appointmentType) throws RemoteException {
    AtomicReference<String> message = new AtomicReference<>();
    database
        .findByTypeAndId(appointmentType, appointmentId)
        .ifPresentOrElse(
            appointment -> {
              var patientIds = appointment.getPatientIds();
              if (patientIds.size() > 0) {
                message.set(
                    putPatientsAfterAppointment(patientIds, appointmentType, appointmentId));
              }
              database.remove(appointmentId, appointmentType);
              message.set("The target appointment is removed.");
              logger.info(message.get());
            },
            () -> {
              message.set("The target appointment does not exist.");
              logger.info(message.get());
            });
    return message.get();
  }

  @Override
  public List<AppointmentAvailability> listAppointmentAvailability(AppointmentType appointmentType)
      throws RemoteException {
    return UdpClient.requestAppointments(appointmentType);
  }

  @Override
  public synchronized boolean bookAppointment(
      PatientId patientId, AppointmentId appointmentId, AppointmentType type)
      throws RemoteException, NotBoundException {
    return this.patient.bookAppointment(patientId, appointmentId, type);
  }

  @Override
  public List<Appointment> getAppointmentSchedule(PatientId patientId)
      throws RemoteException, NotBoundException {
    return this.patient.getAppointmentSchedule(patientId);
  }

  @Override
  public synchronized boolean cancelAppointment(
      PatientId patientId, AppointmentType type, AppointmentId appointmentId)
      throws RemoteException {
    return this.patient.cancelAppointment(patientId, type, appointmentId);
  }

  private List<PatientId> getAppointmentPatientIds(AppointmentType type, AppointmentId id) {
    return database.findByTypeAndId(type, id).orElseThrow().getPatientIds();
  }

  private synchronized String putPatientsAfterAppointment(
      List<PatientId> patientIds, AppointmentType type, AppointmentId id) {
    var targetAppointmentOptional = database.findByTypeAndId(type, id);
    String message = "";
    if (targetAppointmentOptional.isPresent()) {
      var nextAppointmentIdOptional = database.findNextAppointmentId(type, id);
      if (nextAppointmentIdOptional.isPresent()) {
        var nextAppId = nextAppointmentIdOptional.orElseThrow();
        var nextApp = database.findByTypeAndId(type, nextAppId).orElseThrow();
        if (patientIds.size() > nextApp.getRemainingCapacity()) {
          // next appointment cannot fit all patients; try put them to later ones
          int fromIndex = nextApp.getRemainingCapacity();
          nextApp.addPatients(patientIds.subList(0, fromIndex));
          message =
              "Patient(s) %s are assigned to appointment %s:%s"
                  .formatted(patientIds.subList(0, fromIndex), type, id);
          logger.info(message);
          putPatientsAfterAppointment(
              patientIds.subList(fromIndex, patientIds.size()), type, nextAppId);
        } else {
          nextApp.addPatients(patientIds);
          message =
              "Patient(s) %s are assigned to appointment %s:%s".formatted(patientIds, type, id);
          logger.info(message);
        }
      } else {
        // this is the last appointment; no future appointment is available
        message =
            "Patient(s) %s are dropped from appointments because no future appointments are available";
        logger.warn(message);
      }
    }
    return message;
  }
}
