package database;

import java.util.HashMap;

import java.util.Optional;
import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;

public class Database {
  private final HashMap<AppointmentType, HashMap<AppointmentId, Appointment>> hashMap;
  private static Database instance;

  private Database() {
    hashMap = new HashMap<>();
  }

  public static synchronized Database getInstance() {
    if (instance == null) {
      instance = new Database();
    }
    return instance;
  }

  public void add(AppointmentId id, AppointmentType type, int capacity) {
    if (!hashMap.containsKey(type)) {
      hashMap.put(type, new HashMap<>());
    }
    hashMap.get(type).put(id, new Appointment(id, capacity));
    System.out.println("Appointment is added: " + id.getId());
  }

  public void remove(AppointmentId id, AppointmentType type) {}

  public Optional<Appointment> findByTypeAndId(AppointmentType type, AppointmentId id) {
    var innerHashMap = hashMap.get(type);
    if (innerHashMap != null) {
      var appointment = innerHashMap.get(id);
      if (appointment != null) {
        return Optional.of(appointment);
      } else {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }
}
