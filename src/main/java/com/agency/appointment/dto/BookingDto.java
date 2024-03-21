package com.agency.appointment.dto;

import com.agency.appointment.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto extends BaseEntityPojo{

    private BookingStatus bookingStatus;

    private SlotDto slot;

}
