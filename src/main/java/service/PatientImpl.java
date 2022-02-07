package service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
  private final PatientImpl patient = PatientImpl.getInstance();

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

    Optional<Appointment> appointment = database.findByTypeAndId(type, appointmentId);
    ArrayList<PatientId> patientIds = appointment.orElseThrow().addPatient(patientId);
    int capacity = appointment.get().getRemainingCapacity();
    database.update(new Appointment(appointmentId, capacity, patientIds), appointmentId);

    logger.info(
        "Book appointment success: %s, %s".formatted(type.toString(), appointmentId.getId()));
    return true;
  }

  @Override
  public List<Appointment> getAppointmentSchedule(PatientId patientId) throws RemoteException {
    return database.findAllByPatientId(patientId);
  }

  @Override
  public synchronized boolean cancelAppointment(PatientId patientId, AppointmentId appointmentId)
      throws RemoteException {
    List<Appointment> appointmentList = database.findAllByPatientId(patientId);
    appointmentList.forEach(
        appointment -> {
          if (appointment.getAppointmentId() == appointmentId) {
            ArrayList<PatientId> patientIds = appointment.removePatient(patientId);
            int capacity = appointment.getRemainingCapacity();
            database.update(new Appointment(appointmentId, capacity, patientIds), appointmentId);
          }
        });
    logger.info("The target appointment is removed.");
    return true;
  }
}
