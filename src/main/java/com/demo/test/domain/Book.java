package com.demo.test.domain;

import lombok.*;

import javax.persistence.*;

/**
 * @author Jack
 * @date 2019/06/24
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "tbl_book")
public class Book implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ISBN;
    private String name;
    private String price;
    private String desc;
}
