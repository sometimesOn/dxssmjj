package com.jia.dxssmjj.model.po.makeAppointment;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jia.dxssmjj.model.po.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@TableName("make_appointment")
@ApiModel(description = "预约表")
public class MakeAppointment extends BaseEntity {

    public static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "教员id")
    @TableField("tutor_id")
    private Long tutorId;

    @ApiModelProperty(value = "真实姓名")
    @TableField("real_name")
    private String realName;

    @ApiModelProperty(value = "性别")
    @TableField("gender")
    private String gender;

    @ApiModelProperty(value = "流水号")
    @TableField("serial_number")
    private String serialNumber;

    @ApiModelProperty(value = "认证")
    @TableField("auth")
    private Integer auth;

    @ApiModelProperty(value = "微信")
    @TableField("weChat")
    private String weChat;

    @ApiModelProperty(value = "订单状态")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "手机号码")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "订单总额")
    @TableField("total")
    private BigDecimal total;

    @ApiModelProperty(value = "科目")
    @TableField("subject")
    private String subject;


    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;


    @ApiModelProperty(value = "地址")
    @TableField("address")
    private String address;


    @ApiModelProperty(value = "辅导时间")
    @TableField("hour_string")
    private String hourString;


}
