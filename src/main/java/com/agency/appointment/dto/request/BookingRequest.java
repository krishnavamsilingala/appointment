package com.agency.appointment.dto.request;

import lombok.Data;

@Data
public class BookingRequest {
    private Long operatorId;
    private Long slotId;
}
