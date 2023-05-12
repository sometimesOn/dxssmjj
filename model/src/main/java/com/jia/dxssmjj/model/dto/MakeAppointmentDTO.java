package com.jia.dxssmjj.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MakeAppointmentDTO {
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

    private BigDecimal price;


    @ApiModelProperty(value = "微信")
    @TableField("wechat")
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

    private List<List<String>> hour;

    @ApiModelProperty(value = "辅导时间")
    @TableField("hour_string")
    private String hourString;


}
