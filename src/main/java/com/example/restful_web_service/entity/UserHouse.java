package com.example.restful_web_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user_house")
public class UserHouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_name",nullable = false)
    private String userName;
    @Column(name = "user_phone", nullable = false, unique = true)
    private String userPhone;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String town;
    @Column(nullable = false)
    private String address;
    @Column(name = "house_number", nullable = false)
    private Integer houseNumber;
    @Column(name = "flat_number", nullable = false)
    private Integer flatNumber;

    @ManyToOne
    @JoinColumn(name = "user_house_id")
    private User user;

    public UserHouse(Integer id, String userName, String userPhone, String country,
                     String town, String address, Integer houseNumber, Integer flatNumber) {
        this.id = id;
        this.userName = userName;
        this.userPhone = userPhone;
        this.country = country;
        this.town = town;
        this.address = address;
        this.houseNumber = houseNumber;
        this.flatNumber = flatNumber;
    }
}
