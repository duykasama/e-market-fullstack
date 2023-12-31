package com.example.emarket.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Apartment implements Serializable {
    @Id
    @Size(max = 50)
    @Column(name = "id", nullable = false, length = 50)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    private String address;

    @Size(max = 12)
    @Column(name = "rental_price", length = 12)
    private String rentalPrice;

    @Column(name = "number_of_rooms")
    private Short numberOfRooms;

}