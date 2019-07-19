package com.demo.test.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author Jack
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultInfo {
    private String code;
    private String msg;
}
