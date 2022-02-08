package service;

import common.GlobalConstants;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import api.Patient;
import database.Database;
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
      throws RemoteException, NotBoundException {
    Registry registry = LocateRegistry.getRegistry("localhost", 1099);
    Patient patientRemote = (Patient) registry.lookup("Patient" + appointmentId.getCity().code);
    // check for multiple bookings in one day
    if (getAppointmentSchedule(patientId).stream()
        .filter(app -> app.getType().equals(type))
        .map(app -> app.getAppointmentId().getDate())
        .toList()
        .contains(appointmentId.getDate())) {
      logger.info("The patient %s already has an appointment %s - %s. Booking not proceeded"
          .formatted(patientId, type, appointmentId));
      return false;
    }
    if (GlobalConstants.thisCity.equals(appointmentId.getCity())) {
      // patient book an appointment in its own city
      return bookLocalAppointment(patientId, appointmentId, type);
    } else {
      // call bookLocalAppointment at the city according to the appointment
      // check the 3 times/week booking limit first
      int thisWeek = appointmentId.getDate().getWeekOfWeekyear();
      int allAppsInThisWeekCount = patientRemote.getAppointmentSchedule(patientId).stream()
          .filter(app -> app.getAppointmentId().getDate().getWeekOfWeekyear() == thisWeek)
          .toList().size();
      int localAppsInThisWeekCount = getLocalAppointmentSchedule(patientId).stream()
          .filter(app -> app.getAppointmentId().getDate().getWeekOfWeekyear() == thisWeek)
          .toList().size();
      int nonlocalAppCount = allAppsInThisWeekCount - localAppsInThisWeekCount;
      if (nonlocalAppCount < 3){
        logger.info("Call server " + appointmentId.getCity().code +
            " to book appointment for %s, %s - %s".formatted(patientId, type, appointmentId));
        return patientRemote.bookLocalAppointment(patientId, appointmentId, type);
      } else {
        logger.info("Non-local appointments over limit. Cannot book for %s, %s - %s"
            .formatted(patientId, type, appointmentId));
        return false;
      }
    }
  }

  @Override
  public synchronized boolean bookLocalAppointment(PatientId patientId, AppointmentId appointmentId,
      AppointmentType type) throws RemoteException {
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
  public List<Appointment> getLocalAppointmentSchedule(PatientId patientId) throws RemoteException {
    return database.findAllByPatientId(patientId);
  }

  @Override
  public List<Appointment> getAppointmentSchedule(PatientId patientId)
      throws RemoteException, NotBoundException {
    List<Appointment> allAppointmentIds = new ArrayList<>(getLocalAppointmentSchedule(patientId));
    Registry registry = LocateRegistry.getRegistry("localhost", 1099);
    Patient patientRemote1 = (Patient) switch (GlobalConstants.thisCity) {
      case Montreal -> registry.lookup("PatientQUE");
      case Quebec -> registry.lookup("PatientSHE");
      case Sherbrooke -> registry.lookup("PatientMTL");
    };
    Patient patientRemote2 = (Patient) switch (GlobalConstants.thisCity) {
      case Montreal -> registry.lookup("PatientSHE");
      case Quebec -> registry.lookup("PatientMTL");
      case Sherbrooke -> registry.lookup("PatientQUE");
    };
    allAppointmentIds.addAll(patientRemote1.getLocalAppointmentSchedule(patientId));
    allAppointmentIds.addAll(patientRemote2.getLocalAppointmentSchedule(patientId));
    return allAppointmentIds;
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
