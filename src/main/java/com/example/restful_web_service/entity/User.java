package com.example.restful_web_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer age;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    private UserPassport userPassport;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserCar> userCars;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserHouse> userHouses;

    public User(Integer id, String name, Integer age, String email, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }
}
