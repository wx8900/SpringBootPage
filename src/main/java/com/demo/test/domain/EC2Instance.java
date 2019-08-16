package com.demo.test.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Jack
 * @date 2019/08/16
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EC2Instance {

    @Id
    private Integer id;
    private String name;
    private String model;
    private Integer vCPU;
    /**
     * can be empty
     */
    private Integer CPUCredits;
    /**
     * GiB
     */
    private Integer memory;
    /**
     * GiB
     */
    private String instanceStorage;
    /**
     * can be empty, Gbps
     */
    private String networkBandwidth;
    /**
     * can be empty, Gbps
     */
    private String networkPerformance;
    /**
     * can be empty, Mbps
     */
    private String EBSBandwidth;
}
