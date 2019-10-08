package com.demo.test.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Jack
 * @date 2019/10/08 update
 */
//@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Override
    public String toString() {
        return "EC2Instance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", vCPU=" + vCPU +
                ", CPUCredits=" + CPUCredits +
                ", memory=" + memory +
                ", instanceStorage='" + instanceStorage + '\'' +
                ", networkBandwidth='" + networkBandwidth + '\'' +
                ", networkPerformance='" + networkPerformance + '\'' +
                ", EBSBandwidth='" + EBSBandwidth + '\'' +
                '}';
    }
}
