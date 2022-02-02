package database;

import java.util.HashMap;

import model.appointment.AppointmentId;
import model.appointment.AppointmentType;

public class Database {
  private final HashMap<AppointmentType, HashMap<AppointmentId, Integer>> hashMap;
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
    hashMap.get(type).put(id, capacity);
    System.out.println("Appointment is added: " + id.getId());
  }

  public void remove(AppointmentId id, AppointmentType type) {}
}
