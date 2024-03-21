package com.agency.appointment.dto;

import com.agency.appointment.entity.Operator;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class SlotDto extends BaseEntityPojo{

    private LocalDate date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean open;
}
