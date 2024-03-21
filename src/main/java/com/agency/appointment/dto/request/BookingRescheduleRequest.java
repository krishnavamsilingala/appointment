package com.agency.appointment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRescheduleRequest {

    private Long appointmentId;

    private Long slotId;
}
