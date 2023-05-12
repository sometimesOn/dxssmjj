package com.jia.dxssmjj.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EvaluateAndMakeAppointmentDTO {


    @ApiModelProperty(value = "真实姓名")
    @TableField("real_name")
    private String realName;


    @ApiModelProperty(value = "流水号")
    @TableField("serial_number")
    private String serialNumber;


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

    @ApiModelProperty(value = "订单id")
    @TableField("make_appointment_id")
    private Long makeAppointmentId;


    @ApiModelProperty(value = "评价内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "得分")
    @TableField("rate")
    private BigDecimal rate;
}
