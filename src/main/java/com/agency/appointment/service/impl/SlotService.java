package com.agency.appointment.service.impl;

import com.agency.appointment.dto.AvailableSlotsDto;
import com.agency.appointment.dto.SlotDto;
import com.agency.appointment.entity.Operator;
import com.agency.appointment.entity.QSlot;
import com.agency.appointment.entity.Slot;
import com.agency.appointment.repository.SlotRepository;
import com.agency.appointment.service.ISlotService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlotService implements ISlotService {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void generateSlots(Operator operator,Integer days) {
        LocalDate currentDate = LocalDate.now();
        List<Slot> slots = new ArrayList<>();

        // Generate slots for the next seven days
        for (int i = 0; i < days; i++) {
            LocalDate currentDatePlusI = currentDate.plusDays(i);
            for (int hour = 0; hour < 24; hour++) {
                LocalDateTime startDateTime = LocalDateTime.of(currentDatePlusI, LocalTime.of(hour, 0));
                LocalDateTime endDateTime = startDateTime.plusHours(1); // Slot duration is 1 hour

                Slot slot = new Slot();
                slot.setDate(startDateTime.toLocalDate());
                slot.setStartTime(startDateTime);
                slot.setEndTime(endDateTime);
                slot.setOperator(operator);
                slot.setOpen(true); // Assuming all slots are open initially

                slots.add(slot);
            }
        }

        // Save the generated slots to the database
        slotRepository.saveAll(slots);
    }


    @Override
    public AvailableSlotsDto getAvailableSlots(Long operatorId) {
        List<Slot> slots=slotRepository.findAll(QSlot.slot.open.eq(true)
                .and(QSlot.slot.operator().id.eq(operatorId))
                .and(QSlot.slot.date.goe(LocalDate.now())));
        List<SlotDto> mergedSlots = mergeSlots(slots);
        return new AvailableSlotsDto(mergedSlots,slots.stream().map(s->modelMapper.map(s,SlotDto.class)).collect(Collectors.toList()));
    }


    private List<SlotDto> mergeSlots(List<Slot> slots) {
        List<SlotDto> mergedSlots = new ArrayList<>();
        SlotDto currentSlot = null;

        for (Slot slot : slots) {
            if (currentSlot == null) {
                currentSlot = new SlotDto();
                currentSlot.setDate(slot.getDate());
                currentSlot.setStartTime(slot.getStartTime());
                currentSlot.setEndTime(slot.getEndTime());
            } else {
                if (currentSlot.getDate().equals(slot.getDate()) && currentSlot.getEndTime().equals(slot.getStartTime())) {
                    // If the current slot's date matches the next slot's date and end time is same as start time, merge them
                    currentSlot.setEndTime(slot.getEndTime());
                } else {
                    // If not continuous or dates don't match, add current slot to merged slots and start a new slot
                    mergedSlots.add(currentSlot);
                    currentSlot = new SlotDto();
                    currentSlot.setDate(slot.getDate());
                    currentSlot.setStartTime(slot.getStartTime());
                    currentSlot.setEndTime(slot.getEndTime());
                }
            }
        }

        // Add the last slot
        if (currentSlot != null) {
            mergedSlots.add(currentSlot);
        }

        // Convert merged slots to the desired format (4-14)
        List<SlotDto> formattedSlots = new ArrayList<>();
        for (SlotDto slot : mergedSlots) {
            formattedSlots.add(formatSlot(slot));
        }

        return formattedSlots;
    }

    // Helper method to format slots as 4-14
    private SlotDto formatSlot(SlotDto slot) {
        return modelMapper.map(slot,SlotDto.class);
    }

    @Override
    public List<SlotDto> getBookedSlots(Long operatorId) {
        List<Slot> slots=slotRepository.findAll(QSlot.slot.open.eq(false)
                .and(QSlot.slot.operator().id.eq(operatorId))
                .and(QSlot.slot.date.goe(LocalDate.now())));
        return slots.stream().map(s->modelMapper.map(s,SlotDto.class)).collect(Collectors.toList());
    }

    @Override
    public Slot checkIfSlotIsOpen(Long slotId,Long operatorId) {
        Slot slot= slotRepository.findOne(QSlot.slot.id.eq(slotId)
                .and(QSlot.slot.operator().id.eq(operatorId))).orElse(null);
        if(slot==null){
            throw new RuntimeException("Slot not found");
        } else if (slot.getOpen().equals(false)) {
            throw new RuntimeException("Slot not available");
        }

        return slot.getOpen().equals(true)?slot:null;
    }

    @Override
    public Slot save(Slot slot) {
        return slotRepository.save(slot);
    }
}
