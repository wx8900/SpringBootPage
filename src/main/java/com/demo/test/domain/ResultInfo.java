package com.demo.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
