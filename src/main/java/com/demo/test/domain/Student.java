package com.demo.test.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Jack
 * @date 2019/4/15
 */
@Entity
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
@Table(name = "tbl_student")
public class Student implements java.io.Serializable {

    public Student(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private long id;
    /**
     * We’ll use @Column to indicate specific characteristics of the physical database column.
     * Let’s use the length attribute of the @Column annotation to specify the string-valued column length:
     */
    @ApiModelProperty("用户名")
    @Size(min = 3, max = 20, message = "用户名长度必须在 {min} - {max} 之间")
    private String name;

    @ApiModelProperty("密码")
    @Size(min = 8, max = 20, message = "密码长度必须在 {min} - {max} 之间")
    private String password;

    @NotBlank(message = "分部不能为空")
    private String branch;

    @Size(min = 1, max = 3, message = "百分比长度必须在 {min} - {max} 之间")
    private String percentage;

    @Size(min = 10, max = 11, message = "电话号码长度必须在 {min} - {max} 之间")
    private String phone;

    @Email(message = "电子邮箱格式不正确")
    private String email;

}