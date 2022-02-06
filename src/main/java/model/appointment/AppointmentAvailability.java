package model.appointment;

import java.io.Serializable;

public record AppointmentAvailability(String appointmentId, int availability) implements
    Serializable {}
