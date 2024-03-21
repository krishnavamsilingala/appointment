package com.agency.appointment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Slot extends BaseEntity{

    @ManyToOne
    private Operator operator;

    private LocalDate date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    // slots availability
    private Boolean open;

    //for controlling concurrency through optimistic locking
    @Version
    private Integer version;
}
