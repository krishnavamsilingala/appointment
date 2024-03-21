package com.agency.appointment.entity;


import com.agency.appointment.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking extends BaseEntity{

    @NonNull
    @ManyToOne
    @JsonBackReference
    private Appointment appointment;


    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @OneToOne
    private Slot slot;

    @Version
    private Integer version;
}
