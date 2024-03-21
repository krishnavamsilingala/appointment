package com.agency.appointment.service.impl;

import com.agency.appointment.dto.BookingDto;
import com.agency.appointment.entity.Appointment;
import com.agency.appointment.entity.Booking;
import com.agency.appointment.entity.QBooking;
import com.agency.appointment.entity.Slot;
import com.agency.appointment.enums.BookingStatus;
import com.agency.appointment.repository.BookingRepository;
import com.agency.appointment.service.IBookingService;
import com.agency.appointment.service.ISlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    private ISlotService slotService;


    @Override
    public Booking createBooking(Appointment appointment, Slot slot) {
        Booking booking=new Booking();
        booking.setAppointment(appointment);
        booking.setSlot(slot);
        booking.setBookingStatus(BookingStatus.ACTIVE);
        return booking;
    }

    @Override
    public Booking cancelBooking(Booking booking) {
        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.getSlot().setOpen(true);
        save(booking);
        slotService.save(booking.getSlot());
        return booking;
    }


    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getActiveBooking(Appointment appointment) {
        return bookingRepository.findOne(QBooking.booking.bookingStatus.eq(BookingStatus.ACTIVE)).orElse(null);
    }
}
