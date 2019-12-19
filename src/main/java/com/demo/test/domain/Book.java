package com.demo.test.domain;

import lombok.*;

import javax.persistence.*;

/**
 * @author Jack
 * @date 2019/06/24
 */

@Entity
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
@Table(name = "tbl_book")
public class Book implements java.io.Serializable {

    private static final long serialVersionUID = -7234223743479253383L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ISBN;
    private String name;
    private String price;
    private String desc;

    public Book() {}
}
