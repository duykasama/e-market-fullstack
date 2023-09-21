package com.example.emarket.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "customer")
public class Customer implements Serializable {
    @Id
    @Size(max = 50)
    @Column(name = "ID", nullable = false, length = 50)
    private String id;

    @Size(max = 50)
    @Column(name = "FirstName", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "LastName", length = 50)
    private String lastName;

    @Size(max = 100)
    @Column(name = "Address", length = 100)
    private String address;

    @Column(name = "Age")
    private Short age;

    @Size(max = 50)
    @Column(name = "Status", length = 50)
    private String status;

    // Thuộc tính Apartment để liên kết với Apartment
    @ManyToOne
    @JoinColumn(name = "apartment_id") // Điều này phụ thuộc vào cách bạn đặt tên cột liên kết trong cơ sở dữ liệu
    private Apartment apartment;
}