package com.jia.dxssmjj.model.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class EvaluateAndUserDTO {

    private String userName;

    private String content;

    private BigDecimal rate;

}
