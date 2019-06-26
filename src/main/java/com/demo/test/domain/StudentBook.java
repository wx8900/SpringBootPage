package com.demo.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author Jack
 * @date   2019/06/25
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_student_book")
public class StudentBook implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uid;
    private String bid;
    private String extendTimes;
    private String returnDate;
    private int isReturned;
}
