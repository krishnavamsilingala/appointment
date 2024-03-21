package com.agency.appointment.controller;

import com.agency.appointment.dto.AppointmentDto;
import com.agency.appointment.dto.AvailableSlotsDto;
import com.agency.appointment.dto.SlotDto;
import com.agency.appointment.dto.request.BookingRequest;
import com.agency.appointment.dto.request.BookingRescheduleRequest;
import com.agency.appointment.service.IAppointmentManagementService;
import com.agency.appointment.service.ISlotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    @Autowired
    private ISlotService slotService;

    @Autowired
    private IAppointmentManagementService appointmentManagementService;


    @PostMapping(value = "/book")
    public ResponseEntity<AppointmentDto> bookAppointment(@RequestBody BookingRequest request){
        return new ResponseEntity<AppointmentDto>(appointmentManagementService.bookAppointment(request), HttpStatus.OK);
    };

    @PostMapping(value = "/cancel")
    public ResponseEntity<AppointmentDto> cancelAppointment(@RequestParam Long appointmentId){
        return new ResponseEntity<AppointmentDto>(appointmentManagementService.cancelAppointment(appointmentId), HttpStatus.OK);
    };

    @PostMapping(value = "/reschedule")
    public ResponseEntity<AppointmentDto> rescheduleAppointment(@RequestBody BookingRescheduleRequest request){
        return new ResponseEntity<AppointmentDto>(appointmentManagementService.rescheduleAppointment(request), HttpStatus.OK);
    };

//    @PostMapping(value = "/slots/generate")
//    public void generateSlotsForNextSevenDays() {
//        slotService.generateSlotsForNextSevenDays();
//    }

    @GetMapping(value = "/slots/available")
    public AvailableSlotsDto getAvailableSlots(@RequestParam Long operatorId){
        return slotService.getAvailableSlots(operatorId);
    };

    @GetMapping(value = "/slots/booked")
    public List<SlotDto> getBookedSlots(@RequestParam Long operatorId){
        return slotService.getBookedSlots(operatorId);
    }

    @GetMapping(value = "/booked")
    public List<AppointmentDto> getBookedAppointments(@RequestParam Long operatorId){
        return appointmentManagementService.getBookedAppointments(operatorId);
    }

}
