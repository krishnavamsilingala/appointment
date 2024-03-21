package com.agency.appointment.dto;

import com.agency.appointment.enums.AppointmentStatus;
import lombok.Data;

import java.util.List;

@Data
public class AppointmentDto extends BaseEntityPojo{

    private OperatorDto operator;

    private BookingDto booking;

    private List<BookingDto> prevBookings;

    private AppointmentStatus appointmentStatus;

}
