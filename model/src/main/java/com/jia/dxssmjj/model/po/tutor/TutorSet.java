package com.jia.dxssmjj.model.po.tutor;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jia.dxssmjj.model.po.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("tutor_set")
@ApiModel(description = "教员信息表")
public class TutorSet extends BaseEntity {

    public static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "真实姓名")
    @TableField("real_name")
    private String realName;

    @ApiModelProperty(value = "头像url")
    @TableField("headImageUrl")
    private String headImageUrl;

    @ApiModelProperty(value = "昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "性别 1：男 2：女")
    @TableField("gender")
    private Integer gender;

    @ApiModelProperty(value = "年龄")
    @TableField("age")
    private Integer age;

    @ApiModelProperty(value = "QQ号")
    @TableField("qq")
    private String qq;

    @ApiModelProperty(value = "微信")
    @TableField("wechat")
    private String wechat;

    @ApiModelProperty(value = "手机号码")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;


    @ApiModelProperty(value = "个人简介")
    @TableField("person_intr")
    private String personIntr;

    @ApiModelProperty(value = "擅长科目")
    @TableField("good_subject")
    private String goodSubject;

    @ApiModelProperty(value = "课时费用")
    @TableField("hour_price")
    private BigDecimal hourPrice;

    @ApiModelProperty(value = "账户余额")
    @TableField("balance")
    private BigDecimal balance;

    @ApiModelProperty(value = "证件材料存放位置")
    @TableField("auth_url")
    private String authUrl;

    @ApiModelProperty(value = "大学生认证 1：已认证 0：未认证")
    @TableField("college_auth")
    private Integer collegeAuth;

    @ApiModelProperty(value = "教师认证失败原因")
    @TableField("reason")
    private String reason;

}
