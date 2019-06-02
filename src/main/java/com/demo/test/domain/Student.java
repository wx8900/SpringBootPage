package com.demo.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * @author Jack
 * @date 2019/4/15
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"id", "name", "phone", "email"})
@Slf4j
@Table(name = "tbl_student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private long id;
    @Column
    @Length(min = 3, max = 20, message = "用户名长度必须在 {min} - {max} 之间")
    private String name;
    @Column
    @Length(min = 8, max = 20, message = "密码长度必须在 {min} - {max} 之间")
    private String password;
    @Column
    @NotBlank(message = "分部不能为空")
    private String branch;
    @Column
    @Length(min = 1, max = 3, message = "百分比长度必须在 {min} - {max} 之间")
    private String percentage;
    @Column
    @Length(min = 10, max = 11, message = "电话号码长度必须在 {min} - {max} 之间")
    private String phone;
    @Column
    @Email(message = "电子邮箱格式不正确")
    private String email;

}