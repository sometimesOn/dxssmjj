package com.jia.dxssmjj.model.po.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jia.dxssmjj.model.po.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("user")
@ApiModel(description = "用户信息表")
public class User extends BaseEntity {

    public static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "真实姓名")
    @TableField("real_name")
    private String realName;

    @ApiModelProperty(value = "用户昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty(value = "角色")
    @TableField("role")
    private String role;

    @ApiModelProperty(value = "头像")
    @TableField("head_img")
    private String headImg;

    @ApiModelProperty(value = "余额")
    @TableField("balance")
    private BigDecimal balance;

    @ApiModelProperty("证件类型")
    @TableField("certificates_type")
    private String certificatesType;

    @ApiModelProperty("实名验证失败原因")
    @TableField("reason")
    private String reason;



    @ApiModelProperty("证件编号")
    @TableField("certificates_no")
    private String certificatesNo;

    @ApiModelProperty("证件路径")
    @TableField("certificates_url")
    private String certificatesUrl;

    @ApiModelProperty("认证进度")
    @TableField("certificates_status")
    private Integer certificatesStatus;

    @ApiModelProperty(value = "用户手机号")
    @TableField("phone")
    private String phone ;

    @ApiModelProperty(value = "用户邮箱")
    @TableField("email")
    private String email ;
}
