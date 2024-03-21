package com.agency.appointment;

import com.agency.appointment.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SlotGenerator {

    @Autowired
    private SlotRepository slotRepository;

}
