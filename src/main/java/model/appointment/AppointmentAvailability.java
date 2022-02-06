package model.appointment;

import java.io.Serializable;

public record AppointmentAvailability(AppointmentId appointmentId, int availability) implements
    Serializable {}
