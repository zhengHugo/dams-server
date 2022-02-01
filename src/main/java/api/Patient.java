package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.PatientId;
import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;

public interface Patient extends Remote {
    public void bookAppointment(PatientId patientId, AppointmentId appointmentId, AppointmentType type) throws RemoteException;

    public List<Appointment> getAppointmentSchedule(PatientId patientId) throws RemoteException;

    public void cancelAppointment(PatientId patientId, AppointmentId appointmentId) throws RemoteException;

}
