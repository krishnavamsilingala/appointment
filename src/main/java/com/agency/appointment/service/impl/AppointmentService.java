package com.agency.appointment.service.impl;

import com.agency.appointment.entity.Appointment;
import com.agency.appointment.entity.Operator;
import com.agency.appointment.entity.QAppointment;
import com.agency.appointment.enums.AppointmentStatus;
import com.agency.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    @Autowired
    public AppointmentRepository appointmentRepository;

    public Appointment createAppointment(Operator operator){
        Appointment appointment=new Appointment();
        appointment.setOperator(operator);
        appointment.setAppointmentStatus(AppointmentStatus.BOOKED);
        return appointment;
    }

    public Appointment save(Appointment appointment){
        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointmentById(Long id){
        return appointmentRepository.findOne(QAppointment.appointment.id.eq(id)).orElse(null);
    }

    public List<Appointment> getAppointmentByOperatorId(Long id){
        return appointmentRepository.findAll(QAppointment.appointment.operator().id.eq(id)
                .and(QAppointment.appointment.appointmentStatus.eq(AppointmentStatus.BOOKED)));
    }
}
