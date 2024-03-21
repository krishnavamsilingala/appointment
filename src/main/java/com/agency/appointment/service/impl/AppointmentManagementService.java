package com.agency.appointment.service.impl;

import com.agency.appointment.dto.AppointmentDto;
import com.agency.appointment.dto.BookingDto;
import com.agency.appointment.dto.SlotDto;
import com.agency.appointment.dto.request.BookingRequest;
import com.agency.appointment.dto.request.BookingRescheduleRequest;
import com.agency.appointment.entity.Appointment;
import com.agency.appointment.entity.Booking;
import com.agency.appointment.entity.Operator;
import com.agency.appointment.entity.Slot;
import com.agency.appointment.enums.AppointmentStatus;
import com.agency.appointment.enums.BookingStatus;
import com.agency.appointment.service.IAppointmentManagementService;
import com.agency.appointment.service.IBookingService;
import com.agency.appointment.service.ISlotService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentManagementService implements IAppointmentManagementService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ISlotService slotService;

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private AppointmentService appointmentService;


    @Override
    public AppointmentDto bookAppointment(BookingRequest request) {
        //check for operators available slots
        Operator operator=operatorService.getOperatorById(request.getOperatorId());
        Slot slot=slotService.checkIfSlotIsOpen(request.getSlotId(),request.getOperatorId());

        //create an appointment
        Appointment appointment=appointmentService.createAppointment(operator);

        //create a booking
        Booking booking=bookingService.createBooking(appointment,slot);

        //modify slot's open to false
        slot.setOpen(false);

        appointmentService.save(appointment);
        slotService.save(slot);
        bookingService.save(booking);
        AppointmentDto appointmentDto = getAppointmentDto(slot, appointment, booking);


        return appointmentDto;
    }

    private AppointmentDto getAppointmentDto(Slot slot, Appointment appointment, Booking booking) {
        //create appointment dto and return
        BookingDto bookingDto=modelMapper.map(booking,BookingDto.class);
        bookingDto.setSlot(modelMapper.map(slot,SlotDto.class));
        AppointmentDto appointmentDto=modelMapper.map(appointment,AppointmentDto.class);
        appointmentDto.setBooking(bookingDto);
        return appointmentDto;

    }

    @Override
    public AppointmentDto cancelAppointment(Long appointmentId) {
        Appointment appointment=appointmentService.getAppointmentById(appointmentId);
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);

        //make booking redundant
        Booking booking=bookingService.getActiveBooking(appointment);
        booking=bookingService.cancelBooking(booking);
//        booking.setBookingStatus(BookingStatus.CANCELLED);

//        booking.getSlot().setOpen(true);
        appointmentService.save(appointment);
//        bookingService.save(booking);
//        slotService.save(booking.getSlot());
        return getAppointmentDto(booking.getSlot(),appointment,booking);
    }

    @Override
    public AppointmentDto rescheduleAppointment(BookingRescheduleRequest request) {
        Appointment appointment=appointmentService.getAppointmentById(request.getAppointmentId());
        if(!appointment.getAppointmentStatus().equals(AppointmentStatus.BOOKED)){
            throw new RuntimeException("Appointment cannot be rescheduled");
        }



        //make booking redundant
        Booking booking=bookingService.getActiveBooking(appointment);
        booking.setBookingStatus(BookingStatus.RE_SCHEDULED);

        booking.getSlot().setOpen(true);

        //checking slot avaiability
        Slot slot=slotService.checkIfSlotIsOpen(request.getSlotId(),appointment.getOperator().getId());
        slot.setOpen(false);

        //create a booking
        Booking newBooking=bookingService.createBooking(appointment,slot);


        appointmentService.save(appointment);
        bookingService.save(booking);
        bookingService.save(newBooking);
        slotService.save(booking.getSlot());
        slotService.save(slot);
        return getAppointmentDto(slot,appointment,newBooking);
    }

    @Override
    public List<AppointmentDto> getBookedAppointments(Long operatorId) {
        List<Appointment> appointments=appointmentService.getAppointmentByOperatorId(operatorId);
        List<AppointmentDto> response= appointments.stream().map(appointment->{
            AppointmentDto dto=modelMapper.map(appointment,AppointmentDto.class);
            appointment.getBookings().forEach((b)->{
                System.out.println("Booking Id:  "+b.getId());
                if(b.getBookingStatus().equals(BookingStatus.ACTIVE)){
                    dto.setBooking(modelMapper.map(b,BookingDto.class));
                } else{
                    if(dto.getPrevBookings()==null){
                        dto.setPrevBookings(new ArrayList<>());
                    }
                    dto.getPrevBookings().add(modelMapper.map(b,BookingDto.class));
                }
            });
            return dto;
        }).collect(Collectors.toList());

        return response;
    }
}
