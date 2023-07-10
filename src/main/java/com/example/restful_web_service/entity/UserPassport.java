package com.example.restful_web_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user_passport")
public class UserPassport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String sex;
    @Column(name = "date_of_birth", nullable = false)
    private String dateOfBirth;
    @Column(nullable = false)
    private String nationality;
    @Column(name = "date_of_issue", nullable = false)
    private String dateOfIssue;
    @Column(name = "date_of_expire")
    private String dateOfExpire;
    @Column(name = "passport_number", nullable = false, unique = true)
    private Integer passportNumber;

    @OneToOne(mappedBy = "userPassport")
    @JoinColumn(name = "user_passport_id")
    private User user;

    public UserPassport(Integer id, String name, String surname, String sex, String dateOfBirth,
                        String nationality, String dateOfIssue, String dateOfExpire, Integer passportNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.dateOfIssue = dateOfIssue;
        this.dateOfExpire = dateOfExpire;
        this.passportNumber = passportNumber;
    }
}
