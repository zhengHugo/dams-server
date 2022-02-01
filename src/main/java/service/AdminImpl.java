package service;

import java.rmi.RemoteException;
import java.util.List;

import api.Admin;
import database.Database;
import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;

public class AdminImpl implements Admin {

    private Database database = Database.getInstance();
    @Override
    public void addAppointment(AppointmentId appointmentId, AppointmentType appointmentType, int capacity) throws RemoteException {
        database.add(appointmentId, appointmentType, capacity);
    }

    @Override
    public void removeAppointment(AppointmentId appointmentId, AppointmentType appointmentType) throws RemoteException {

    }

    @Override
    public List<Appointment> listAppointmentAvailability(AppointmentType appointmentType) throws RemoteException {
        return null;
    }
}
