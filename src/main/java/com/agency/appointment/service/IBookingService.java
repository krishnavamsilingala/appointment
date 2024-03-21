package com.agency.appointment.service;


import com.agency.appointment.dto.BookingDto;
import com.agency.appointment.entity.Appointment;
import com.agency.appointment.entity.Booking;
import com.agency.appointment.entity.Slot;

public interface IBookingService {

    public Booking createBooking(Appointment appointment, Slot slot);

    public Booking cancelBooking(Booking booking);


    Booking save(Booking booking);

    Booking getActiveBooking(Appointment appointment);
}
