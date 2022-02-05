package database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import java.util.List;
import java.util.Map.Entry;
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

  public synchronized void add(AppointmentId id, AppointmentType type, int capacity) {
    if (!hashMap.containsKey(type)) {
      hashMap.put(type, new HashMap<>());
    }
    hashMap.get(type).put(id, new Appointment(id, capacity));
  }

  public synchronized void remove(AppointmentId id, AppointmentType type) {
    var innerHashMap = hashMap.get(type);
    if (innerHashMap != null) {
      var appointment = innerHashMap.get(id);
      if (appointment != null) {
        innerHashMap.remove(id);
      }
    }
  }

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

  public Collection<Appointment> findAllByType(AppointmentType type){
    var innerHashMap = hashMap.get(type);
    if (innerHashMap != null) {
      return innerHashMap.values();
    } else {
      return new ArrayList<>();
    }
  }

  public Optional<AppointmentId> findNextAppointmentId(
      AppointmentType type, AppointmentId thisId) {
    // 1. Get the inner hashmap that contain this appointment
    // 2. Iterator over the inner hashmap and get the next available one
    var innerHashMap = hashMap.get(type);
    if (innerHashMap != null) {
      AppointmentId nextAvailableAppointmentId = null;
      for (Entry<AppointmentId, Appointment> appointmentIdAppointmentEntry :
          innerHashMap.entrySet()) {
        var nextId = appointmentIdAppointmentEntry.getKey();
        if (thisId.compareTo(nextId) < 0) {
          if (nextAvailableAppointmentId == null
              || nextId.compareTo(nextAvailableAppointmentId) < 0) {
            nextAvailableAppointmentId = nextId;
          }
        }
      }
      return nextAvailableAppointmentId == null
          ? Optional.empty()
          : Optional.of(nextAvailableAppointmentId);
    }
    return Optional.empty();
  }
}
