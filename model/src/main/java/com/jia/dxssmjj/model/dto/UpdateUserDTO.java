package com.jia.dxssmjj.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateUserDTO {

    private String nickName;
    private String headImg ;
    private BigDecimal balance;
    private String phone ;
    private String email ;
    private String password ;

}
