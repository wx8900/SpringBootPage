package com.demo.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
/**
 * @author Jack
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultInfo {
    private String code;
    private String msg;
}
