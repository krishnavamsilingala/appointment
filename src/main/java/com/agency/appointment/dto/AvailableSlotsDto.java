package com.agency.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableSlotsDto {

    private List<SlotDto> availableMergedSlots;

    private List<SlotDto> availableSlots;

}
