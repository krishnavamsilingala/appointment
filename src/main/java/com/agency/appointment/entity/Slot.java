package com.agency.appointment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;



@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"operator_id", "date", "startTime", "endTime"}))
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
