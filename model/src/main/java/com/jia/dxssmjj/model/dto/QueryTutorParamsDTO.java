package com.jia.dxssmjj.model.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class QueryTutorParamsDTO {

    //审核状态
    //private String state;

    //家教名称
    private String nickname;

    //擅长科目
    private String goodSubject;

    //单价
    //private String isAscByHourPrice;

    private Integer current;

    private Integer limit;
}
