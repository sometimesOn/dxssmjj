package com.jia.dxssmjj.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyAppointmentDTO {

    private Long tutorId;

    private String headImageUrl;

    private Long makeAppointmentId;

    private String status;

    private String subject;

    private BigDecimal total;

    private String personIntr;

    private String tutorName;

    private String hourString;
}
