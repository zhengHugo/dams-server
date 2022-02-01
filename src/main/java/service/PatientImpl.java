package service;

import java.rmi.RemoteException;
import java.util.List;

import api.Patient;
import model.PatientId;
import model.appointment.Appointment;
import model.appointment.AppointmentId;
import model.appointment.AppointmentType;

public class PatientImpl implements Patient {

    @Override
    public void bookAppointment(PatientId patientId, AppointmentId appointmentId, AppointmentType type) throws RemoteException {

    }

    @Override
    public List<Appointment> getAppointmentSchedule(PatientId patientId) throws RemoteException {
        return null;
    }

    @Override
    public void cancelAppointment(PatientId patientId, AppointmentId appointmentId) throws RemoteException {

    }
}
