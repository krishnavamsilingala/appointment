package com.agency.appointment.entity;

import com.agency.appointment.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Appointment extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private Operator operator;

    @OneToMany(mappedBy = "appointment" ,cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Booking> bookings;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    @Version
    private Integer version;

}
