package com.demo.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * @author Jack
 * @date 2019/4/15
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"id", "name", "phone"})
@Slf4j
@Table(name = "tbl_student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private long id;
    @Column
    private String name;
    @Column
    private String password;
    @Column
    private String branch;
    @Column
    private int percentage;
    @Column
    private int phone;
    @Column
    private String email;

}