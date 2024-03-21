package com.agency.appointment.service;

import com.agency.appointment.dto.AvailableSlotsDto;
import com.agency.appointment.dto.SlotDto;
import com.agency.appointment.entity.Booking;
import com.agency.appointment.entity.Operator;
import com.agency.appointment.entity.Slot;

import java.util.List;

public interface ISlotService {

    void generateSlots(Operator operator,Integer days);

    AvailableSlotsDto getAvailableSlots(Long operatorId);

    List<SlotDto> getBookedSlots(Long operatorId);

    Slot checkIfSlotIsOpen(Long slotId, Long operatorId);

    Slot save(Slot slot);
}
