package com.jia.dxssmjj.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class EvaluateDTO {
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "教员id")
    @TableField("tutor_id")
    private Long tutorId;

    @ApiModelProperty(value = "教员id")
    @TableField("make_appointment_id")
    private Long makeAppointmentId;


    @ApiModelProperty(value = "评价内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "得分")
    @TableField("rate")
    private BigDecimal rate;

    private String status;

}
