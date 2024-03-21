package com.agency.appointment.service;

import com.agency.appointment.dto.AppointmentDto;
import com.agency.appointment.dto.request.BookingRequest;
import com.agency.appointment.dto.request.BookingRescheduleRequest;

import java.util.List;

public interface IAppointmentManagementService {

    public AppointmentDto bookAppointment(BookingRequest request);

    public AppointmentDto cancelAppointment(Long appointmentId);

    AppointmentDto rescheduleAppointment(BookingRescheduleRequest request);

    List<AppointmentDto> getBookedAppointments(Long operatorId);

}
