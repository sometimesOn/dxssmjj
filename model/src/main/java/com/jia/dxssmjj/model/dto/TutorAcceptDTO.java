package com.jia.dxssmjj.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TutorAcceptDTO {

    private String userHeadImgUrl;

    private Long makeAppointmentId;

    private BigDecimal total;

    private String hourString;

    private String subject;

    private String address;

    private String realName;

    private String phone;

    private String weChat;

    private String remark;

}
