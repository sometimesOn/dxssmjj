package com.jia.dxssmjj.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户实名认证")
public class UserAuthDTO {

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("证件类型")
    private String certificatesType;

    @ApiModelProperty("证件编号")
    private String certificatesNo;

    @ApiModelProperty("证件路径")
    private String certificatesUrl;

    @ApiModelProperty("认证进度")
    private String certificatesStatus;
}
