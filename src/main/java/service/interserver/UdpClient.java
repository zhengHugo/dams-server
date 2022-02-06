package service.interserver;

import common.GlobalConstants;
import database.Database;
import java.util.ArrayList;
import java.util.List;
import model.appointment.AppointmentAvailability;
import model.appointment.AppointmentType;
import model.common.City;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UdpClient {
  private static final Database database = Database.getInstance();
  private static final Logger logger = LogManager.getLogger();

  public static List<AppointmentAvailability> requestAppointments(AppointmentType type) {
    var availabilities  = switch (GlobalConstants.thisCity) {
      case Montreal -> requestToCities(City.Quebec, City.Sherbrooke, type);
      case Quebec -> requestToCities(City.Montreal, City.Sherbrooke, type);
      case Sherbrooke -> requestToCities(City.Montreal, City.Quebec, type);
    };
     availabilities.addAll(database.findAllByType(type).stream().map(
         appointment -> new AppointmentAvailability(appointment.getAppointmentId().getId(),
             appointment.getRemainingCapacity())).toList());
     logger.info("Appointment availabilities received: " + availabilities);
     return availabilities;
  }

  private static List<AppointmentAvailability> requestToCities(City city1, City city2, AppointmentType type) {
    ListAppointmentClientThread city1Thread = new ListAppointmentClientThread(City.Quebec, type);
    ListAppointmentClientThread city2Thread = new ListAppointmentClientThread(City.Sherbrooke, type);
    Thread requestCity1Thread = new Thread(city1Thread);
    Thread requestCity2Thread = new Thread(city2Thread);
    requestCity1Thread.start();
    requestCity2Thread.start();
    try {
      requestCity1Thread.join();
      requestCity2Thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    List<AppointmentAvailability> allAvailabilities = new ArrayList<>();
    allAvailabilities.addAll(city1Thread.getAvailabilities());
    allAvailabilities.addAll(city2Thread.getAvailabilities());
    return allAvailabilities;
  }


}
