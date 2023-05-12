package com.jia.dxssmjj.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class MsmDTO {

    private String phone;

    private String templateCode;

    private Map<String,Object> param;

}
