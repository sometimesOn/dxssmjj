package com.jia.dxssmjj.model.po.evaluate;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jia.dxssmjj.model.po.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("evaluate")
@ApiModel(description = "评价表")
public class Evaluate extends BaseEntity {

    public static final long serialVersionUID = 1L;

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

}
