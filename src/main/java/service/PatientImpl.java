package service;

import java.rmi.RemoteException;
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

  private PatientImpl(){}

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
    database.add(patientId,appointmentId,type);
    logger.info(
            "Book appointment success: %s, %s"
                    .formatted(type.toString(), appointmentId.getId()));
  return true;
  }

  @Override
  public List<Appointment> getAppointmentSchedule(PatientId patientId) throws RemoteException {
    return null;
  }

  @Override
  public synchronized boolean cancelAppointment(PatientId patientId, AppointmentId appointmentId)
      throws RemoteException {
    return false;
  }
}
