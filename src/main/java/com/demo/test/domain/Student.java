package com.demo.test.domain;

import lombok.Data;
import javax.persistence.*;

/**
 *
 * Created by  on 2019/4/15
 */
@Data
@Entity
@Table(name="tbl_student")
public class Student {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) //auto increment
    private long id;
    @Column
    private String name;
    @Column
    private String branch;
    @Column
    private int percentage;
    @Column
    private int phone;
    @Column
    private String email;

}